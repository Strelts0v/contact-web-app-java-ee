package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.utils.PageConfigurationManager;

public class SendEmailToContactsAction implements ContactAction{

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(ContactActionProperties.EMAIL_SUBMIT_PARAM));
        if(!isSubmit){
            page = PageConfigurationManager.getPageName(ContactActionProperties.EMAIL_PAGE_NAME);
        } else {
            page = null;
        }
        return page;
    }
}
