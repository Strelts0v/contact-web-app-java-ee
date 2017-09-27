package com.itechart.app.model.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.InputStream;

public class Attachment {

    private int attachmentId;

    private int contactId;

    private String fileName;

    private String downloadDate;

    private String comment;

    private InputStream fileStream;

    private int fileSize;

    public final static Attachment EMPTY_ATTACHMENT;

    static{
        EMPTY_ATTACHMENT = new Attachment();
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        return new EqualsBuilder()
                .append(attachmentId, that.attachmentId)
                .append(contactId, that.contactId)
                .append(fileSize, that.fileSize)
                .append(fileName, that.fileName)
                .append(downloadDate, that.downloadDate)
                .append(comment, that.comment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(attachmentId)
                .append(contactId)
                .append(fileName)
                .append(downloadDate)
                .append(comment)
                .append(fileSize)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("attachmentId", attachmentId)
                .append("contactId", contactId)
                .append("fileName", fileName)
                .append("downloadDate", downloadDate)
                .append("comment", comment)
                .append("fileSize", fileSize)
                .toString();
    }
}
