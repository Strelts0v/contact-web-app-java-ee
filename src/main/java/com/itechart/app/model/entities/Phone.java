package com.itechart.app.model.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Phone {

    private long phoneId;

    private String phoneNumber;

    private String description;

    private String comment;

    public final static Phone EMPTY_PHONE;

    static {
        EMPTY_PHONE = new Phone();
    }

    public long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(long phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        return new EqualsBuilder()
                .append(phoneId, phone.phoneId)
                .append(phoneNumber, phone.phoneNumber)
                .append(description, phone.description)
                .append(comment, phone.comment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(phoneId)
                .append(phoneNumber)
                .append(description)
                .append(comment)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("phoneId", phoneId)
                .append("phoneNumber", phoneNumber)
                .append("description", description)
                .append("comment", comment)
                .toString();
    }
}
