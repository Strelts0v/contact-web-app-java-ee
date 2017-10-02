package com.itechart.app.model.utils;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.entities.SearchContactDetails;

public class SearchContactDetailsMapper {

    public static SearchContactDetails mapSearchContactDetailsParams(RequestContent requestContent){
        SearchContactDetails details = new SearchContactDetails();

        details.setFirstName(requestContent.getParameter(ContactActionProperties.SEARCH_FIRST_NAME_PARAM));
        details.setSurname(requestContent.getParameter(ContactActionProperties.SEARCH_SURNAME_PARAM));
        details.setPatronymic(requestContent.getParameter(ContactActionProperties.SEARCH_PATRONYMIC_PARAM));
        details.setBirthdayFrom(requestContent.getParameter(ContactActionProperties.SEARCH_BIRTHDAY_FROM_PARAM));
        details.setBirthdayTo(requestContent.getParameter(ContactActionProperties.SEARCH_BIRTHDAY_TO_PARAM));
        details.setGender(requestContent.getParameter(ContactActionProperties.SEARCH_GENDER_PARAM));
        details.setNationality(requestContent.getParameter(ContactActionProperties.SEARCH_NATIONALITY_PARAM));
        details.setMaritalStatus(requestContent.getParameter(ContactActionProperties.SEARCH_MARITAL_STATUS_PARAM));
        details.setCountry(requestContent.getParameter(ContactActionProperties.SEARCH_COUNTRY_PARAM));
        details.setCity(requestContent.getParameter(ContactActionProperties.SEARCH_CITY_PARAM));
        details.setAddress(requestContent.getParameter(ContactActionProperties.SEARCH_ADDRESS_PARAM));
        details.setIndexNumber(requestContent.getParameter(ContactActionProperties.SEARCH_INDEX_PARAM));

        return details;
    }
}
