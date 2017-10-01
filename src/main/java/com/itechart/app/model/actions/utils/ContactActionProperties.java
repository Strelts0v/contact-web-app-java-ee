package com.itechart.app.model.actions.utils;

/**
 * holds constants for getting and extracting properties during
 * performing of contact action
 */
public class ContactActionProperties {

    /**
     * makes class non-instantiation
     */
    private ContactActionProperties(){}

    // common properties for all actions
    public final static String ERROR_PAGE_NAME = "path.page.jsp.error";
    public final static String CONTACT_DETAIL_PAGE_NAME = "path.page.jsp.contact-detail";
    public final static String CONTACT_LIST_PAGE_NAME = "path.page.jsp.contact-list";
    public final static String SEARCH_CONTACT_PAGE_NAME = "path.page.jsp.search";
    public final static String EMAIL_PAGE_NAME = "path.page.jsp.email";
    public final static String CONTACTS_ATTRIBUTE_NAME = "contactList";
    public final static String CONTACT_PARAM_IDS = "selectedContacts";
    public final static int INITIAL_CONTACT_OFFSET = 0;
    public final static int INITIAL_CONTACT_COUNT = 20;
    public final static String CONTACT_SUBMIT_PARAM = "submit";


    // Update and create contact actions properties
    public final static String FILE_ITEMS_ATTRIBUTE = "fileItems";
    public final static String PHONES_PARAM = "phones";
    public final static String ATTACHMENTS_PARAM = "attachments";
    public final static String PHOTO_PARAM = "photo";
    public final static String CONTACT_REQUEST_ATTRIBUTE = "contact";
    public final static String WAS_CONTACT_SUCCESSFULLY_SAVED_REQUEST_ATTRIBUTE = "wasSaved";
    public final static String CONTACT_ID_PARAM_NAME = "id_contact";
    public final static Boolean CONTACT_UPDATE_WAS_SUCCESSFUL = true;
    public final static Boolean CONTACT_UPDATE_WAS_UNSUCCESSFUL = false;

    // Email contact action properties
    public final static String EMAIL_SUBMIT_PARAM = "submit";
    public final static String EMAILS_TO_SEND_PARAM = "emails-to-send";
    public final static String EMAIL_TEMPLATE_PARAM = "email-template";
    public final static String EMAIL_BIRTHDAY_LAST_NAME_PARAM = "birthday-last-name";
    public final static String EMAIL_BIRTHDAY_FIRST_NAME_PARAM = "birthday-first-name";
    public final static String EMAIL_PHONE_PARAM = "phone";
    public final static String EMAIL_FROM_PARAM = "email";
    public final static String EMAIL_FIRST_NAME_PARAM = "firstname";
    public final static String EMAIL_LAST_NAME_PARAM = "lastname";
    public final static String WAS_SENDING_EMAIL_SUCCESSFUL_REQUEST_ATTRIBUTE = "wasSend";
    public final static Boolean SENDING_EMAIL_WAS_SUCCESSFUL = true;
    public final static Boolean SENDING_EMAIL_WAS_UNSUCCESSFUL = false;
    public final static String EMAIL_MESSAGE_PARAM = "message";
    public final static String EMAIL_SUBJECT_PARAM = "subject";
    public final static String EMAIL_INITIAL_EMAILS_TO_SEND_PARAM = "emails-ids-to-send";
    public final static String EMAIL_INITIAL_EMAIL_TO_SEND_REQUEST_ATTRIBUTE = "emailsToSend";

    // Get contacts action properties
    public final static String CONTACT_PAGE_PARAM_NAME = "page";
    public final static String CONTACT_COUNT_ATTRIBUTE_NAME = "pageCount";
    public final static int DEFAULT_CONTACT_COUNT = 20;

    // Get contact action properties
    public static final String CONTACT_ATTRIBUTE_ID = "contactId";
    public static final String CONTACT_ATTRIBUTE_NAME = "contact";
}
