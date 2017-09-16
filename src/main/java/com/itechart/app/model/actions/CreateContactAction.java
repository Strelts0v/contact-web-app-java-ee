package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Address;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.utils.PageConfigurationManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CreateContactAction implements ContactAction{

    // params from contact create form
    private static final String SURNAME_PARAM = "surname";
    private static final String FIRSTNAME_PARAM = "first_name";
    private static final String PATRONYMIC_PARAM = "patronymic";
    private static final String BIRTHDAY_PARAM = "birthday";
    private static final String GENDER_PARAM = "gender";
    private static final String NATIONALITY_PARAM = "nationality";
    private static final String MARITAL_STATUS_PARAM = "marital_status";
    private static final String WEBSITE_PARAM = "website";
    private static final String EMAIL_PARAM = "email";
    private static final String COMPANY_PARAM = "company";
    private static final String COUNTRY_PARAM = "country";
    private static final String CITY_PARAM = "city";
    private static final String ADDRESS_PARAM = "address";
    private static final String INDEX_PARAM = "index";

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
                Contact contact = mapContactParams(requestContent);

                JdbcContactDao dao = JdbcContactDao.newInstance();

                if (dao == null) {
                    page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
                } else {
                    contact = dao.createContact(contact);
                    requestContent.insertAttribute(CONTACT_REQUEST_ATTRIBUTE, contact);
                    page = PageConfigurationManager.getPageName(CONTACT_DETAIL_PAGE_NAME);
                    dao.closeConnection();
                }
            } catch (ParseException e) {
                AppLogger.error(e.getMessage());
                page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
            }
        }
        return page;
    }

    private Contact mapContactParams(RequestContent requestContent) throws ParseException{
        Contact contact = new Contact();

        contact.setSurname(requestContent.getParameter(SURNAME_PARAM));
        contact.setFirstName(requestContent.getParameter(FIRSTNAME_PARAM));
        contact.setPatronymic(requestContent.getParameter(PATRONYMIC_PARAM));
        contact.setGender(requestContent.getParameter(GENDER_PARAM));
        contact.setNationality(requestContent.getParameter(NATIONALITY_PARAM));
        contact.setMaritalStatus(requestContent.getParameter(MARITAL_STATUS_PARAM));
        contact.setWebsite(requestContent.getParameter(WEBSITE_PARAM));
        contact.setEmail(EMAIL_PARAM);
        contact.setCompany(COMPANY_PARAM);

        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        contact.setBirthday(formatter.parse(requestContent.getParameter(BIRTHDAY_PARAM)));

        Address address = new Address();
        address.setCountry(requestContent.getParameter(COUNTRY_PARAM));
        address.setCity(requestContent.getParameter(CITY_PARAM));
        address.setAddress(requestContent.getParameter(ADDRESS_PARAM));
        address.setIndex(requestContent.getParameter(INDEX_PARAM));

        contact.setAddress(address);

        return contact;
    }
}
