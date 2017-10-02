package com.itechart.app.model.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchContactDetails {

    private String surname;

    private String firstName;

    private String patronymic;

    private String birthdayFrom;

    private String birthdayTo;

    private String gender;

    private String nationality;

    private String maritalStatus;

    private String email;

    private String country;

    private String city;

    private String address;

    private String indexNumber;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthdayFrom() {
        return birthdayFrom;
    }

    public void setBirthdayFrom(String birthdayFrom) {
        this.birthdayFrom = birthdayFrom;
    }

    public String getBirthdayTo() {
        return birthdayTo;
    }

    public void setBirthdayTo(String birthdayTo) {
        this.birthdayTo = birthdayTo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SearchContactDetails that = (SearchContactDetails) o;

        return new EqualsBuilder()
                .append(surname, that.surname)
                .append(firstName, that.firstName)
                .append(patronymic, that.patronymic)
                .append(birthdayFrom, that.birthdayFrom)
                .append(birthdayTo, that.birthdayTo)
                .append(gender, that.gender)
                .append(nationality, that.nationality)
                .append(maritalStatus, that.maritalStatus)
                .append(email, that.email)
                .append(country, that.country)
                .append(city, that.city)
                .append(address, that.address)
                .append(indexNumber, that.indexNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(surname)
                .append(firstName)
                .append(patronymic)
                .append(birthdayFrom)
                .append(birthdayTo)
                .append(gender)
                .append(nationality)
                .append(maritalStatus)
                .append(email)
                .append(country)
                .append(city)
                .append(address)
                .append(indexNumber)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("surname", surname)
                .append("firstName", firstName)
                .append("patronymic", patronymic)
                .append("birthdayFrom", birthdayFrom)
                .append("birthdayTo", birthdayTo)
                .append("gender", gender)
                .append("nationality", nationality)
                .append("maritalStatus", maritalStatus)
                .append("email", email)
                .append("country", country)
                .append("city", city)
                .append("address", address)
                .append("indexNumber", indexNumber)
                .toString();
    }
}
