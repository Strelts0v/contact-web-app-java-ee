package com.itechart.app.model.enums;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;

import java.util.HashMap;
import java.util.Map;

public enum EmailTemplateEnum {

    DEFAULT, BIRTHDAY;

    public Map<String, String> getEmailParamsMap(RequestContent requestContent){
        Map<String, String> emailParamsMap = new HashMap<>();
        switch (this){
            case DEFAULT:
                emailParamsMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_FIRST_NAME_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_LAST_NAME_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_FROM_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_FROM_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_PHONE_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_PHONE_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_SUBJECT_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_SUBJECT_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_MESSAGE_PARAM));
                break;

            case BIRTHDAY:
                emailParamsMap.put(ContactActionProperties.EMAIL_FIRST_NAME_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_FIRST_NAME_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_LAST_NAME_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_LAST_NAME_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_FROM_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_FROM_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_PHONE_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_PHONE_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_BIRTHDAY_FIRST_NAME_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_BIRTHDAY_FIRST_NAME_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_BIRTHDAY_LAST_NAME_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_BIRTHDAY_LAST_NAME_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_SUBJECT_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_SUBJECT_PARAM));
                emailParamsMap.put(ContactActionProperties.EMAIL_MESSAGE_PARAM,
                        requestContent.getParameter(ContactActionProperties.EMAIL_MESSAGE_PARAM));
                break;
        }
        return emailParamsMap;
    }
}
