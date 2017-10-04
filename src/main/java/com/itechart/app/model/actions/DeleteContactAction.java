package com.itechart.app.model.actions;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeleteContactAction implements ContactAction{

    private final Logger logger = LoggerFactory.getLogger(DeleteContactAction.class);

    public String execute(RequestContent requestContent) {
        String page;

        final String contactIdsStr = requestContent.getParameter(ContactActionProperties.CONTACT_PARAM_IDS);
        final int[] contactIds = parseContactIdsFromString(contactIdsStr);

        JdbcContactDao dao = null;
        try {
            dao = JdbcContactDao.newInstance();
            if (dao == null) {
                page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
            } else {
                dao.initializeDao();
                for (int i = 0; i < contactIds.length; i++) {
                    dao.deleteContact(contactIds[i]);
                    logger.info("Deleting of contact with id=" + contactIds[i] + " was successful.");
                }
                // after deleting return page with contacts without deleted ones
                List<Contact> contactList = dao.getContacts(ContactActionProperties.INITIAL_CONTACT_OFFSET,
                        ContactActionProperties.INITIAL_CONTACT_COUNT);
                requestContent.insertAttribute(ContactActionProperties.CONTACTS_ATTRIBUTE_NAME, contactList);

                page = PageConfigurationManager.getPageName(ContactActionProperties.CONTACT_LIST_PAGE_NAME);
                dao.closeDao(ContactActionProperties.CONTACT_UPDATE_WAS_SUCCESSFUL);
            }

        } catch (ContactDaoException cde){
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
        logger.info("Return " + page + " to client");
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
