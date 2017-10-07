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
import com.itechart.app.model.utils.ContactMapper;
import com.itechart.app.model.utils.PageConfigurationManager;
import com.itechart.app.model.utils.PhoneParser;
import com.itechart.app.model.utils.PhotoParser;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateContactAction implements ContactAction{

    private final Logger logger = LoggerFactory.getLogger(CreateContactAction.class);

    private Map<String, String> contactPropertiesMap;
    private Map<String, Attachment> attachmentsMap;
    private List<Phone> phones;
    private Photo photo = Photo.EMPTY_PHOTO;

    public CreateContactAction(){
        contactPropertiesMap = new HashMap<>();
        attachmentsMap = new HashMap<>();
    }

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(
                ContactActionProperties.CONTACT_SUBMIT_PARAM));
        if(!isSubmit){
            page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_DETAIL_PAGE_NAME);
            logger.info("Get contact-detail.jsp for creating new contact.");
        } else {
            // parse FileItem objects into Contact entities
            List<FileItem> items = (List<FileItem>) requestContent.getAttribute(ContactActionProperties.FILE_ITEMS_ATTRIBUTE);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    handleFormField(item);
                } else {
                    handleFile(item);
                }
            }
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
                    contact = dao.createContact(contact);
                    // add new attachments
                    for(Attachment attachment : attachmentsMap.values()){
                        attachment.setContactId(contact.getContactId());
                        dao.addAttachmentToContact(contact.getContactId(), attachment);
                    }
                    // add new phone
                    for(Phone phone : phones){
                        phone.setContactId(contact.getContactId());
                        dao.addPhoneToContact(contact.getContactId(), phone);
                    }
                    // save updated contact as attribute for sending on client
                    contact = dao.getContact(contact.getContactId());
                    requestContent.insertAttribute(ContactActionProperties.CONTACT_REQUEST_ATTRIBUTE, contact);
                    requestContent.insertAttribute(
                            ContactActionProperties.WAS_CONTACT_SUCCESSFULLY_SAVED_REQUEST_ATTRIBUTE,
                            ContactActionProperties.CONTACT_UPDATE_WAS_SUCCESSFUL);

                    dao.closeDao(ContactActionProperties.CONTACT_UPDATE_WAS_SUCCESSFUL);

                    page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_DETAIL_PAGE_NAME);
                }
                logger.info("Creating of new contact was successful.");
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
        }
        logger.info("Return " + page + " to client");
        return page;
    }

    private void handleFormField(FileItem item){
        if(item.getFieldName().equals(ContactActionProperties.PHONES_PARAM)){
            phones = PhoneParser.parsePhones(item);
        } else if (item.getFieldName().equals(ContactActionProperties.ATTACHMENTS_PARAM)){
            List<Attachment> attachments = AttachmentParser.parseAttachments(item);
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
                attachment.setFileStream(item.getInputStream());
                attachment.setFileSize((int) item.getSize());
            }
        } catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}
