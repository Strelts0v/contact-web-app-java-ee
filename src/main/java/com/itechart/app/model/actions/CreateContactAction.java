package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.ContactMapper;
import com.itechart.app.model.utils.PageConfigurationManager;

import java.text.ParseException;

public class CreateContactAction implements ContactAction{

    // request attribute value
    private static final String CONTACT_REQUEST_ATTRIBUTE = "contact";

    // param to identify client want only get page with form
    // or create new contact
    private static final String CONTACT_SUBMIT_PARAM = "contact_submit";

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";
    private final static String CONTACT_DETAIL_PAGE_NAME = "path.page.jsp.contact-detail";

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(CONTACT_SUBMIT_PARAM));
        if(!isSubmit){
            page = PageConfigurationManager.getPageName(CONTACT_DETAIL_PAGE_NAME);
        } else {
            try {
                Contact contact = ContactMapper.mapContactParams(requestContent);

                JdbcContactDao dao = JdbcContactDao.newInstance();
                if (dao == null) {
                    page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
                } else {
                    contact = dao.createContact(contact);
                    requestContent.insertAttribute(CONTACT_REQUEST_ATTRIBUTE, contact);
                    page = PageConfigurationManager.getPageName(CONTACT_DETAIL_PAGE_NAME);
                    dao.closeConnection();
                }
            } catch (ParseException pe) {
                AppLogger.error(pe.getMessage());
                page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
            } catch (ContactDaoException cde){
                AppLogger.error(cde.getMessage());
                page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
            }
        }
        return page;
    }
}
