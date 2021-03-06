package com.itechart.app.controller.servlets;

import com.itechart.app.controller.utils.ContactUploadHelper;
import com.itechart.app.controller.utils.NationalitySearcherHelper;
import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.controller.utils.RestRequest;
import com.itechart.app.model.actions.ContactAction;
import com.itechart.app.model.actions.CreateContactAction;
import com.itechart.app.model.actions.UpdateContactAction;
import com.itechart.app.model.actions.utils.ContactActionProperties;
import com.itechart.app.model.factories.ContactActionFactory;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Contact application front controller
 */
public class ContactRestFrontController extends HttpServlet{

    private final Logger logger = LoggerFactory.getLogger(ContactRestFrontController.class);

    private static final String RESOURCE_ID_ATTRIBUTE = "contactId";
    private static final String FILE_ITEMS_ATTRIBUTE = "fileItems";
    private static final String SUBMIT_PARAM = "submit";
    private static final String FIND_NATIONALITIES_REQUEST = "find_nationalities";

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

    /**
     * processed request from client according URL template
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Set encoding UTF-8
            request.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            // Create RestRequest object to handle REST request
            RestRequest restRequest = new RestRequest(request.getPathInfo());

            if(isThirdPartyRequest(restRequest)){
                NationalitySearcherHelper searcher = new NationalitySearcherHelper();
                searcher.searchNationalities(request, response);
                return;
            }

            // Create wrapper for HttpServletRequest for getting params from JSP
            // and inserting attributes back to JSP
            RequestContent requestContent = new RequestContent(request);

            // Check if the rest request contains resource id and if so
            // add this attribute to RequestContent
            if (restRequest.hasResourceId()) {
                requestContent.insertAttribute(RESOURCE_ID_ATTRIBUTE, restRequest.getResourceId());
            }

            // identify command coming from JSP
            ContactActionFactory factory = new ContactActionFactory();
            ContactAction action = factory.defineContactAction(restRequest.getActionFromRestUrl());

            // check if there is upload or create contact action it is
            // use ContactUploadHelper to upload files and other attributes from client
            if (isActionNeedFileUpload(action, requestContent)) {
                try {
                    logger.info("Initializing of ContactUploadHelper...");
                    ContactUploadHelper uploadHelper = new ContactUploadHelper();
                    List<FileItem> fileItems = uploadHelper.getUploadFileItems(request, getServletContext());
                    requestContent.insertAttribute(FILE_ITEMS_ATTRIBUTE, fileItems);
                    logger.info("ContactUploadHelper returned FileItem objects.");
                } catch (ServletException se) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(se.getMessage());
                    return;
                }
            }

            // handle request according type of action
            String pagePath = action.execute(requestContent);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pagePath);
            dispatcher.forward(request, response);
        } catch (Exception se){
            String pagePath = PageConfigurationManager
                    .getPageName(ContactActionProperties.ERROR_PAGE_NAME);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pagePath);
            dispatcher.forward(request, response);
        }
    }

    private boolean isThirdPartyRequest(RestRequest restRequest) {
        return restRequest.getActionFromRestUrl().equals(FIND_NATIONALITIES_REQUEST);
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
