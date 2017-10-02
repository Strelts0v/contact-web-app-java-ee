package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.SearchContactDetails;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.PageConfigurationManager;
import com.itechart.app.model.utils.SearchContactDetailsMapper;

import java.util.List;

public class SearchContactAction implements ContactAction{

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(
                ContactActionProperties.CONTACT_SUBMIT_PARAM));
        if(!isSubmit){
            page = PageConfigurationManager.getPageName(
                    ContactActionProperties.SEARCH_CONTACT_PAGE_NAME);
        } else {
            SearchContactDetails details = SearchContactDetailsMapper
                    .mapSearchContactDetailsParams(requestContent);
            ContactDao dao = null;
            try{
                dao = JdbcContactDao.newInstance();

                List<Contact> contacts = dao.findContacts(details);
                requestContent.insertAttribute(ContactActionProperties.CONTACTS_ATTRIBUTE_NAME, contacts);
                page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_LIST_PAGE_NAME);

                dao.closeDao();
                AppLogger.info("After searching for contacts was found " + contacts.size() + " contacts");
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
        }
        AppLogger.info("Return " + page + " to client");
        return page;
    }
}