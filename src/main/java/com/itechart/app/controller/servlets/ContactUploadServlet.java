package com.itechart.app.controller.servlets;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.controller.utils.RestRequest;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.actions.ContactAction;
import com.itechart.app.model.factories.ContactActionFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ContactUploadServlet extends HttpServlet {

    private static final String FILE_ITEMS_ATTRIBUTE = "fileItems";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Before you can work with the uploaded items, of course, you need
        // to parse the request itself. Ensuring that the request is actually
        // a file upload request is straightforward, but FileUpload makes it
        // simplicity itself, by providing a static method to do just that.
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            AppLogger.error("Bad request while uploading file from client");
            return;
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        // Set factory constraints
        // Max constraint - when max is run out, data is written in temp directory
        // set 10MB
        diskFileItemFactory.setSizeThreshold(1024*1024*10);

        // set temp dir
        File tempDir = (File)getServletContext().getAttribute("javax.servlet.context.tempdir");
        diskFileItemFactory.setRepository(tempDir);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);

        // Set overall request size constraint. Set to 10 MB
        upload.setSizeMax(1024 * 1024 * 10);

        // upload.setFileSizeMax - max file size
        try {
            List<FileItem> fileItems = upload.parseRequest(request);

            // Create RestRequest object to handle REST request
            RestRequest restRequest = new RestRequest(request.getPathInfo());

            // Create wrapper for HttpServletRequest for getting params from JSP
            // and inserting attributes back to JSP
            RequestContent requestContent = new RequestContent(request);
            request.setAttribute(FILE_ITEMS_ATTRIBUTE, fileItems);

            // identify command coming from JSP
            ContactActionFactory actionFactory = new ContactActionFactory();
            ContactAction action = actionFactory.defineContactAction(restRequest.getActionFromRestUrl());

            // handle request according type of action
            String pagePath = action.execute(requestContent);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pagePath);
            dispatcher.forward(request, response);


        } catch (Exception e) {
            AppLogger.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
