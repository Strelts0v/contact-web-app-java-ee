package com.itechart.app.model.actions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itechart.app.controller.utils.RequestContent;
import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Phone;
import com.itechart.app.model.entities.Photo;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.PageConfigurationManager;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddAttachmentToContactAction implements ContactAction{

    private static final String FILE_ITEMS_ATTRIBUTE = "fileItems";

    private static final int ATTACHMENT_PROPERTIES_COUNT = 3;

    private static final String CONTACT_ID_PARAM = "id_contact";

    private static final String ATTACHMENT_NAME_PARAM = "attachment_name";

    private static final String COMMENT_PARAM = "comment";

    private static final String PHONES_PARAM = "phones";

    private static final String ATTACHMENTS_PARAM = "attachments";

    private static final String PHOTO_PARAM = "photo";

    private final static String ERROR_PAGE_NAME = "path.page.jsp.error";

    private final static String CONTACT_DETAIL_PAGE_NAME = "path.page.jsp.contact-detail";

    private final static String CONTACT_REQUEST_ATTRIBUTE = "contact";

    public String execute(RequestContent requestContent) {
        String page = null;

        List<FileItem> items = (List<FileItem>) requestContent.getAttribute(FILE_ITEMS_ATTRIBUTE);
        Iterator fileItemIterator = items.iterator();

        Map<String, String> contactPropertiesMap = new HashMap<>(ATTACHMENT_PROPERTIES_COUNT);

        Map<String, Attachment> attachmentsMap = new HashMap<>();
        List<Phone> phones;
        Photo photo;

        while (fileItemIterator.hasNext()) {
            FileItem item = (FileItem) fileItemIterator.next();
            if (item.isFormField()) {
                if(item.getFieldName().equals(PHONES_PARAM)){
                    phones = parsePhones(item);
                } else if (item.getFieldName().equals(ATTACHMENTS_PARAM)){
                    List<Attachment> attachments = parseAttachment(item);
                    for(Attachment attachment: attachments){
                        attachmentsMap.put(attachment.getFileName(), attachment);
                    }
                } else {
                    contactPropertiesMap.put(item.getFieldName(), item.getString());
                }
            } else {
                if(item.getFieldName().equals(PHOTO_PARAM)){
                    photo = parsePhoto(item);
                }
                try {
                    Attachment attachment = attachmentsMap.get(item.getFieldName());
                    if(attachment != null) {
                        attachment.setFileStream(item.getInputStream());
                        attachment.setFileSize((int)item.getSize());
                    }
                } catch (IOException e){
                    AppLogger.error(e.getMessage());
                }
            }
        }

        int contactId = Integer.parseInt(contactPropertiesMap.get(CONTACT_ID_PARAM));

        try {
            JdbcContactDao dao = JdbcContactDao.newInstance();
            if (dao == null) {
                page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
            } else {
                page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
                // TODO
            }
        }catch (ContactDaoException cde){
            AppLogger.error(cde.getMessage());
            page = PageConfigurationManager.getPageName(ERROR_PAGE_NAME);
        } finally {
            return page;
        }
    }

    private List<Phone> parsePhones(FileItem item) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Phone>>(){}.getType();

        // here method getString() from FileItem should return JSON string
        List<Phone> phones = gson.fromJson(item.getString(), type);
        return phones;
    }

    private Photo parsePhoto(FileItem item){
        return null;
    }

    private List<Attachment> parseAttachment(FileItem item) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Attachment>>(){}.getType();

        // here method getString() from FileItem should return JSON string
        String json = item.getString();
        List<Attachment> attachments = gson.fromJson(json, type);
        return attachments;
    }
}
