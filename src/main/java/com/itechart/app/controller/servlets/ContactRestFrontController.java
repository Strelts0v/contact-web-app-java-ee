package com.itechart.app.controller.servlets;

import com.itechart.app.controller.utils.ContactUploadHelper;
import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.controller.utils.RestRequest;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.actions.ContactAction;
import com.itechart.app.model.actions.CreateContactAction;
import com.itechart.app.model.actions.UpdateContactAction;
import com.itechart.app.model.factories.ContactActionFactory;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ContactRestFrontController extends HttpServlet{

    private static final String RESOURCE_ID_ATTRIBUTE = "contactId";
    private static final String FILE_ITEMS_ATTRIBUTE = "fileItems";
    public final static String SUBMIT_PARAM = "submit";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Create RestRequest object to handle REST request
        RestRequest restRequest = new RestRequest(request.getPathInfo());

        // Create wrapper for HttpServletRequest for getting params from JSP
        // and inserting attributes back to JSP
        RequestContent requestContent = new RequestContent(request);

        // Check if the rest request contains resource id and if so
        // add this attribute to RequestContent
        if(restRequest.hasResourceId()){
            requestContent.insertAttribute(RESOURCE_ID_ATTRIBUTE, restRequest.getResourceId());
        }

        // identify command coming from JSP
        ContactActionFactory factory = new ContactActionFactory();
        ContactAction action = factory.defineContactAction(restRequest.getActionFromRestUrl());

        // check if there is upload or create contact action it is
        // use ContactUploadHelper to upload files and other attributes from client
        if(isActionNeedFileUpload(action, requestContent)){
            try {
                ContactUploadHelper uploadHelper = new ContactUploadHelper();
                List<FileItem> fileItems = uploadHelper.getUploadFileItems(request, getServletContext());
                requestContent.insertAttribute(FILE_ITEMS_ATTRIBUTE, fileItems);
            } catch (ServletException se){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                AppLogger.error(se.getMessage());
                return;
            }
        }

        // handle request according type of action
        String pagePath = action.execute(requestContent);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pagePath);
        dispatcher.forward(request, response);
    }

    private boolean isActionNeedFileUpload(ContactAction action, RequestContent requestContent){
        boolean isActionNeedFileUpload = false;
        if(action instanceof CreateContactAction ||
           action instanceof UpdateContactAction){
            isActionNeedFileUpload = true;
        }
        String submitParam = requestContent.getParameter(SUBMIT_PARAM);
        if(submitParam != null) {
            if (!Boolean.valueOf(submitParam)) {
                isActionNeedFileUpload = false;
            }
        }
        return isActionNeedFileUpload;
    }
}
