package com.itechart.app.model.dao;

import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.Phone;
import com.itechart.app.model.exceptions.ContactDaoException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class JdbcContactDaoTest {

    private Contact testContact;

    public JdbcContactDaoTest(){
        testContact = new Contact();
        testContact.setSurname("testSurname");
        testContact.setFirstName("testFirstName");
        testContact.setPatronymic("testPatronymic");
        testContact.setBirthday("2010-04-07");
        testContact.setGender("Female");
        testContact.setNationality("testNationality");
        testContact.setMaritalStatus("Single");
        testContact.setWebsite("testWebsite");
        testContact.setEmail("testEmail");
        testContact.setCompany("testCompany");
        testContact.setCountry("testCountry");
        testContact.setCity("testCity");
        testContact.setAddress("testStreet");
        testContact.setIndexNumber("testIndex");
    }

    @Test
    public void getJdbcContactDaoInstanceTest() throws Exception {
        JdbcContactDao dao = JdbcContactDao.newInstance();
        final String errorMessage = "Expected not null JdbcContactDao instance";

        Assert.assertNotNull(errorMessage, dao);
    }

    @Test
    public void createContactShouldReturnValidIdOfCreatedContactTest() throws Exception {
        ContactDao dao = JdbcContactDao.newInstance();
        try {
            dao.initializeDao();
            Contact resultContact = dao.createContact(testContact);
            final int resultId = resultContact.getContactId();

            final String errorMessage = "Expected valid contact id after creating new contact";
            Assert.assertTrue(errorMessage, isIdValid(resultId));
            // delete contact after successful test
            dao.deleteContact(resultId);
            dao.closeDao(true);
        } catch (ContactDaoException cde){
            dao.closeDao(false);
            throw new Exception();
        }
    }

    private boolean isIdValid(long id){
        boolean isValid = true;
        if(id <= 0){
            isValid = false;
        }
        return isValid;
    }

    @Test
    public void updateContactShouldReturnValidCountOfUpdateRowsTest() throws Exception {
        Contact contact = new Contact();
        contact.setSurname("testSurnameX");
        contact.setFirstName("testFirstNameX");
        contact.setPatronymic("testPatronymicX");
        contact.setBirthday("2010-04-07");
        contact.setGender("Female");
        contact.setNationality("testNationalityX");
        contact.setMaritalStatus("Single");
        contact.setWebsite("testWebsiteX");
        contact.setEmail("testEmail@gmail.com");
        contact.setCompany("testCompanyX");
        contact.setCountry("testCountryX");
        contact.setCity("testCityX");
        contact.setAddress("testStreetX");
        contact.setIndexNumber("testIndexX");

        ContactDao dao = JdbcContactDao.newInstance();
        try {
            dao.initializeDao();

            // before update contact we need to create one
            Contact resultContact = dao.createContact(testContact);
            final int contactId = resultContact.getContactId();

            final int expectedUpdateRowsCount = 1;
            final int updateRowsCount = dao.updateContact(contactId, contact);
            final String errorMessage
                    = "Expected and actual count of rows after updating contact are different";
            Assert.assertTrue(errorMessage, expectedUpdateRowsCount <= updateRowsCount);
            // delete contact after successful test
            dao.deleteContact(contactId);
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }

    @Test
    public void deleteContactShouldReturnAppropriateCountOfDeletedRowsTest() throws Exception{
        ContactDao dao = JdbcContactDao.newInstance();
        try {
            dao.initializeDao();

            // before deleting contact we need to create one
            Contact resultContact = dao.createContact(testContact);
            final int contactId = resultContact.getContactId();

            final int expectedCountOfDeletedRows = 2;
            final String errorMessage
                    = "Expected and actual count of rows after deleting contact are different";
            final int deleteRowsCount = dao.deleteContact(contactId);
            Assert.assertEquals(errorMessage, expectedCountOfDeletedRows, deleteRowsCount);
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }

    @Test
    public void getContactShouldReturnCorrespondContactObjectTest() throws Exception{
        ContactDao dao = JdbcContactDao.newInstance();
        try {
            dao.initializeDao();

            // before deleting contact we need to create one
            Contact contactAfterCreate = dao.createContact(testContact);
            final int contactId = contactAfterCreate.getContactId();

            Contact resultContact = dao.getContact(contactId);
            final String errorMessage = "Expected and actual contact objects are different";
            Assert.assertEquals(errorMessage, testContact, resultContact);

            // delete contact after successful test
            dao.deleteContact(contactId);
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }

    @Test
    public void getContactsShouldReturnAppropriateCountOfContactsTest() throws Exception{
        JdbcContactDao dao = JdbcContactDao.newInstance();
        final int offset = 0;
        final int recordCount = 3;
        try {
            dao.initializeDao();

            // before deleting contact we need to create one
            int[] createdContactIds = new int[recordCount];
            for(int i = 0; i < recordCount; i++){
                createdContactIds[i] = dao.createContact(testContact).getContactId();
            }

            List<Contact> contacts = dao.getContacts(offset, recordCount);

            final String errorMessage = "Expected and actual count of returned contacts are different";
            Assert.assertEquals(errorMessage, recordCount , contacts.size());
            // delete contacts after successful test
            for (int createdContactId : createdContactIds) {
                dao.deleteContact(createdContactId);
            }
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }

    @Test
    public void updateAttachmentShouldReturnValidCountOfUpdateRowsTest() throws Exception {
        ContactDao dao = JdbcContactDao.newInstance();
        try{
            dao.initializeDao();

            Attachment attachment = new Attachment();
            attachment.setFileName("v");
            attachment.setDownloadDate("2015-07-27");
            attachment.setComment("v");

            // first create contact and attachment to update it
            final int contactId = dao.createContact(testContact).getContactId();
            final int attachmentId = dao.addAttachmentToContact(contactId, attachment);

            attachment.setFileName("x");
            attachment.setComment("x");
            final int resultUpdatedRows = dao.updateAttachmentFromContact(attachmentId, attachment);
            final int expectedUpdatedRows = 1;

            final String errorMessage = "Expected and actual count of updated rows are different";
            Assert.assertEquals(errorMessage, expectedUpdatedRows, resultUpdatedRows);
            // delete contacts after successful test
            dao.deleteContact(contactId);
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }

    @Test
    public void deleteAttachmentShouldReturnValidCountOfDeletedRowsTest() throws Exception {
        ContactDao dao = JdbcContactDao.newInstance();
        try{
            dao.initializeDao();

            Attachment attachment = new Attachment();
            attachment.setFileName("v");
            attachment.setDownloadDate("2015-07-27");
            attachment.setComment("v");

            // first create contact and attachment to update it
            final int contactId = dao.createContact(testContact).getContactId();
            final int attachmentId = dao.addAttachmentToContact(contactId, attachment);

            // delete attachment
            final int resultDeletedRows = dao.deleteAttachmentFromContact(attachmentId);
            final int expectedDeletedRows = 1;

            final String errorMessage = "Expected and actual count of deleted rows are different";
            Assert.assertEquals(errorMessage, resultDeletedRows, expectedDeletedRows);
            // delete contacts after successful test
            dao.deleteContact(contactId);
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }

    @Test
    public void updatePhoneShouldReturnValidCountOfUpdateRowsTest() throws Exception {
        ContactDao dao = JdbcContactDao.newInstance();
        try{
            dao.initializeDao();

            Phone phone = new Phone();
            phone.setPhoneNumber("+375291234567");
            phone.setPhoneType("Mobile");
            phone.setComment("comment");

            // first create contact and attachment to update it
            final int contactId = dao.createContact(testContact).getContactId();
            final int phoneId = dao.addPhoneToContact(contactId, phone);

            phone.setPhoneNumber("+375447654321");
            final int resultUpdatedRows = dao.updatePhoneFromContact(phoneId, phone);
            final int expectedUpdatedRows = 1;

            final String errorMessage = "Expected and actual count of updated rows are different";
            Assert.assertEquals(errorMessage, expectedUpdatedRows, resultUpdatedRows);
            // delete contacts after successful test
            dao.deleteContact(contactId);
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }

    @Test
    public void deletePhoneShouldReturnValidCountOfDeletedRowsTest() throws Exception {
        ContactDao dao = JdbcContactDao.newInstance();
        try{
            dao.initializeDao();

            Phone phone = new Phone();
            phone.setPhoneNumber("+375291234567");
            phone.setPhoneType("Mobile");
            phone.setComment("comment");

            // first create contact and attachment to update it
            final int contactId = dao.createContact(testContact).getContactId();
            final int phoneId = dao.addPhoneToContact(contactId, phone);

            final int resultDeletedRows = dao.deletePhoneFromContact(phoneId);
            final int expectedDeletedRows = 1;

            final String errorMessage = "Expected and actual count of updated rows are different";
            Assert.assertEquals(errorMessage, expectedDeletedRows, resultDeletedRows);
            // delete contacts after successful test
            dao.deleteContact(contactId);
            dao.closeDao(true);
        } catch (ContactDaoException cde) {
            dao.closeDao(false);
            throw new Exception();
        }
    }
}