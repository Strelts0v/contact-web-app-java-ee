package com.itechart.app.model.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Phone {

    private int phoneId;

    private String phoneNumber;

    private String phoneType;

    private String comment;

    public final static Phone EMPTY_PHONE;

    static {
        EMPTY_PHONE = new Phone();
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
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
                .append(phoneType, phone.phoneType)
                .append(comment, phone.comment)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(phoneId)
                .append(phoneNumber)
                .append(phoneType)
                .append(comment)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("phoneId", phoneId)
                .append("phoneNumber", phoneNumber)
                .append("phoneType", phoneType)
                .append("comment", comment)
                .toString();
    }
}
