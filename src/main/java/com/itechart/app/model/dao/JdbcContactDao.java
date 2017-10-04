package com.itechart.app.model.dao;

import com.itechart.app.model.dao.utils.JdbcContactDaoHelper;
import com.itechart.app.model.dao.utils.SearchTemplateEngine;
import com.itechart.app.model.entities.*;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * provides interface to access database with contacts using JDBC
 */
public class JdbcContactDao implements ContactDao {

    private final Logger logger = LoggerFactory.getLogger(JdbcContactDao.class);

    private final static Logger staticLogger = LoggerFactory.getLogger(JdbcContactDao.class);

    private Connection connection;

    private Savepoint initialSavePoint;

    private JdbcContactDaoHelper jdbcHelper;

    private JdbcContactDao(Connection connection){
        this.connection = connection;
        jdbcHelper = new JdbcContactDaoHelper(this.connection);
    }

    @Override
    public void initializeDao() throws ContactDaoException{
        try{
            connection.setAutoCommit(false);
            this.initialSavePoint = connection.setSavepoint();
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            closeConnection(connection);
            throw new ContactDaoException(sqle);
        }
    }

    /**
     * incapsulates creating of new JdbcContactDao instances
     * @return JdbcContactDao instances
     * @throws SQLException when there is no available Connection
     * or DatabaseConnectionManager
     */
    public static JdbcContactDao newInstance() throws ContactDaoException {
        DatabaseConnectionManager manager = DatabaseConnectionManager.getInstance();
        Connection connection;
        try {
            if (manager == null) {
                throw new ContactDaoException("Cannot get DatabaseConnectionManager object");
            }
            connection = manager.getConnection();
            if (connection == null) {
                throw new ContactDaoException("Cannot get Connection object from DatabaseConnectionManager object");
            }
        } catch (SQLException sqle){
            staticLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return new JdbcContactDao(connection);
    }

    @Override
    public Contact createContact(Contact contact) throws ContactDaoException{
        try {
            // get correspond ids of contact properties
            int companyId = jdbcHelper.getContactCompanyId(contact);
            int nationalityId = jdbcHelper.getContactNationalityId(contact);
            int photoId = jdbcHelper.addContactPhoto(contact);
            contact.getPhoto().setPhotoId(photoId);

            // add contact properties to database
            int contactId = addContactToDatabase(contact, companyId, nationalityId, photoId);
            contact.setContactId(contactId);

            // add attachments of contact to database
            List<Attachment> attachments = contact.getAttachments();
            for(Attachment attachment : attachments){
                int attachmentId = addAttachmentToContact(contactId, attachment);
                attachment.setAttachmentId(attachmentId);
            }

            // add phones of contact to database
            List<Phone> phones = contact.getPhones();
            for(Phone phone : phones){
                int phoneId = addPhoneToContact(contactId, phone);
                phone.setPhoneId(phoneId);
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return contact;
    }

    @Override
    public List<Integer> getContactAttachmentIds(int contactId) throws ContactDaoException {
        final String getContactAttachmentIdsSqlQuery =
                "SELECT id_attachment \n" +
                        "FROM attachments \n" +
                        "WHERE id_contact = ?";

        return jdbcHelper.getContactDataIdsTemplate(getContactAttachmentIdsSqlQuery, contactId);
    }

    @Override
    public List<Integer> getContactPhonesIds(int contactId) throws ContactDaoException {
        final String getContactPhoneIdsSqlQuery =
                "SELECT id_phone \n" +
                        "FROM phones \n" +
                        "WHERE id_contact = ?";

        return jdbcHelper.getContactDataIdsTemplate(getContactPhoneIdsSqlQuery, contactId);
    }

    /**
     * @return id of created contact
     * @throws SQLException if creating of new contact is failed
     */
    private int addContactToDatabase(final Contact contact, final int companyId,
                                     final int nationalityId, final int photoId) throws SQLException {
        int contactId;
        PreparedStatement addContactStmt = null;
        final String addContactSqlQuery =
                "INSERT INTO contacts(first_name, surname, patronymic, birthday, \n" +
                "website, email, country, city, address, index_number, gender, marital_status, id_nationality, \n" +
                "id_company, id_photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            addContactStmt = connection.prepareStatement(addContactSqlQuery,
                    Statement.RETURN_GENERATED_KEYS);

            addContactStmt.setString(1, contact.getFirstName());
            addContactStmt.setString(2, contact.getSurname());
            addContactStmt.setString(3, contact.getPatronymic());
            addContactStmt.setDate(4, java.sql.Date.valueOf(contact.getBirthday()));
            addContactStmt.setString(5, contact.getWebsite());
            addContactStmt.setString(6, contact.getEmail());
            addContactStmt.setString(7, contact.getCountry());
            addContactStmt.setString(8, contact.getCity());
            addContactStmt.setString(9, contact.getAddress());
            addContactStmt.setString(10, contact.getIndexNumber());
            addContactStmt.setString(11, contact.getGender());
            addContactStmt.setString(12, contact.getMaritalStatus());
            addContactStmt.setInt(13, nationalityId);
            addContactStmt.setInt(14, companyId);
            addContactStmt.setInt(15, photoId);

            int affectedRows = addContactStmt.executeUpdate();
            if (affectedRows == 0) {
                addContactStmt.close();
                throw new SQLException("Creating of new contact is failed, no rows affected");
            }

            ResultSet generatedKeys = addContactStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                contactId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating of new contact is failed, no ID (id_contact) obtained ");
            }
        } finally {
            closeStatement(addContactStmt);
        }

        return contactId;
    }

    @Override
    public int updateContact(final int contactId, final Contact contact) throws ContactDaoException {
        int updatedRows = 0;
        try {
            updatedRows += jdbcHelper.updateContactInfo(contactId, contact);

            // add new attachments and update existed
            List<Attachment> attachments = contact.getAttachments();
            for(Attachment attachment : attachments){
                if(attachment.getAttachmentId() == 0){
                    addAttachmentToContact(contactId, attachment);
                } else {
                    updateAttachmentFromContact(attachment.getAttachmentId(), attachment);
                }
            }

            // add new phones and update existed
            List<Phone> phones = contact.getPhones();
            for(Phone phone : phones){
                if(phone.getPhoneId() == 0){
                    addPhoneToContact(contactId, phone);
                } else {
                    updatePhoneFromContact(phone.getPhoneId(), phone);
                }
            }

        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return updatedRows;
    }

    @Override
    public int deleteContact(final int contactId) throws ContactDaoException {
        int deletedRows = 0;
        try {
            deletedRows += jdbcHelper.deleteContactPhones(contactId);
            deletedRows += jdbcHelper.deleteContactAttachments(contactId);
            // before delete contactInfo get photoId
            int photoId = jdbcHelper.getContactPhotoId(contactId);

            deletedRows += jdbcHelper.deleteContactInfo(contactId);
            deletedRows += jdbcHelper.deleteContactPhoto(photoId);
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return deletedRows;
    }

    @Override
    public Contact getContact(final int contactId) throws ContactDaoException {
        Contact contact;
        try {
            contact = jdbcHelper.getContactInfo(contactId);
            contact.setAttachments(jdbcHelper.getContactAttachments(contactId));
            contact.setPhones(jdbcHelper.getContactPhones(contactId));
        }catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return contact;
    }

    @Override
    public List<Contact> getContacts(final int recordOffset, final int recordCount) throws ContactDaoException {
        List<Contact> contacts;
        PreparedStatement getContactsStmt = null;
        final String getContactsSqlQuery =
                "SELECT c.id_contact, c.first_name, c.surname, c.patronymic, \n" +
                        "c.country, c.city, c.address, c.index_number, comp.company, c.birthday \n" +
                        "FROM contacts c \n" +
                        "INNER JOIN companies comp ON c.id_company = comp.id_company \n" +
                        "LIMIT ?, ?";
        try {
            getContactsStmt = connection.prepareStatement(getContactsSqlQuery);
            getContactsStmt.setInt(1, recordOffset);
            getContactsStmt.setInt(2, recordCount);

            ResultSet resultSet = getContactsStmt.executeQuery();
            contacts = new ArrayList<>(recordCount);
            while(resultSet.next()){
                Contact contact = jdbcHelper.mapParamFromResultSetToContact(resultSet);
                contacts.add(contact);
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(getContactsStmt);
        }
        return contacts;
    }

    @Override
    public int getContactCount() throws ContactDaoException{
        int contactCount = 0;
        PreparedStatement getContactCountStmt = null;

        final String getContactCountSqlQuery = "SELECT COUNT(id_contact) AS contact_count FROM contacts";

        try{
            getContactCountStmt = connection.prepareStatement(getContactCountSqlQuery);
            ResultSet resultSet = getContactCountStmt.executeQuery(getContactCountSqlQuery);

            if(resultSet.next()){
                contactCount = resultSet.getInt(1);
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(getContactCountStmt);
        }
        return contactCount;
    }

    @Override
    public int addAttachmentToContact(final int contactId, final Attachment attachment) throws ContactDaoException {
        int generatedAttachmentId;

        PreparedStatement addAttachmentStmt = null;
        try {
            final String addAttachmentSqlQuery =
                    "INSERT INTO attachments (id_contact, name, \n" +
                            "download_date, commentary, attachment) VALUES \n" +
                            "(?, ?, ?, ?, ?) ";

            addAttachmentStmt = connection.prepareStatement(addAttachmentSqlQuery,
                    Statement.RETURN_GENERATED_KEYS);

            addAttachmentStmt.setInt(1, contactId);
            addAttachmentStmt.setString(2, attachment.getFileName());
            addAttachmentStmt.setDate(3,  java.sql.Date.valueOf(attachment.getDownloadDate()));
            addAttachmentStmt.setString(4, attachment.getComment());
            addAttachmentStmt.setBinaryStream(5, attachment.getFileStream(), attachment.getFileSize());

            int affectedRows = addAttachmentStmt.executeUpdate();
            if(affectedRows == 0){
                throw new SQLException("Creating of new attachment is failed, no rows affected");
            }

            ResultSet generatedKeys = addAttachmentStmt.getGeneratedKeys();
            if(generatedKeys.next()){
                generatedAttachmentId = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating of new attachment is failed, no ID obtained ");
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            closeStatement(addAttachmentStmt);
            throw new ContactDaoException(sqle);
        }
        return generatedAttachmentId;
    }

    @Override
    public int updateAttachmentFromContact(final int attachmentId, final Attachment attachment)
            throws ContactDaoException {
        int rowsUpdated;
        if(attachment.getFileStream() == null){
            rowsUpdated = jdbcHelper.updateOnlyAttachmentFields(attachmentId, attachment);
        } else {
            rowsUpdated = jdbcHelper.updateFullAttachment(attachmentId, attachment);
        }
        return rowsUpdated;
    }

    @Override
    public int deleteAttachmentFromContact(final int attachmentId) throws ContactDaoException {
        int rowsDeleted;
        final String deleteAttachmentSqlQuery = "DELETE FROM attachments WHERE id_attachment = ?";
        try {
            rowsDeleted = jdbcHelper.deleteRowsByIdTemplate(deleteAttachmentSqlQuery, attachmentId);
        }catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return rowsDeleted;
    }

    @Override
    public int addPhoneToContact(final int contactId, final Phone phone) throws ContactDaoException {
        int generatedPhoneId;

        PreparedStatement addPhoneStmt = null;
        try {
            final String addPhoneSqlQuery =
                    "INSERT INTO phones(id_contact, phone_number, \n" +
                            "phone_type, commentary) VALUES \n" +
                            "(?, ?, ?, ?) ";

            addPhoneStmt = connection.prepareStatement(addPhoneSqlQuery,
                    Statement.RETURN_GENERATED_KEYS);

            addPhoneStmt.setInt(1, contactId);
            addPhoneStmt.setString(2, phone.getPhoneNumber());
            addPhoneStmt.setString(3, phone.getPhoneType() );
            addPhoneStmt.setString(4, phone.getComment());

            int affectedRows = addPhoneStmt.executeUpdate();
            if(affectedRows == 0){
                throw new SQLException("Creating of new phone is failed, no rows affected");
            }

            ResultSet generatedKeys = addPhoneStmt.getGeneratedKeys();
            if(generatedKeys.next()){
                generatedPhoneId = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating of new phone is failed, no ID obtained ");
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            closeStatement(addPhoneStmt);
            throw new ContactDaoException(sqle);
        }
        return generatedPhoneId;
    }

    @Override
    public int updatePhoneFromContact(final int phoneId, Phone phone) throws ContactDaoException {
        int rowsUpdated;
        PreparedStatement updatePhoneStmt = null;

        final String updatePhoneSqlQuery =
                "UPDATE phones \n" +
                        "SET phone_number = ?, phone_type = ?, commentary = ? \n" +
                        "WHERE id_phone = ?";
        try {
            updatePhoneStmt = connection.prepareStatement(updatePhoneSqlQuery);

            updatePhoneStmt.setString(1, phone.getPhoneNumber());
            updatePhoneStmt.setString(2, phone.getPhoneType());
            updatePhoneStmt.setString(3, phone.getComment());
            updatePhoneStmt.setInt(4, phoneId);

            rowsUpdated = updatePhoneStmt.executeUpdate();
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(updatePhoneStmt);
        }
        return rowsUpdated;
    }

    @Override
    public int deletePhoneFromContact(final int phoneId) throws ContactDaoException{
        int rowsDeleted;
        final String deletePhoneSqlQuery = "DELETE FROM phones WHERE id_phone = ?";
        try {
            rowsDeleted = jdbcHelper.deleteRowsByIdTemplate(deletePhoneSqlQuery, phoneId);
        }catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return rowsDeleted;
    }

    @Override
    public List<String> getEmailsByIds(int[] contactIds) throws ContactDaoException {
        List<String> emailList = new ArrayList<>();
        final String getEmailByIdSqlQuery =
                "SELECT email FROM contacts WHERE id_contact = ?";

        for(int contactId : contactIds){
            PreparedStatement getEmailByIdStmt = null;
            try {
                getEmailByIdStmt = connection.prepareStatement(getEmailByIdSqlQuery);
                getEmailByIdStmt.setInt(1, contactId);

                ResultSet resultSet = getEmailByIdStmt.executeQuery();
                if(resultSet.next()){
                    String email = resultSet.getString(1);
                    emailList.add(email);
                }
            } catch (SQLException sqle){
                logger.error(sqle.getMessage());
                throw new ContactDaoException(sqle);
            } finally {
                closeStatement(getEmailByIdStmt);
            }
        }
        return emailList;
    }

    @Override
    public List<Contact> findContacts(SearchContactDetails searchDetails) throws ContactDaoException {
        List<Contact> contactList;
        final int searchLimitOffset = 0;
        final int searchLimitCount = 20;

        final String findContactsSqlQuery =
                "SELECT c.id_contact, c.first_name, c.surname, c.patronymic, \n" +
                        "c.country, c.city, c.address, c.index_number, comp.company, c.birthday \n" +
                        "FROM contacts c\n" +
                        "INNER JOIN companies comp ON c.id_company = comp.id_company \n" +
                        "WHERE c.first_name COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.surname COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.patronymic COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.gender COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.marital_status COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.email COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.country COLLATE UTF8_GENERAL_CI LIKE ? AND\n" +
                        "    c.city COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.address COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.index_number COLLATE UTF8_GENERAL_CI LIKE ? AND \n" +
                        "    c.`birthday` > ? AND \n" +
                        "    c.`birthday` < ? " +
                        "LIMIT ?, ?";


        PreparedStatement findContactsStmt = null;
        try{
            findContactsStmt = connection.prepareStatement(findContactsSqlQuery);
            findContactsStmt.setString(1,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getFirstName()));
            findContactsStmt.setString(2,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getSurname()));
            findContactsStmt.setString(3,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getPatronymic()));
            findContactsStmt.setString(4,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getGender()));
            findContactsStmt.setString(5,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getMaritalStatus()));
            findContactsStmt.setString(6,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getEmail()));
            findContactsStmt.setString(7,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getCountry()));
            findContactsStmt.setString(8,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getCity()));
            findContactsStmt.setString(9,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getAddress()));
            findContactsStmt.setString(10,
                    SearchTemplateEngine.generateSearchTemplate(searchDetails.getIndexNumber()));
            findContactsStmt.setDate(11, java.sql.Date.valueOf(searchDetails.getBirthdayFrom()));
            findContactsStmt.setDate(12, java.sql.Date.valueOf(searchDetails.getBirthdayTo()));
            findContactsStmt.setInt(13, searchLimitOffset);
            findContactsStmt.setInt(14, searchLimitCount);

            ResultSet resultSet = findContactsStmt.executeQuery();
            contactList = new ArrayList<>();
            while(resultSet.next()){
                Contact contact = jdbcHelper.mapParamFromResultSetToContact(resultSet);
                contactList.add(contact);
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(findContactsStmt);
        }
        return contactList;
    }

    @Override
    public List<Contact> getContactsByBirthday(java.util.Date today) throws ContactDaoException {
        List<Contact> contactList;
        final String getContactsByBirthdaySqlQuery =
                "SELECT first_name, surname, email \n" +
                "FROM contacts \n" +
                "WHERE DATE_FORMAT(birthday, '%m-%d') = DATE_FORMAT(?, '%m-%d')";

        PreparedStatement getContactsByBirthdayStmt = null;
        try{
            getContactsByBirthdayStmt = connection.prepareStatement(getContactsByBirthdaySqlQuery);
            getContactsByBirthdayStmt.setDate(1, new java.sql.Date(today.getTime()));

            ResultSet resultSet = getContactsByBirthdayStmt.executeQuery();
            contactList = new ArrayList<>();
            while(resultSet.next()){
                Contact contact = new Contact();
                contact.setFirstName(resultSet.getString(1));
                contact.setSurname(resultSet.getString(2));
                contact.setEmail(resultSet.getString(3));
                contactList.add(contact);
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(getContactsByBirthdayStmt);
        }
        return contactList;
    }

    /**
     * As JdbcContactDao instance uses connection from connection pool
     * @see DatabaseConnectionManager, it is important to release
     * used connection
     */
    public void closeConnection(){
        try {
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public void closeDao(boolean wasDaoActionsSuccessful) throws ContactDaoException {
        try{
            if(wasDaoActionsSuccessful){
                if(connection != null) {
                    connection.commit();
                }
            } else {
                if(connection != null){
                    connection.rollback(initialSavePoint);
                }
            }
            closeConnection();
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            closeConnection(connection);
            throw new ContactDaoException(sqle);
        }
    }

    @Override
    public void closeDao() throws ContactDaoException {
        closeConnection();
    }

    private void closeConnection(Connection connection) throws ContactDaoException{
        if(connection != null){
            try{
                connection.close();
            } catch (SQLException sqlex){
                logger.error(sqlex.getMessage());
                throw new ContactDaoException(sqlex);
            }
        }
    }

    private void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
        }
    }
}
