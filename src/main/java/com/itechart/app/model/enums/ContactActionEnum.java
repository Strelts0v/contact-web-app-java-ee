package com.itechart.app.model.enums;

import com.itechart.app.model.actions.*;

public enum ContactActionEnum {

    CREATE_CONTACT(new CreateContactAction()),
    GET_CONTACT(new GetContactAction()),
    GET_CONTACTS(new GetContactsAction()),
    UPDATE_CONTACT(new UpdateContactAction()),
    DELETE_CONTACT(new DeleteContactAction()),
    SEARCH_CONTACT(new SearchContactAction()),
    SEND_EMAIL_TO_CONTACTS(new SendEmailToContactsAction()),
    ERROR(new ErrorContactAction());

    ContactActionEnum(ContactAction action){
        this.action = action;
    }

    private ContactAction action;

    public ContactAction getCurrentContactAction(){
        return action;
    }
}
