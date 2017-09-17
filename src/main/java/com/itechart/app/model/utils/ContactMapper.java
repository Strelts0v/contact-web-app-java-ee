package com.itechart.app.model.utils;


import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.entities.Address;
import com.itechart.app.model.entities.Contact;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * transforms info from contact form into new Contact object
 */
public class ContactMapper {

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

    public static Contact mapContactParams(RequestContent requestContent) throws ParseException {
        Contact contact = new Contact();

        contact.setSurname(requestContent.getParameter(SURNAME_PARAM));
        contact.setFirstName(requestContent.getParameter(FIRSTNAME_PARAM));
        contact.setPatronymic(requestContent.getParameter(PATRONYMIC_PARAM));
        contact.setGender(requestContent.getParameter(GENDER_PARAM));
        contact.setNationality(requestContent.getParameter(NATIONALITY_PARAM));
        contact.setMaritalStatus(requestContent.getParameter(MARITAL_STATUS_PARAM));
        contact.setWebsite(requestContent.getParameter(WEBSITE_PARAM));
        contact.setEmail(requestContent.getParameter(EMAIL_PARAM));
        contact.setCompany(requestContent.getParameter(COMPANY_PARAM));

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
