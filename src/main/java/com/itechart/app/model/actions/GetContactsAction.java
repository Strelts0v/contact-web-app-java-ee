package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetContactsAction implements ContactAction{

    private final Logger logger = LoggerFactory.getLogger(GetContactAction.class);

    public String execute(RequestContent requestContent) {
        String page;

        final String pageIndexStr = requestContent.getParameter(ContactActionProperties.CONTACT_PAGE_PARAM_NAME);
        final int pageIndex = Integer.parseInt(pageIndexStr);
        int offset = (pageIndex - 1) * ContactActionProperties.DEFAULT_CONTACT_COUNT;

        ContactDao dao = null;
        try {
            dao = JdbcContactDao.newInstance();
            if (dao == null) {
                page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
            } else {
                int contactCount = dao.getContactCount();
                offset = validateContactOffset(offset, contactCount);
                List<Contact> contactList = dao.getContacts(offset, ContactActionProperties.DEFAULT_CONTACT_COUNT);
                requestContent.insertAttribute(ContactActionProperties.CONTACTS_ATTRIBUTE_NAME, contactList);

                int pageCount = contactCount % ContactActionProperties.DEFAULT_CONTACT_COUNT == 0
                        ? contactCount / ContactActionProperties.DEFAULT_CONTACT_COUNT
                        : contactCount / ContactActionProperties.DEFAULT_CONTACT_COUNT + 1;

                requestContent.insertAttribute(ContactActionProperties.CONTACT_COUNT_ATTRIBUTE_NAME, pageCount);

                page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_LIST_PAGE_NAME);
                // close connection with database
                dao.closeDao();
                logger.info("Getting contacts according page=" + pageIndex + " was successful");
            }
        }catch (ContactDaoException cde){
            logger.error(cde.getMessage());
            try {
                if(dao != null) {
                    dao.closeDao();
                }
            } catch (ContactDaoException cdex){
                logger.error(cde.getMessage());
            }
            page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
        }
        logger.info("Return " + page + " to client");
        return page;
    }

    private int validateContactOffset(int offset, int contactCount) {
        if(offset > contactCount || offset < 0){
            offset = ContactActionProperties.INITIAL_CONTACT_OFFSET;
        }
        return offset;
    }
}