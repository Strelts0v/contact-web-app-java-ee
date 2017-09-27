package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.ContactMapper;
import com.itechart.app.model.utils.PageConfigurationManager;

import java.text.ParseException;

public class UpdateContactAction implements ContactAction{

    private static final String ID_PARAM = "id_contact";
    private static final String CONTACT_REQUEST_ATTRIBUTE = "contact";
    private static final String UPDATE_SUCCESS_ATTRIBUTE = "update_success";
    private static final String ERROR_PAGE_NAME = "path.page.jsp.error";
    private static final String CONTACT_DETAIL_PAGE_NAME = "path.page.jsp.contact-detail";

    public String execute(RequestContent requestContent) {
        String page = null;

        final int contactId = Integer.parseInt(requestContent.getParameter(ID_PARAM));
        try {
            Contact contact = ContactMapper.mapContactParams(requestContent);
//            List<Phone> phones = PhonesMapper.mapPhones(requestContent);
//            List<Attachment> attachments = AttachmentMapper.mapAttachments(requestContent);
            contact.setContactId(contactId);

            JdbcContactDao dao = JdbcContactDao.newInstance();
            if (dao == null) {
                page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
                requestContent.insertAttribute(UPDATE_SUCCESS_ATTRIBUTE, false);
            } else {
                dao.updateContact(contactId, contact);
                requestContent.insertAttribute(CONTACT_REQUEST_ATTRIBUTE, contact);
                requestContent.insertAttribute(UPDATE_SUCCESS_ATTRIBUTE, true);
                page = PageConfigurationManager.getPageName(CONTACT_DETAIL_PAGE_NAME);
                dao.closeConnection();
            }
        } catch (ParseException e) {
            AppLogger.error(e.getMessage());
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
            requestContent.insertAttribute(UPDATE_SUCCESS_ATTRIBUTE, false);
        } catch (ContactDaoException cde){
            AppLogger.error(cde.getMessage());
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
        } finally {
            return page;
        }
    }
}
