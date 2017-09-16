package com.itechart.app.model.actions;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.utils.PageConfigurationManager;

import java.util.List;

public class DeleteContactAction implements ContactAction{

    private static final String CONTACT_PARAM_IDS = "selectedContacts";

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";

    private final static String CONTACT_LIST_PAGE_NAME = "path.page.jsp.contact-list";

    private final static int INITIAL_CONTACT_OFFSET = 0;

    private final static int INITIAL_CONTACT_COUNT = 20;

    private final static String CONTACTS_ATTRIBUTE_NAME = "contactList";

    public String execute(RequestContent requestContent) {
        String page;

        final String contactIdsStr = requestContent.getParameter(CONTACT_PARAM_IDS);
        final int[] contactIds = parseContactIdsFromString(contactIdsStr);

        JdbcContactDao dao = JdbcContactDao.newInstance();
        if(dao == null){
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
        } else {
            for(int i = 0; i < contactIds.length; i++){
                dao.deleteContact(contactIds[i]);
            }
            // after deleting return page with contacts without deleted ones
            List<Contact> contactList = dao.getContacts(INITIAL_CONTACT_OFFSET, INITIAL_CONTACT_COUNT);
            requestContent.insertAttribute(CONTACTS_ATTRIBUTE_NAME, contactList);

            page = PageConfigurationManager.getPageName(CONTACT_LIST_PAGE_NAME);
            dao.closeConnection();
        }
        return page;
    }

    private int[] parseContactIdsFromString(final String contactIdsStr) {
        final String splitterValue = ",";

        Iterable<String> idsStrIterator = Splitter.on(splitterValue)
                .omitEmptyStrings()
                .split(contactIdsStr);

        int idsArraySize = Lists.newArrayList(idsStrIterator).size();
        int[] ids = new int[idsArraySize];
        int i = 0;
        for(String idStr : idsStrIterator){
            int id = Integer.parseInt(idStr);
            if(id != 0){
                ids[i++] = id;
            }
        }
        return ids;
    }
}
