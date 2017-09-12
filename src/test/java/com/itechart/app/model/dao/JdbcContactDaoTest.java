package com.itechart.app.model.dao;

import com.itechart.app.model.entities.Address;
import com.itechart.app.model.entities.Contact;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class JdbcContactDaoTest {

    private Contact testContact;

    public JdbcContactDaoTest(){
        testContact = new Contact();
        testContact.setSurname("testSurname");
        testContact.setFirstName("testFirstName");
        testContact.setPatronymic("testPatronymic");
        testContact.setBirthday(new Date());
        testContact.setGender("Female");
        testContact.setNationality("testNationality");
        testContact.setMaritalStatus("Single");
        testContact.setWebsite("testWebsite");
        testContact.setEmail("testEmail");
        testContact.setCompany("testCompany");

        Address testAddress = new Address();
        testAddress.setCountry("testCountry");
        testAddress.setCity("testCity");
        testAddress.setAddress("testStreet");
        testAddress.setIndex("testIndex");

        testContact.setAddress(testAddress);
    }

    @Test
    public void getJdbcContactDaoInstanceTest() throws Exception {
        JdbcContactDao dao = JdbcContactDao.newInstance();
        final String errorMessage = "Expected not null JdbcContactDao instance";

        Assert.assertNotNull(errorMessage, dao);
    }

    @Test
    public void createContactShouldReturnValidIdOfCreatedContactTest() throws Exception {
        JdbcContactDao dao = JdbcContactDao.newInstance();

        Contact resultContact = dao.createContact(testContact);
        final long resultId = resultContact.getContactId();

        final String errorMessage = "Expected valid contact id";
        Assert.assertTrue(errorMessage, isIdValid(resultId));
    }

    private boolean isIdValid(long id){
        boolean isValid = true;
        if(id <= 0){
            isValid = false;
        }
        return isValid;
    }

}