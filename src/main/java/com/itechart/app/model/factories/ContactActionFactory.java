package com.itechart.app.model.factories;

import com.itechart.app.model.actions.ContactAction;
import com.itechart.app.model.enums.ContactActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactActionFactory {

    private final Logger logger = LoggerFactory.getLogger(ContactActionFactory.class);

    public ContactAction defineContactAction(final String actionFromRestUrl){
        ContactActionEnum contactActionEnum;
        try{
            contactActionEnum = ContactActionEnum.valueOf(actionFromRestUrl.toUpperCase());
            logger.info(contactActionEnum.getCurrentContactAction().getClass() +
                    " instance is successfully returned from ContactActionFactory");
        } catch (IllegalArgumentException e) {
            contactActionEnum = ContactActionEnum.ERROR;
        }
        return contactActionEnum.getCurrentContactAction();
    }
}
