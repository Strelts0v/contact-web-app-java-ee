package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.utils.PageConfigurationManager;

public class SearchContactAction implements ContactAction{

    // param to identify client want only get page with form
    // or proceed searching
    private static final String CONTACT_SUBMIT_PARAM = "submit";

    private static final String SEARCH_CONTACT_PAGE_NAME = "path.page.jsp.search";

    public String execute(RequestContent requestContent) {
        String page;

        Boolean isSubmit = Boolean.valueOf(requestContent.getParameter(CONTACT_SUBMIT_PARAM));
        if(!isSubmit){
            page = PageConfigurationManager.getPageName(SEARCH_CONTACT_PAGE_NAME);
        } else {
            page = null;
        }
        return page;
    }
}
