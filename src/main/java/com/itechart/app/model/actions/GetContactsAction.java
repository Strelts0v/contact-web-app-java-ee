package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.utils.PageConfigurationManager;

import java.util.List;

public class GetContactsAction implements ContactAction{

    private final static String OFFSET_PARAM_NAME = "offset";

    private final static String COUNT_PARAM_NAME = "count";

    private final static String CONTACTS_ATTRIBUTE_NAME = "contactList";

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";

    private final static String CONTACT_LIST_PAGE_NAME = "path.page.jsp.contact-list";

    private final static int DEFAULT_OFFSET = 0;

    private final static int DEFAULT_COUNT = 20;

    public String execute(RequestContent requestContent) {
        String page;

        int offset = Integer.parseInt(requestContent.getParameter(OFFSET_PARAM_NAME));
        int count = Integer.parseInt(requestContent.getParameter(COUNT_PARAM_NAME));

        if(count == 0){
            offset = DEFAULT_OFFSET;
            count = DEFAULT_COUNT;
        }

        JdbcContactDao dao = JdbcContactDao.newInstance();
        if(dao == null){
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
        } else {
            List<Contact> contactList = dao.getContacts(offset, count);
            requestContent.insertAttribute(CONTACTS_ATTRIBUTE_NAME, contactList);

            page = PageConfigurationManager.getPageName(CONTACT_LIST_PAGE_NAME);
            dao.closeConnection();
        }

        return page;
    }
}