package com.itechart.app.model.actions.utils;

/**
 * holds constants for getting and extracting properties during
 * performing of contact action
 */
public interface ContactActionProperties {

    // common properties for all actions
    String ERROR_PAGE_NAME = "path.page.jsp.error";
    String CONTACT_DETAIL_PAGE_NAME = "path.page.jsp.contact-detail";
    String CONTACT_LIST_PAGE_NAME = "path.page.jsp.contact-list";
    String SEARCH_CONTACT_PAGE_NAME = "path.page.jsp.search";
    String EMAIL_PAGE_NAME = "path.page.jsp.email";
    String CONTACTS_ATTRIBUTE_NAME = "contactList";
    String CONTACT_PARAM_IDS = "selectedContacts";
    int INITIAL_CONTACT_OFFSET = 0;
    int INITIAL_CONTACT_COUNT = 20;
    String CONTACT_SUBMIT_PARAM = "submit";

    // Update and create contact actions properties
    String FILE_ITEMS_ATTRIBUTE = "fileItems";
    String PHONES_PARAM = "phones";
    String ATTACHMENTS_PARAM = "attachments";
    String PHOTO_PARAM = "photo";
    String CONTACT_REQUEST_ATTRIBUTE = "contact";
    String WAS_CONTACT_SUCCESSFULLY_SAVED_REQUEST_ATTRIBUTE = "wasSaved";
    String CONTACT_ID_PARAM_NAME = "id_contact";
    Boolean CONTACT_UPDATE_WAS_SUCCESSFUL = true;
    Boolean CONTACT_UPDATE_WAS_UNSUCCESSFUL = false;

    // Email contact action properties
    String EMAIL_SUBMIT_PARAM = "submit";
    String EMAILS_TO_SEND_PARAM = "emails-to-send";
    String EMAIL_TEMPLATE_PARAM = "email-template";
    String EMAIL_BIRTHDAY_LAST_NAME_PARAM = "birthdayLastName";
    String EMAIL_BIRTHDAY_FIRST_NAME_PARAM = "birthdayFirstName";
    String EMAIL_PHONE_PARAM = "phone";
    String EMAIL_FROM_PARAM = "email";
    String EMAIL_FIRST_NAME_PARAM = "firstname";
    String EMAIL_LAST_NAME_PARAM = "lastname";
    String EMAIL_TO_FIRST_NAME_PARAM = "firstnameTo";
    String EMAIL_TO_LAST_NAME_PARAM = "lastnameTo";
    String EMAIL_TO_FIRST_NAME_DEFAULT_PARAM = "";
    String EMAIL_TO_LAST_NAME_DEFAULT_PARAM = "";
    String WAS_SENDING_EMAIL_SUCCESSFUL_REQUEST_ATTRIBUTE = "wasSend";
    Boolean SENDING_EMAIL_WAS_SUCCESSFUL = true;
    Boolean SENDING_EMAIL_WAS_UNSUCCESSFUL = false;
    String EMAIL_MESSAGE_PARAM = "message";
    String EMAIL_SUBJECT_PARAM = "subject";
    String EMAIL_INITIAL_EMAILS_TO_SEND_PARAM = "emails-ids-to-send";
    String EMAIL_INITIAL_EMAIL_TO_SEND_REQUEST_ATTRIBUTE = "emailsToSend";

    // Get contacts action properties
    String CONTACT_PAGE_PARAM_NAME = "page";
    String CONTACT_COUNT_ATTRIBUTE_NAME = "pageCount";
    int DEFAULT_CONTACT_COUNT = 20;

    // Get contact action properties
    String CONTACT_ATTRIBUTE_ID = "contactId";
    String CONTACT_ATTRIBUTE_NAME = "contact";
    String CONTACT_ATTRIBUTE_IMAGE = "image";

    // Search contact action properties
    String SEARCH_FIRST_NAME_PARAM = "firstname";
    String SEARCH_SURNAME_PARAM = "surname";
    String SEARCH_PATRONYMIC_PARAM = "patronymic";
    String SEARCH_BIRTHDAY_FROM_PARAM = "birthday-from";
    String SEARCH_BIRTHDAY_TO_PARAM = "birthday-to";
    String SEARCH_GENDER_PARAM = "gender";
    String SEARCH_NATIONALITY_PARAM = "nationality";
    String SEARCH_MARITAL_STATUS_PARAM = "marital-status";
    String SEARCH_COUNTRY_PARAM = "country";
    String SEARCH_CITY_PARAM = "city";
    String SEARCH_ADDRESS_PARAM = "address";
    String SEARCH_INDEX_PARAM = "index";
}