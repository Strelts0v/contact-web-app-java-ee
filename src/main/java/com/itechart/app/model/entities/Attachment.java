package com.itechart.app.model.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.InputStream;
import java.util.Date;

public class Attachment {

    private long attachmentId;

    private String fileName;

    private Date downloadDate;

    private String comment;

    private InputStream fileStream;

    public final static Attachment EMPTY_ATTACHMENT;

    static{
        EMPTY_ATTACHMENT = new Attachment();
    }

    public long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        return new EqualsBuilder()
                .append(attachmentId, that.attachmentId)
                .append(fileName, that.fileName)
                .append(downloadDate, that.downloadDate)
                .append(comment, that.comment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(attachmentId)
                .append(fileName)
                .append(downloadDate)
                .append(comment)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("attachmentId", attachmentId)
                .append("fileName", fileName)
                .append("downloadDate", downloadDate)
                .append("comment", comment)
                .toString();
    }
}
