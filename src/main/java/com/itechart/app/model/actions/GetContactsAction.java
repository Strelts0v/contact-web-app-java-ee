package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.utils.PageConfigurationManager;

import java.util.List;

public class GetContactsAction implements ContactAction{

    private final static String CONTACT_PAGE_PARAM_NAME = "page";

    private final static String CONTACTS_ATTRIBUTE_NAME = "contactList";

    private final static String CONTACT_COUNT_ATTRIBUTE_NAME = "pageCount";

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";

    private final static String CONTACT_LIST_PAGE_NAME = "path.page.jsp.contact-list";

    private final static int DEFAULT_CONTACT_COUNT = 20;

    public String execute(RequestContent requestContent) {
        String page;

        final String pageIndexStr = requestContent.getParameter(CONTACT_PAGE_PARAM_NAME);
        final int pageIndex = Integer.parseInt(pageIndexStr);
        final int offset = (pageIndex - 1) * DEFAULT_CONTACT_COUNT;

        JdbcContactDao dao = JdbcContactDao.newInstance();
        if(dao == null){
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
        } else {
            int contactCount = dao.getContactCount();
            List<Contact> contactList = dao.getContacts(offset, DEFAULT_CONTACT_COUNT);
            requestContent.insertAttribute(CONTACTS_ATTRIBUTE_NAME, contactList);

            int pageCount = contactCount % DEFAULT_CONTACT_COUNT == 0
                    ? contactCount / DEFAULT_CONTACT_COUNT
                    : contactCount / DEFAULT_CONTACT_COUNT + 1;

            requestContent.insertAttribute(CONTACT_COUNT_ATTRIBUTE_NAME, pageCount);

            page = PageConfigurationManager.getPageName(CONTACT_LIST_PAGE_NAME);
            dao.closeConnection();
        }
        return page;
    }
}