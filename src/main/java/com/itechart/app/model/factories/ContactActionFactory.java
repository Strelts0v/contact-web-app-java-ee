package com.itechart.app.model.factories;

import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.actions.ContactAction;
import com.itechart.app.model.enums.ContactActionEnum;

public class ContactActionFactory {

    public ContactAction defineContactAction(final String actionFromRestUrl){
        ContactActionEnum contactActionEnum;
        try{
            contactActionEnum = ContactActionEnum.valueOf(actionFromRestUrl.toUpperCase());
            AppLogger.info("ContactAction instance is successfully returned from ContactActionFactory");
        } catch (IllegalArgumentException e) {
            contactActionEnum = ContactActionEnum.ERROR;
        }
        return contactActionEnum.getCurrentContactAction();
    }
}
