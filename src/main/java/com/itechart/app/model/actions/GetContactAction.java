package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.utils.PageConfigurationManager;

public class GetContactAction implements ContactAction{

    private static final String CONTACT_ATTRIBUTE_ID = "contactId";

    private static final String CONTACT_ATTRIBUTE_NAME = "contact";

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";

    private final static String CONTACT_DETAILS_PAGE_NAME = "path.page.jsp.contact-detail";

    public String execute(RequestContent requestContent) {
        String page;

        final int contactId = (int) requestContent.getAttribute(CONTACT_ATTRIBUTE_ID);

        JdbcContactDao dao = JdbcContactDao.newInstance();
        if(dao == null){
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
        } else {
            Contact contact = dao.getContact(contactId);
            requestContent.insertAttribute(CONTACT_ATTRIBUTE_NAME, contact);

            page = PageConfigurationManager.getPageName(CONTACT_DETAILS_PAGE_NAME);
            dao.closeConnection();
        }

        return page;
    }
}