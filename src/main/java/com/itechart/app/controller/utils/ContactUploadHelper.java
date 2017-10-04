package com.itechart.app.controller.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

public class ContactUploadHelper {

    private final Logger logger = LoggerFactory.getLogger(ContactUploadHelper.class);

    public List<FileItem> getUploadFileItems(HttpServletRequest request,
                                             ServletContext context) throws ServletException {
        // Before you can work with the uploaded items, of course, you need
        // to parse the request itself. Ensuring that the request is actually
        // a file upload request is straightforward, but FileUpload makes it
        // simplicity itself, by providing a static method to do just that.
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            logger.error("Bad request while uploading file from client");
            throw new ServletException("Bad request while uploading file from client");
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        // Set factory constraints
        // Max constraint - when max is run out, data is written in temp directory
        // set 10MB
        diskFileItemFactory.setSizeThreshold(1024*1024*10);

        // set temp dir
        File tempDir = (File)context.getAttribute("javax.servlet.context.tempdir");
        diskFileItemFactory.setRepository(tempDir);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);

        // Set overall request size constraint. Set to 10 MB
        upload.setSizeMax(1024 * 1024 * 10);

        // upload.setFileSizeMax - max file size
        List<FileItem> fileItems;
        try {
            fileItems = upload.parseRequest(request);
        } catch (FileUploadException fue) {
            logger.error(fue.getMessage());
            throw new ServletException(fue);
        }
        return fileItems;
    }
}
