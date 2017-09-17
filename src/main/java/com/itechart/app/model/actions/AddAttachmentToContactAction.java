package com.itechart.app.model.actions;

import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.util.*;

public class AddAttachmentToContactAction implements ContactAction{

    private static final String FILE_ITEMS_ATTRIBUTE = "fileItems";

    private static final int ATTACHMENT_PROPERTIES_COUNT = 3;

    private static final String CONTACT_ID_PARAM = "id_contact";

    private static final String ATTACHMENT_NAME_PARAM = "attachment_name";

    private static final String COMMENT_PARAM = "comment";

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";

    private final static String CONTACT_DETAIL_PAGE_NAME = "path.page.jsp.contact-detail";

    private final static String CONTACT_REQUEST_ATTRIBUTE = "contact";

    public String execute(RequestContent requestContent) {
        List<FileItem> items = (List<FileItem>) requestContent.getAttribute(FILE_ITEMS_ATTRIBUTE);
        Iterator fileItemIterator = items.iterator();

        Map<String, String> properties = new HashMap<>(ATTACHMENT_PROPERTIES_COUNT);
        Attachment attachment = new Attachment();
        while (fileItemIterator.hasNext()) {
            FileItem item = (FileItem) fileItemIterator.next();

            if (item.isFormField()) {
                properties.put(item.getFieldName(), item.getString());
            } else {
                try {
                    attachment.setFileStream(item.getInputStream());
                    attachment.setFileSize(item.getSize());
                } catch (IOException e){
                    AppLogger.error(e.getMessage());
                }
            }
        }
        int contactId = Integer.parseInt(properties.get(CONTACT_ID_PARAM));
        attachment.setContactId(contactId);
        attachment.setFileName(properties.get(ATTACHMENT_NAME_PARAM));
        attachment.setComment(properties.get(COMMENT_PARAM));
        attachment.setDownloadDate(new Date());

        JdbcContactDao dao = JdbcContactDao.newInstance();
        String page;
        if (dao == null) {
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
        } else {
            dao.addAttachmentToContact(contactId, attachment);
            Contact contact = dao.getContact(contactId);
            requestContent.insertAttribute(CONTACT_REQUEST_ATTRIBUTE, contact);
            page = PageConfigurationManager.getPageName(CONTACT_DETAIL_PAGE_NAME);
            dao.closeConnection();
        }
        return page;
    }

    /**
     * Upload the file to database
     * The folder should be created
     *
     * @param item
     * @throws Exception
     */
    private void processUploadedFile(FileItem item) throws Exception {
        String fieldName = item.getFieldName();
        String fileName = item.getName();
        String contentType = item.getContentType();
        boolean isInMemory = item.isInMemory();
        long sizeInBytes = item.getSize();


//        // Process a file upload
//        if (writeToFile) {
//            File uploadedFile = new File(...);
//            item.write(uploadedFile);
//        } else {
//            InputStream uploadedStream = item.getInputStream(); // ...
//            uploadedStream.close();
//        }
    }
    /**
     * Log field name and content
     * @param item
     */
    private void processFormField(FileItem item) {
        String name = item.getFieldName();
        String value = item.getString();
    }
}
