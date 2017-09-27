package com.itechart.app.model.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    private int contactId;

    private String surname;

    private String firstName;

    private String patronymic;

    private String birthday;

    private String gender;

    private String nationality;

    private String maritalStatus;

    private String website;

    private String email;

    private String country;

    private String city;

    private String address;

    private String indexNumber;

    private String company;

    private Photo photo;

    private List<Phone> phones;

    private List<Attachment> attachments;

    public final static Contact EMPTY_CONTACT;

    static {
        List<Attachment> attachments = new ArrayList<>();
        attachments.add(Attachment.EMPTY_ATTACHMENT);

        List<Phone> phones = new ArrayList<>();
        phones.add(Phone.EMPTY_PHONE);

        EMPTY_CONTACT = new Contact(
                attachments,
                phones,
                Photo.EMPTY_PHOTO
        );
    }

    /**
     * default public constructor
     */
    public Contact(){
        this.attachments = new ArrayList<>();
        this.phones = new ArrayList<>();
        this.photo = Photo.EMPTY_PHOTO;
    }

    /**
     * private constructor to initialize static EMPTY_CONTACT object
     */
    private Contact(List<Attachment> attachments, List<Phone> phones, Photo photo){
        this.attachments = attachments;
        this.phones = phones;
        this.photo = photo;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public List<Phone> getPhones() {
        return phones == null ? EMPTY_CONTACT.getPhones() : phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Attachment> getAttachments() {
        return attachments == null ? EMPTY_CONTACT.getAttachments() : attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return new EqualsBuilder()
                .append(contactId, contact.contactId)
                .append(surname, contact.surname)
                .append(firstName, contact.firstName)
                .append(patronymic, contact.patronymic)
                .append(birthday, contact.birthday)
                .append(gender, contact.gender)
                .append(nationality, contact.nationality)
                .append(maritalStatus, contact.maritalStatus)
                .append(website, contact.website)
                .append(email, contact.email)
                .append(country, contact.country)
                .append(city, contact.city)
                .append(address, contact.address)
                .append(indexNumber, contact.indexNumber)
                .append(company, contact.company)
                .append(photo, contact.photo)
                .append(phones, contact.phones)
                .append(attachments, contact.attachments)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(contactId)
                .append(surname)
                .append(firstName)
                .append(patronymic)
                .append(birthday)
                .append(gender)
                .append(nationality)
                .append(maritalStatus)
                .append(website)
                .append(email)
                .append(country)
                .append(city)
                .append(address)
                .append(indexNumber)
                .append(company)
                .append(photo)
                .append(phones)
                .append(attachments)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("contactId", contactId)
                .append("surname", surname)
                .append("firstName", firstName)
                .append("patronymic", patronymic)
                .append("birthday", birthday)
                .append("gender", gender)
                .append("nationality", nationality)
                .append("maritalStatus", maritalStatus)
                .append("website", website)
                .append("email", email)
                .append("country", country)
                .append("city", city)
                .append("address", address)
                .append("indexNumber", indexNumber)
                .append("company", company)
                .append("photo", photo)
                .append("phones", phones)
                .append("attachments", attachments)
                .toString();
    }
}
