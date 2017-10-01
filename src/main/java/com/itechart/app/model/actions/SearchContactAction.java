package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.utils.PageConfigurationManager;

public class SearchContactAction implements ContactAction{

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(
                ContactActionProperties.CONTACT_SUBMIT_PARAM));
        if(!isSubmit){
            page = PageConfigurationManager.getPageName(
                    ContactActionProperties.SEARCH_CONTACT_PAGE_NAME);
        } else {
            page = null;
        }
        return page;
    }
}
