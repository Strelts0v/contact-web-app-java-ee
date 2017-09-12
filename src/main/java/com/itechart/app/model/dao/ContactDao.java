package com.itechart.app.model.dao;

import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.Phone;

import java.util.List;

/**
 * provides interface to access database with contacts
 */
public interface ContactDao {

    /**
     * creates new contact in database
     * @param contact - object with all necessary properties
     * @return - created Contact object in database
     */
    Contact createContact(final Contact contact);

    /**
     * updates existed contact in database
     * @param contactId - unique property used to find updated contact
     * @param contact - object with new properties
     * @return - count of updated records in database
     */
    int updateContact(final int contactId, final Contact contact);

    /**
     * deletes existed contact from database
     * @param contactId - unique property used to find deleted contact
     * @return - count of deleted records in database
     */
    int deleteContact(final int contactId);

    /**
     * gets existed contact from database
     * @param contactId - unique property used to find contact
     * @return - Contact object whis is hold all contact properties
     */
    Contact getContact(final int contactId);

    /**
     * gets Contact objects from database
     * @param recordOffset - offset of all contact stored in database
     * @param recordCount - count of contacts to return
     * @return list of Contact objects
     */
    List<Contact> getContacts(final int recordOffset, final int recordCount);

    /**
     * adds attachment to contact
     * @param contactId - unique property of contact object
     * @param attachment - object that store attachment properties
     * @return count of created records in database
     */
    int addAttachmentToContact(final int contactId, final Attachment attachment);

    /**
     * updates existed attachment in database
     * @param attachmentId - unique property used to find updated attachment
     * @param attachment - object with new attachment properties
     * @return - count of updated records in database
     */
    int updateAttachmentFromContact(final int attachmentId, final Attachment attachment);

    /**
     * deletes existed attachment from database
     * @param attachmentId - unique property used to find deleted attachment
     * @return - count of deleted records in database
     */
    int deleteAttachmentFromContact(final int attachmentId);

    /**
     * adds phone to contact
     * @param contactId - unique property of contact object
     * @param phone - object that store phone properties
     * @return count of created records in database
     */
    int addPhoneToContact(final int contactId, final Phone phone);

    /**
     * updates existed phone in database
     * @param phoneId - unique property used to find updated phone
     * @param phone - object with new phone properties
     * @return - count of updated records in database
     */
    int updatePhoneFromContact(final int phoneId, final Phone phone);

    /**
     * deletes existed phone from database
     * @param phoneId - unique property used to find deleted phone
     * @return - count of deleted records in database
     */
    int deletePhoneFromContact(final int phoneId);
}
