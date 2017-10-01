package com.itechart.app.model.utils;

import com.itechart.app.model.entities.Attachment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AttachmentSorterTest {

    private Map<String, Attachment> attachmentMap;

    {
        final int attachmentListLength = 10;
        attachmentMap = new HashMap<>(attachmentListLength);
        for (int i = 0; i < attachmentListLength; i++) {
            if (i % 2 == 0) {
                attachmentMap.put(String.valueOf(i) , new Attachment());
            } else {
                Attachment attachment = new Attachment();
                attachment.setAttachmentId(1);
                attachmentMap.put(String.valueOf(i), attachment);
            }
        }
    }

    private AttachmentSorter attachmentSorter;

    @Before
    public void initAttachmentSorter(){
        attachmentSorter = new AttachmentSorter(attachmentMap);
    }

    @Test
    public void getOldAttachmentsShouldReturnExpectedAttachmentCount() throws Exception {
        final int expectedAttachmentCount = 5;
        final String errorMessage =
                "Expected and actual count of returned old attachment objects are different";
        Assert.assertEquals(errorMessage, expectedAttachmentCount,
                attachmentSorter.getOldAttachments().size());
    }

    @Test
    public void  getNewAttachmentsShouldReturnExpectedAttachmentCount() throws Exception {
        final int expectedAttachmentCount = 5;
        final String errorMessage =
                "Expected and actual count of returned new attachment objects are different";
        Assert.assertEquals(errorMessage, expectedAttachmentCount,
                attachmentSorter.getNewAttachments().size());
    }
}