package com.itechart.app.model.dao;

import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.Phone;
import com.itechart.app.model.entities.SearchContactDetails;
import com.itechart.app.model.exceptions.ContactDaoException;

import java.util.List;

/**
 * provides interface to access database with contacts
 */
public interface ContactDao {

    /**
     * perform actions to initialize dao object
     * @throws ContactDaoException - if there is error during performing method
     */
    void initializeDao() throws ContactDaoException;

    /**
     * creates new contact in database
     * @param contact - object with all necessary properties
     * @return - created Contact object in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    Contact createContact(final Contact contact) throws ContactDaoException;

    /**
     * updates existed contact in storage
     * @param contactId - unique property used to find updated contact
     * @param contact - object with new properties
     * @return - count of updated records in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int updateContact(final int contactId, final Contact contact) throws ContactDaoException;

    /**
     * deletes existed contact from storage
     * @param contactId - unique property used to find deleted contact
     * @return - count of deleted records in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int deleteContact(final int contactId) throws ContactDaoException;

    /**
     * gets existed contact from storage
     * @param contactId - unique property used to find contact
     * @return - Contact object who's is hold all contact properties
     * @throws ContactDaoException - if there is error during performing method
     */
    Contact getContact(final int contactId) throws ContactDaoException;

    /**
     * gets Contact objects from storage
     * @param recordOffset - offset of all contact stored in storage
     * @param recordCount - count of contacts to return
     * @return list of Contact objects
     * @throws ContactDaoException - if there is error during performing method
     */
    List<Contact> getContacts(final int recordOffset, final int recordCount) throws ContactDaoException;

    /**
     * gets count of contacts in storage
     * @return count of contact objects in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int getContactCount() throws ContactDaoException;

    /**
     * adds attachment to contact
     * @param contactId - unique property of contact object
     * @param attachment - object that store attachment properties
     * @return id of created attachment in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int addAttachmentToContact(final int contactId, final Attachment attachment) throws ContactDaoException;

    /**
     * updates existed attachment in storage
     * @param attachmentId - unique property used to find updated attachment
     * @param attachment - object with new attachment properties
     * @return - count of updated records in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int updateAttachmentFromContact(final int attachmentId, final Attachment attachment) throws ContactDaoException;

    /**
     * deletes existed attachment from storage
     * @param attachmentId - unique property used to find deleted attachment
     * @return - count of deleted records in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int deleteAttachmentFromContact(final int attachmentId) throws ContactDaoException;

    /**
     * adds phone to contact
     * @param contactId - unique property of contact object
     * @param phone - object that store phone properties
     * @return count of created records in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int addPhoneToContact(final int contactId, final Phone phone) throws ContactDaoException;

    /**
     * updates existed phone in storage
     * @param phoneId - unique property used to find updated phone
     * @param phone - object with new phone properties
     * @return - count of updated records in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int updatePhoneFromContact(final int phoneId, final Phone phone) throws ContactDaoException;

    /**
     * deletes existed phone from storage
     * @param phoneId - unique property used to find deleted phone
     * @return - count of deleted records in storage
     * @throws ContactDaoException - if there is error during performing method
     */
    int deletePhoneFromContact(final int phoneId) throws ContactDaoException;

    /**
     * gets email address from database according contactIds
     * @param contactIds - ids of contacts
     * @return List of email addresses
     * @throws ContactDaoException - if there is error during performing method
     */
    List<String> getEmailsByIds(final int[] contactIds) throws ContactDaoException;

    /**
     * searches contacts according data stored in @param searchDetails
     * @param searchDetails - stores data that will be used to find contacts
     * @return List of Contact objects
     * @throws ContactDaoException - if there is error during performing method
     */
    List<Contact> findContacts(SearchContactDetails searchDetails) throws ContactDaoException;

    /**
     * gets contacts who has birthday according @param today
     * @param today - date of current day used to find contacts
     *              with birthdays on that day
     * @return List of contact objects
     * @throws ContactDaoException - if there is error during performing method
     */
    List<Contact> getContactsByBirthday(java.util.Date today) throws ContactDaoException;

    /**
     * perform actions to close dao object
     * @param wasDaoActionsSuccessful - indicated was any mistake in dao actions
     * @throws ContactDaoException - if there is error during performing method
     */
    void closeDao(boolean wasDaoActionsSuccessful) throws ContactDaoException;

    /**
     * perform actions to close dao object
     * @throws ContactDaoException - if there is error during performing method
     */
    void closeDao() throws ContactDaoException;
}
