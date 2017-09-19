package com.itechart.app.model.enums;

import com.itechart.app.model.actions.*;

public enum ContactActionEnum {

    CREATE_CONTACT{
        {
            action = new CreateContactAction();
        }
    },
    GET_CONTACT{
        {
            action = new GetContactAction();
        }
    },
    GET_CONTACTS{
        {
            action = new GetContactsAction();
        }
    },
    UPDATE_CONTACT{
        {
            action = new UpdateContactAction();
        }
    },
    DELETE_CONTACT{
        {
            action = new DeleteContactAction();
        }
    },
    SEARCH_CONTACT{
        {
            action = new SearchContactAction();
        }
    },
    ADD_ATTACHMENT_TO_CONTACT{
        {
            action = new AddAttachmentToContactAction();
        }
    },
    SEND_EMAIL_TO_CONTACTS{
        {
            action = new SendEmailToContactsAction();
        }
    },
    ERROR{
        {
            action = new ErrorContactAction();
        }
    };

    ContactAction action;

    public ContactAction getCurrentContactAction(){
        return action;
    }
}
