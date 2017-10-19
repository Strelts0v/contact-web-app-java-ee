package com.itechart.app.model.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itechart.app.model.entities.Attachment;
import org.apache.commons.fileupload.FileItem;

import java.lang.reflect.Type;
import java.util.List;

public class AttachmentParser {

    public static List<Attachment> parseAttachments(FileItem item, int contactId) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Attachment>>(){}.getType();

        // here method getString() from FileItem should return JSON string
        String json = StringUtf8Encoder.getString(item);
        List<Attachment> attachments = gson.fromJson(json, type);

        // set contact id to each attachment object
        for(Attachment attachment : attachments){
            attachment.setContactId(contactId);
        }

        return attachments;
    }

    public static List<Attachment> parseAttachments(FileItem item) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Attachment>>(){}.getType();

        // here method getString() from FileItem should return JSON string
        String json = StringUtf8Encoder.getString(item);
        List<Attachment> attachments = gson.fromJson(json, type);

        return attachments;
    }
}
