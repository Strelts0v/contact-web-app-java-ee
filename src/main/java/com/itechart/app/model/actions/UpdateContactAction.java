package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.Phone;
import com.itechart.app.model.entities.Photo;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.AttachmentParser;
import com.itechart.app.model.utils.AttachmentSorter;
import com.itechart.app.model.utils.ContactMapper;
import com.itechart.app.model.utils.PageConfigurationManager;
import com.itechart.app.model.utils.PhoneParser;
import com.itechart.app.model.utils.PhoneSorter;
import com.itechart.app.model.utils.PhotoParser;
import com.itechart.app.model.utils.StringUtf8Encoder;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdateContactAction implements ContactAction{

    private final Logger logger = LoggerFactory.getLogger(UpdateContactAction.class);

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

                // Init list with all actual attachment ids;
                List<Integer> actualAttachmentIds = new ArrayList<>(attachmentsMap.size());
                // Init list with all actual phone ids
                List<Integer> actualPhoneIds = new ArrayList<>(phones.size());

                // update old attachments
                for(Attachment attachment : oldAttachments){
                    dao.updateAttachmentFromContact(attachment.getAttachmentId(), attachment);
                    actualAttachmentIds.add(attachment.getAttachmentId());
                }
                // add new attachments
                for(Attachment attachment : newAttachments){
                    int attachmentId = dao.addAttachmentToContact(getContactId(), attachment);
                    actualAttachmentIds.add(attachmentId);
                }
                // remove from database all deleted attachments
                List<Integer> storedAttachmentIds = dao.getContactAttachmentIds(getContactId());
                List<Integer> deleteAttachmentsIds = getDeletedIds(storedAttachmentIds, actualAttachmentIds);
                for(int attachmentId : deleteAttachmentsIds){
                    dao.deleteAttachmentFromContact(attachmentId);
                }

                // update old phones
                for(Phone phone : oldPhones){
                    dao.updatePhoneFromContact(phone.getPhoneId(), phone);
                    actualPhoneIds.add(phone.getPhoneId());
                }
                // add new phone
                for(Phone phone : newPhones){
                    int phoneId = dao.addPhoneToContact(getContactId(), phone);
                    actualPhoneIds.add(phoneId);
                }
                // remove from database all deleted phones
                List<Integer> storedPhoneIds = dao.getContactPhonesIds(getContactId());
                List<Integer> deletePhoneIds = getDeletedIds(storedPhoneIds, actualPhoneIds);
                for(int attachmentId : deletePhoneIds){
                    dao.deletePhoneFromContact(attachmentId);
                }

                // save updated contact as attribute for sending on client
                contact = dao.getContact(getContactId());
                requestContent.insertAttribute(ContactActionProperties.CONTACT_REQUEST_ATTRIBUTE, contact);
                requestContent.insertAttribute(
                        ContactActionProperties.WAS_CONTACT_SUCCESSFULLY_SAVED_REQUEST_ATTRIBUTE,
                        ContactActionProperties.CONTACT_UPDATE_WAS_SUCCESSFUL);

                dao.closeDao(ContactActionProperties.CONTACT_UPDATE_WAS_SUCCESSFUL);
                logger.info("Updating of contact with id=" + getContactId() + " was successful");
                page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_DETAIL_PAGE_NAME);
            }
        }catch (ContactDaoException cde){
            logger.error(cde.getMessage());
            if(dao != null) {
                try {
                    dao.closeDao(ContactActionProperties.CONTACT_UPDATE_WAS_UNSUCCESSFUL);
                } catch (ContactDaoException cdex){
                    logger.error(cdex.getMessage());
                }
            }
            page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
        }
        logger.info("Return " + page + " to client");
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
            contactPropertiesMap.put(item.getFieldName(), StringUtf8Encoder.getString(item));
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
            logger.error(e.getMessage());
        }
    }

    private int getContactId(){
        if(contactId == 0) {
            String contactIdStr = contactPropertiesMap.get(ContactActionProperties.CONTACT_ID_PARAM_NAME);
            contactId = Integer.parseInt(contactIdStr);
        }
        return contactId;
    }

    private List<Integer> getDeletedIds(List<Integer> storedIds, List<Integer> actualIds){
        HashSet<Integer> storedIdsSet = new HashSet(storedIds);
        HashSet<Integer> actualIdsSet = new HashSet(actualIds);

        Set<Integer> deletedIdsSet = symmetricDifference(storedIdsSet, actualIdsSet);
        return new ArrayList<>(deletedIdsSet);
    }

    private Set<Integer> symmetricDifference(Set<Integer> a, Set<Integer> b) {
        Set<Integer> result = new HashSet<>(a);
        for (Integer element : b) {
            // .add() returns false if element already exists
            if (!result.add(element)) {
                result.remove(element);
            }
        }
        return result;
    }
}
