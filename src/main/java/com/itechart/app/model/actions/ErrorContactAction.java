package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorContactAction implements ContactAction{

    private final Logger logger = LoggerFactory.getLogger(ErrorContactAction.class);

    public String execute(RequestContent requestContent) {
        String page = PageConfigurationManager.getPageName(ContactActionProperties.ERROR_PAGE_NAME);
        logger.info("Return " + page + " to client");
        return page;
    }
}
