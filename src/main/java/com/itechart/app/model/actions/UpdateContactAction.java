package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.Phone;
import com.itechart.app.model.entities.Photo;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.*;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateContactAction implements ContactAction{

    private Map<String, String> contactPropertiesMap;
    private Map<String, Attachment> attachmentsMap;
    private List<Phone> phones;
    private Photo photo = Photo.EMPTY_PHOTO;
    private int contactId;

    public UpdateContactAction(){
        contactPropertiesMap = new HashMap<>();
        attachmentsMap = new HashMap<>();
    }

    public String execute(RequestContent requestContent) {
        String page;

        // parse FileItem objects into Contact entities
        List<FileItem> items = (List<FileItem>) requestContent.getAttribute(
                ContactActionProperties.FILE_ITEMS_ATTRIBUTE);
        for (FileItem item : items) {
            if (item.isFormField()) {
                handleFormField(item);
            } else {
                handleFile(item);
            }
        }
        // Sort phones and attachment into new and old
        PhoneSorter phoneSorter = new PhoneSorter(phones);
        List<Phone> oldPhones = phoneSorter.getOldPhones();
        List<Phone> newPhones = phoneSorter.getNewPhones();

        AttachmentSorter attachmentSorter = new AttachmentSorter(attachmentsMap);
        List<Attachment> oldAttachments = attachmentSorter.getOldAttachments();
        List<Attachment> newAttachments = attachmentSorter.getNewAttachments();

        // Map contacts properties
        Contact contact = ContactMapper.mapContactParams(contactPropertiesMap);
        contact.setPhoto(photo);

        ContactDao dao = null;
        try {
            dao = JdbcContactDao.newInstance();
            if (dao == null) {
                page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
            } else {
                // start jdbc transaction
                dao.initializeDao();
                dao.updateContact(getContactId(), contact);
                // update old attachments
                for(Attachment attachment : oldAttachments){
                    dao.updateAttachmentFromContact(attachment.getAttachmentId(), attachment);
                }
                // add new attachments
                for(Attachment attachment : newAttachments){
                    dao.addAttachmentToContact(getContactId(), attachment);
                }
                // update old phones
                for(Phone phone : oldPhones){
                    dao.updatePhoneFromContact(phone.getPhoneId(), phone);
                }
                // add new phone
                for(Phone phone : newPhones){
                    dao.addPhoneToContact(getContactId(), phone);
                }
                // save updated contact as attribute for sending on client
                contact = dao.getContact(getContactId());
                requestContent.insertAttribute(ContactActionProperties.CONTACT_REQUEST_ATTRIBUTE, contact);
                requestContent.insertAttribute(
                        ContactActionProperties.WAS_CONTACT_SUCCESSFULLY_SAVED_REQUEST_ATTRIBUTE,
                        ContactActionProperties.CONTACT_UPDATE_WAS_SUCCESSFUL);

                dao.closeDao(ContactActionProperties.CONTACT_UPDATE_WAS_SUCCESSFUL);

                page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_DETAIL_PAGE_NAME);
            }
        }catch (ContactDaoException cde){
            AppLogger.error(cde.getMessage());
            if(dao != null) {
                try {
                    dao.closeDao(ContactActionProperties.CONTACT_UPDATE_WAS_UNSUCCESSFUL);
                } catch (ContactDaoException cdex){
                    AppLogger.error(cdex.getMessage());
                }
            }
            page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
        }
        return page;
    }

    private void handleFormField(FileItem item){
        if(item.getFieldName().equals(ContactActionProperties.PHONES_PARAM)){
            phones = PhoneParser.parsePhones(item, getContactId());
        } else if (item.getFieldName().equals(ContactActionProperties.ATTACHMENTS_PARAM)){
            List<Attachment> attachments = AttachmentParser.parseAttachments(item, getContactId());
            for(Attachment attachment: attachments){
                attachmentsMap.put(attachment.getFileName(), attachment);
            }
        } else {
            contactPropertiesMap.put(item.getFieldName(), item.getString());
        }
    }

    private void handleFile(FileItem item){
        try {
            if (item.getFieldName().equals(ContactActionProperties.PHOTO_PARAM)) {
                photo = PhotoParser.parsePhoto(item);
                return;
            }

            Attachment attachment = attachmentsMap.get(item.getFieldName());
            if (attachment != null) {
                attachment.setContactId(getContactId());
                attachment.setFileStream(item.getInputStream());
                attachment.setFileSize((int) item.getSize());
            }
        } catch (IOException e){
            AppLogger.error(e.getMessage());
        }
    }

    private int getContactId(){
        if(contactId == 0) {
            String contactIdStr = contactPropertiesMap.get(ContactActionProperties.CONTACT_ID_PARAM_NAME);
            contactId = Integer.parseInt(contactIdStr);
        }
        return contactId;
    }
}
