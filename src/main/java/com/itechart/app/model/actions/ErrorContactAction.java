package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.utils.PageConfigurationManager;

public class ErrorContactAction implements ContactAction{

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";

    public String execute(RequestContent requestContent) {
        return PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
    }
}
