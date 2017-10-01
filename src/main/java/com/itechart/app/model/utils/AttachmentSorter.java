package com.itechart.app.model.utils;

import com.itechart.app.model.entities.Attachment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttachmentSorter {

    private List<Attachment> oldAttachments;
    private List<Attachment> newAttachments;

    public AttachmentSorter(Map<String, Attachment> attachmentMap){
        oldAttachments = new ArrayList<>();
        newAttachments = new ArrayList<>();
        for(Map.Entry<String, Attachment> attachmentEntry : attachmentMap.entrySet()){
            if(attachmentEntry.getValue().getAttachmentId() == 0){
                newAttachments.add(attachmentEntry.getValue());
            } else {
                oldAttachments.add(attachmentEntry.getValue());
            }
        }
    }

    public List<Attachment> getOldAttachments(){
        return oldAttachments;
    }

    public List<Attachment> getNewAttachments(){
        return newAttachments;
    }
}
