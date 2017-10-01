package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.utils.PageConfigurationManager;

public class ErrorContactAction implements ContactAction{

    public String execute(RequestContent requestContent) {
        return PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
    }
}
