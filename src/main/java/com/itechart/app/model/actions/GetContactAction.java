package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;

public class GetContactAction implements ContactAction{

    private final Logger logger = LoggerFactory.getLogger(GetContactAction.class);

    public String execute(RequestContent requestContent) {
        String page;

        final int contactId = (int) requestContent.getAttribute(ContactActionProperties.CONTACT_ATTRIBUTE_ID);

        ContactDao dao = null;
        try {
            dao = JdbcContactDao.newInstance();
            if (dao == null) {
                page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
            } else {
                Contact contact = dao.getContact(contactId);
                requestContent.insertAttribute(ContactActionProperties.CONTACT_ATTRIBUTE_NAME, contact);
                if(contact.getPhoto().getPhotoStream() != null) {
                    byte[] photoData = IOUtils.toByteArray(contact.getPhoto().getPhotoStream());
                    String encodedPhoto = Base64.getEncoder().encodeToString(photoData);
                    requestContent.insertAttribute(ContactActionProperties.CONTACT_ATTRIBUTE_IMAGE,
                            encodedPhoto);
                }

                page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_DETAIL_PAGE_NAME);
                dao.closeDao();
                logger.info("Getting of contact with id=" + contact.getContactId() + " was successful");
            }
        } catch (Exception e){
            logger.error(e.getMessage());
            try {
                if(dao != null) {
                    dao.closeDao();
                }
            } catch (ContactDaoException ex){
                logger.error(e.getMessage());
            }
            page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
        }
        logger.info("Return " + page + " to client");
        return page;
    }
}