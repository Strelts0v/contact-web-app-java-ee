package com.itechart.app.model.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.InputStream;

public class Photo {

    private int photoId;

    private InputStream photoStream;

    private int photoSize;

    public final static Photo EMPTY_PHOTO;

    static {
        EMPTY_PHOTO = new Photo();
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public InputStream getPhotoStream() {
        return photoStream;
    }

    public void setPhotoStream(InputStream photoStream) {
        this.photoStream = photoStream;
    }

    public int getPhotoSize() {
        return photoSize;
    }

    public void setPhotoSize(int photoSize) {
        this.photoSize = photoSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        return new EqualsBuilder()
                .append(photoId, photo.photoId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(photoId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("photoId", photoId)
                .toString();
    }
}
