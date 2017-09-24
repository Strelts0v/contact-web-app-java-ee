package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.utils.PageConfigurationManager;

public class SendEmailToContactsAction implements ContactAction{

    // param to identify client want only get page with form
    // or proceed searching
    private static final String EMAIL_SUBMIT_PARAM = "submit";

    private static final String EMAIL_PAGE_NAME = "path.page.jsp.email";

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(EMAIL_SUBMIT_PARAM));
        if(!isSubmit){
            page = PageConfigurationManager.getPageName(EMAIL_PAGE_NAME);
        } else {
            page = null;
        }
        return page;
    }
}
