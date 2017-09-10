package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;

/**
 * specifies contract for handling client requests
 */
public interface ContactAction {

    /**
     * handles request from client
     * @param requestContent - wrapper with all request params and attributes
     * @return path to page for client
     */
    String execute(RequestContent requestContent);
}
