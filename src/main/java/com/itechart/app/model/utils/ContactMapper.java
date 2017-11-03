package com.itechart.app.model.utils;

import com.itechart.app.model.entities.Contact;

import java.util.Map;

/**
 * transforms info from contact form into new Contact object
 */
public class ContactMapper {

    private final static String ID_PARAM = "id_contact";
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

    private ContactMapper(){}

    public static Contact mapContactParams(Map<String, String> contactPropertiesMap) {
        Contact contact = new Contact();

        String contactIdStr = contactPropertiesMap.get(ID_PARAM);
        if(contactIdStr != null){
            contact.setContactId(Integer.parseInt(contactIdStr));
        }
        contact.setSurname(contactPropertiesMap.get(SURNAME_PARAM));
        contact.setFirstName(contactPropertiesMap.get(FIRSTNAME_PARAM));
        contact.setPatronymic(contactPropertiesMap.get(PATRONYMIC_PARAM));
        contact.setGender(contactPropertiesMap.get(GENDER_PARAM));
        contact.setNationality(contactPropertiesMap.get(NATIONALITY_PARAM));
        contact.setMaritalStatus(contactPropertiesMap.get(MARITAL_STATUS_PARAM));
        contact.setWebsite(contactPropertiesMap.get(WEBSITE_PARAM));
        contact.setEmail(contactPropertiesMap.get(EMAIL_PARAM));
        contact.setCompany(contactPropertiesMap.get(COMPANY_PARAM));
        contact.setBirthday(contactPropertiesMap.get(BIRTHDAY_PARAM).replace("\\.", "-"));
        contact.setCountry(contactPropertiesMap.get(COUNTRY_PARAM));
        contact.setCity(contactPropertiesMap.get(CITY_PARAM));
        contact.setAddress(contactPropertiesMap.get(ADDRESS_PARAM));
        contact.setIndexNumber(contactPropertiesMap.get(INDEX_PARAM));

        return contact;
    }
}
