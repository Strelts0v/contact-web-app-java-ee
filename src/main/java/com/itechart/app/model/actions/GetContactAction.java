package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.PageConfigurationManager;

public class GetContactAction implements ContactAction{

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

                page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_DETAIL_PAGE_NAME);
                dao.closeDao();
                AppLogger.info("Getting of contact with id=" + contact + " was successful");
            }
        } catch (ContactDaoException cde){
            AppLogger.error(cde.getMessage());
            try {
                if(dao != null) {
                    dao.closeDao();
                }
            } catch (ContactDaoException cdex){
                AppLogger.error(cde.getMessage());
            }
            page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
        }
        AppLogger.info("Return " + page + " to client");
        return page;
    }
}