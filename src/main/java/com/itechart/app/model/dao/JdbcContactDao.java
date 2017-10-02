package com.itechart.app.model.dao;

import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.dao.utils.SearchTemplateEngine;
import com.itechart.app.model.entities.*;
import com.itechart.app.model.exceptions.ContactDaoException;
import com.itechart.app.model.utils.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * provides interface to access database with contacts using JDBC
 */
public class JdbcContactDao implements ContactDao {

    private Connection connection;

    private Savepoint initialSavePoint;

    private JdbcContactDao(Connection connection){
        this.connection = connection;
    }

    @Override
    public void initializeDao() throws ContactDaoException{
        try{
            connection.setAutoCommit(false);
            this.initialSavePoint = connection.setSavepoint();
        } catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
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
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return new JdbcContactDao(connection);
    }

    @Override
    public Contact createContact(Contact contact) throws ContactDaoException{
        try {
            // get correspond ids of contact properties
            int companyId = getContactCompanyId(contact);
            int nationalityId = getContactNationalityId(contact);
            int photoId = getContactPhotoId(contact);
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
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return contact;
    }

    private int getContactCompanyId(final Contact contact) throws SQLException{
        // first try to find existed company with such name

        // make searching case-insensitive
        final String findExistedCompanySqlQuery =
                "SELECT id_company FROM companies "
                        + "WHERE company COLLATE UTF8_GENERAL_CI = ?";

        final String idName = "id_company";
        try {
            return findIdByValueTemplate(findExistedCompanySqlQuery, idName, contact.getCompany());
        } catch (SQLException e){
            AppLogger.info(e.getMessage());
        }

        // if not create new company in database
        final String insertCompanySqlQuery = "INSERT INTO companies(company) VALUES (?)";

        final String companyIdName = "id_company";
        final String[] values = { contact.getCompany()};

        return createNewRecordTemplate(insertCompanySqlQuery, companyIdName, values);
    }

    private int getContactNationalityId(final Contact contact) throws SQLException{
        // first try to find existed nationality with such name

        // make searching case-insensitive
        final String findExistedNationalitySqlQuery =
                "SELECT id_nationality FROM nationalities \n" +
                        "WHERE nationality COLLATE UTF8_GENERAL_CI = ?";
        final String idName = "id_nationality";

        try {
            return findIdByValueTemplate(findExistedNationalitySqlQuery, idName, contact.getNationality());
        } catch (SQLException e){
            AppLogger.info(e.getMessage());
        }

        // if not create new nationality in database
        final String insertNewNationalityQuery = "INSERT INTO nationalities(nationality) VALUES (?)";
        final String nationalityIdName = "id_nationality";
        final String[] values = { contact.getNationality()};

        return createNewRecordTemplate(insertNewNationalityQuery, nationalityIdName, values);
    }

    private int getContactPhotoId(Contact contact) throws SQLException{
        final String insertNewPhotoQuery = "INSERT INTO photos(photo) VALUES (?)";
        final String photoIdName = "id_photo";

        int generatedRecordId;
        PreparedStatement insertNewPhotoStmt = null;

        try {
            insertNewPhotoStmt = connection.prepareStatement(insertNewPhotoQuery,
                    Statement.RETURN_GENERATED_KEYS);
            insertNewPhotoStmt.setBinaryStream(1, contact.getPhoto().getPhotoStream(),
                    contact.getPhoto().getPhotoSize());

            int affectedRows = insertNewPhotoStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating of new record in photos table is failed, no rows affected");
            }

            ResultSet generatedKeys = insertNewPhotoStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedRecordId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating of new record in photos table is failed," +
                        " no ID (" + photoIdName + ") obtained ");
            }
        }finally {
            closeStatement(insertNewPhotoStmt);
        }
        return generatedRecordId;
    }


    /**
     * @return id of find record
     * @throws SQLException if no such record stored in database
     */
    private int findIdByValueTemplate(final String selectSqlQuery, final String idName,
                                      final String searchValue) throws SQLException{
        int id;
        PreparedStatement findExistedIdStmt = null;

        try {
            findExistedIdStmt = connection.prepareStatement(selectSqlQuery);
            findExistedIdStmt.setString(1, searchValue);

            ResultSet resultSet = findExistedIdStmt.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(idName);
            } else {
                throw new SQLException("Cannot find " + idName + " according value " + searchValue);
            }
        } finally {
            closeStatement(findExistedIdStmt);
        }
        return id;
    }

    /**
     * @return id of created record
     * @throws SQLException if creating of new record is failed
     */
    private int createNewRecordTemplate(final String insertSqlQuery, final String idName,
                                        final String[] values) throws SQLException {
        int generatedRecordId;
        PreparedStatement insertStmt = null;

        try {
            insertStmt = connection.prepareStatement(insertSqlQuery,
                    Statement.RETURN_GENERATED_KEYS);

            // insert values in statement
            for (int i = 1; i < values.length + 1; i++) {
                insertStmt.setString(i, values[i - 1]);
            }

            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows == 0) {
                insertStmt.close();
                throw new SQLException("Creating of new record is failed, no rows affected");
            }

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedRecordId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating of new record is failed, no ID (" + idName + ") obtained ");
            }
        } finally {
            closeStatement(insertStmt);
        }
        return generatedRecordId;
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
                "INSERT INTO contacts(first_name, surname, patronymic, birthday, \n"
                        + "website, email, country, city, address, index_number, gender, marital_status, id_nationality,\n"
                        + "id_company, id_photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            updatedRows += updateContactInfo(contactId, contact);

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
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return updatedRows;
    }

    private int updateContactInfo(final int contactId, final Contact contact) throws SQLException {
        int rowsUpdated = 0;
        int companyId = getContactCompanyId(contact);
        int nationalityId = getContactNationalityId(contact);

        if(contact.getPhoto().getPhotoStream() != null) {
            rowsUpdated += updateContactPhoto(contact);
        }

        PreparedStatement updateContactInfoStmt = null;

        try {
            final String updateContactInfoSqlQuery =
                    "UPDATE contacts AS c \n" +
                            "SET c.first_name = ?, c.surname = ?, c.patronymic = ?, \n" +
                            "c.birthday = ?, c.website = ?, c.email = ?, \n" +
                            "c.country = ?, c.city = ?, c.address = ?, c.index_number = ?, \n" +
                            "c.gender = ?, c.marital_status = ?, c.id_nationality = ?, \n" +
                            "c.id_company = ? \n" +
                            "WHERE c.id_contact = ?";

            updateContactInfoStmt = connection.prepareStatement(updateContactInfoSqlQuery);
            updateContactInfoStmt.setString(1, contact.getFirstName());
            updateContactInfoStmt.setString(2, contact.getSurname());
            updateContactInfoStmt.setString(3, contact.getPatronymic());
            updateContactInfoStmt.setDate(4, java.sql.Date.valueOf(contact.getBirthday()));
            updateContactInfoStmt.setString(5, contact.getWebsite());
            updateContactInfoStmt.setString(6, contact.getEmail());
            updateContactInfoStmt.setString(7, contact.getCountry());
            updateContactInfoStmt.setString(8, contact.getCity());
            updateContactInfoStmt.setString(9, contact.getAddress());
            updateContactInfoStmt.setString(10, contact.getIndexNumber());
            updateContactInfoStmt.setString(11, contact.getGender());
            updateContactInfoStmt.setString(12, contact.getMaritalStatus());
            updateContactInfoStmt.setInt(13, nationalityId);
            updateContactInfoStmt.setInt(14, companyId);
            updateContactInfoStmt.setInt(15, contactId);

            rowsUpdated += updateContactInfoStmt.executeUpdate();
        } finally {
            closeStatement(updateContactInfoStmt);
        }

        return rowsUpdated;
    }

    private int updateContactPhoto(Contact contact) throws SQLException{
        final String updateContactPhotoSqlQuery = "UPDATE photos SET photo = ? WHERE id_photo = ?";

        PreparedStatement updateContactPhotoStmt = null;
        int rowsUpdated;

        try {
            updateContactPhotoStmt = connection.prepareStatement(updateContactPhotoSqlQuery);
            updateContactPhotoStmt.setBinaryStream(1, contact.getPhoto().getPhotoStream(),
                    contact.getPhoto().getPhotoSize());
            updateContactPhotoStmt.setInt(2, contact.getPhoto().getPhotoId());

            rowsUpdated = updateContactPhotoStmt.executeUpdate();
        } finally {
            closeStatement(updateContactPhotoStmt);
        }

        return rowsUpdated;
    }

    @Override
    public int deleteContact(final int contactId) throws ContactDaoException {
        int deletedRows = 0;
        try {
            deletedRows += deleteContactPhones(contactId);
            deletedRows += deleteContactAttachments(contactId);
            // before delete contactInfo get photoId
            int photoId = getContactPhotoId(contactId);

            deletedRows += deleteContactInfo(contactId);
            deletedRows += deleteContactPhoto(photoId);
        } catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return deletedRows;
    }

    private int deleteContactInfo(int contactId) throws SQLException {
        final String deleteContactInfoSqlQuery = "DELETE FROM contacts WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactInfoSqlQuery, contactId);
    }

    private int deleteContactPhoto(int photoId) throws SQLException {
        final String deleteContactPhotosSqlQuery = "DELETE FROM photos WHERE id_photo = ?";
        return deleteRowsByIdTemplate(deleteContactPhotosSqlQuery, photoId);
    }

    private int getContactPhotoId(int contactId) throws SQLException {
        // first need to get photo id
        int photoId;
        final String getContactPhotoIdSqlQuery = "SELECT id_photo FROM contacts WHERE id_contact = ?";
        PreparedStatement getContactPhotoIdStmt = null;
        try {
            getContactPhotoIdStmt = connection.prepareStatement(getContactPhotoIdSqlQuery);
            getContactPhotoIdStmt.setInt(1, contactId);

            ResultSet resultSet = getContactPhotoIdStmt.executeQuery();
            if (resultSet.next()) {
                photoId = resultSet.getInt(1);
            } else {
                throw new SQLException("Cannot find id_photo of contact with id_contact=" + contactId);
            }
        } finally {
            closeStatement(getContactPhotoIdStmt);
        }
        return photoId;
    }

    private int deleteContactAttachments(int contactId) throws SQLException {
        final String deleteContactAttachmentsSqlQuery = "DELETE FROM attachments WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactAttachmentsSqlQuery, contactId);
    }

    private int deleteContactPhones(int contactId) throws SQLException {
        final String deleteContactPhonesSqlQuery = "DELETE FROM phones WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactPhonesSqlQuery, contactId);
    }

    private int deleteRowsByIdTemplate(String deleteSqlQuery, int rowId) throws SQLException{
        int deletedRows;
        PreparedStatement deleteRowsStmt;

        deleteRowsStmt = connection.prepareStatement(deleteSqlQuery);
        deleteRowsStmt.setInt(1, rowId);

        deletedRows = deleteRowsStmt.executeUpdate();
        closeStatement(deleteRowsStmt);
        return deletedRows;
    }

    @Override
    public Contact getContact(final int contactId) throws ContactDaoException {
        Contact contact;
        try {
            contact = getContactInfo(contactId);
            contact.setAttachments(getContactAttachments(contactId));
            contact.setPhones(getContactPhones(contactId));
        }catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        }
        return contact;
    }

    private Contact getContactInfo (final int contactId) throws SQLException{
        Contact contact = new Contact();
        PreparedStatement getContactInfoStmt = null;

        try {
            final String getContactInfoSqlQuery =
                    "SELECT first_name, surname, patronymic, birthday, website, \n" +
                            "email, country, city, address, index_number, gender, marital_status \n" +
                            "FROM contacts \n" +
                            "WHERE id_contact = ? \n";

            getContactInfoStmt = connection.prepareStatement(getContactInfoSqlQuery);
            getContactInfoStmt.setInt(1, contactId);

            ResultSet resultSet = getContactInfoStmt.executeQuery();
            if (resultSet.next()) {
                contact.setContactId(contactId);
                contact.setFirstName(resultSet.getString(1));
                contact.setSurname(resultSet.getString(2));
                contact.setPatronymic(resultSet.getString(3));
                contact.setBirthday(resultSet.getDate(4).toString());
                contact.setWebsite(resultSet.getString(5));
                contact.setEmail(resultSet.getString(6));
                contact.setCountry(resultSet.getString(7));
                contact.setCity(resultSet.getString(8));
                contact.setAddress(resultSet.getString(9));
                contact.setIndexNumber(resultSet.getString(10));
                contact.setGender(resultSet.getString(11));
                contact.setMaritalStatus(resultSet.getString(12));
            } else {
                throw new SQLException("No contact with such id_contact");
            }
        } finally {
            closeStatement(getContactInfoStmt);
        }

        // get fields of contact from other tables
        String company = getContactCompany(contactId);
        contact.setCompany(company);
        String nationality = getContactNationality(contactId);
        contact.setNationality(nationality);
        Photo photo = getContactPhoto(contactId);
        contact.setPhoto(photo);

        return contact;
    }

    private String getContactCompany(int contactId) throws SQLException{
        final String getContactCompanySqlQuery =
                "SELECT comp.company \n" +
                        "FROM companies comp \n" +
                        "INNER JOIN contacts c ON c.id_contact = ? AND comp.id_company = c.id_company";

        return getContactAttributeUsingForeignKey(getContactCompanySqlQuery, contactId);
    }

    private String getContactNationality(int contactId) throws SQLException{
        final String getContactNationalitySqlQuery =
                "SELECT n.nationality \n" +
                        "FROM nationalities n \n" +
                        "INNER JOIN contacts c ON c.id_contact = ? AND n.id_nationality = c.id_nationality";

        return getContactAttributeUsingForeignKey(getContactNationalitySqlQuery, contactId);
    }

    private String getContactAttributeUsingForeignKey(String sqlQuery, int contactId) throws SQLException{
        String attribute;
        PreparedStatement getContactAttributeStmt = null;
        try {
            getContactAttributeStmt = connection.prepareStatement(sqlQuery);
            getContactAttributeStmt.setInt(1, contactId);

            ResultSet resultSet = getContactAttributeStmt.executeQuery();
            if (resultSet.next()) {
                attribute = resultSet.getString(1);
            } else {
                throw new SQLException("Cannot get contact attribute using foreign key with id_contact=" + contactId);
            }
        } finally {
            closeStatement(getContactAttributeStmt);
        }
        return attribute;
    }

    private List<Attachment> getContactAttachments (final int contactId) throws SQLException{
        List<Attachment> attachments = new ArrayList<>();
        PreparedStatement getContactAttachmentsStmt = null;

        try {
            final String getContactAttachmentsSqlQuery =
                    "SELECT id_attachment, id_contact, name, download_date, commentary, attachment\n" +
                            "FROM attachments WHERE id_contact = ?";

            getContactAttachmentsStmt = connection.prepareStatement(getContactAttachmentsSqlQuery);
            getContactAttachmentsStmt.setInt(1, contactId);

            ResultSet resultSet = getContactAttachmentsStmt.executeQuery();
            while (resultSet.next()) {
                Attachment attachment = new Attachment();
                attachment.setAttachmentId(resultSet.getInt(1));
                attachment.setContactId(resultSet.getInt(2));
                attachment.setFileName(resultSet.getString(3));
                attachment.setDownloadDate(resultSet.getDate(4).toString());
                attachment.setComment(resultSet.getString(5));
                attachment.setFileStream(resultSet.getBinaryStream(6));

                attachments.add(attachment);
            }
        }finally {
            closeStatement(getContactAttachmentsStmt);
        }
        return attachments;
    }

    private List<Phone> getContactPhones (final int contactId) throws SQLException{
        List<Phone> phones = new ArrayList<>();
        PreparedStatement getContactPhonesStmt = null;

        try {
            final String getContactPhonesSqlQuery =
                    "SELECT id_phone, id_contact, phone_number, commentary, phone_type \n" +
                            "FROM phones \n" +
                            "WHERE id_contact = ?";

            getContactPhonesStmt = connection.prepareStatement(getContactPhonesSqlQuery);
            getContactPhonesStmt.setInt(1, contactId);

            ResultSet resultSet = getContactPhonesStmt.executeQuery();
            while (resultSet.next()) {
                Phone phone = new Phone();
                phone.setPhoneId(resultSet.getInt(1));
                phone.setContactId(resultSet.getInt(2));
                phone.setPhoneNumber(resultSet.getString(3));
                phone.setComment(resultSet.getString(4));
                phone.setPhoneType(resultSet.getString(5));

                phones.add(phone);
            }
        } finally {
            closeStatement(getContactPhonesStmt);
        }

        return phones;
    }

    private Photo getContactPhoto (final int contactId) throws SQLException{
        Photo photo;
        PreparedStatement getContactPhotoStmt = null;
        try {
            final String getContactPhotoSqlQuery =
                    "SELECT p.id_photo, p.photo \n" +
                            "FROM photos p \n" +
                            "INNER JOIN contacts c ON c.id_photo = p.id_photo AND c.id_contact = ?";

            getContactPhotoStmt = connection.prepareStatement(getContactPhotoSqlQuery);
            getContactPhotoStmt.setInt(1, contactId);

            ResultSet resultSet = getContactPhotoStmt.executeQuery();
            if (resultSet.next()) {
                photo = new Photo();
                photo.setPhotoId(resultSet.getInt(1));
                photo.setPhotoStream(resultSet.getBinaryStream(2));
            } else {
                throw new SQLException("Cannot find row in photos table associated with contact who's id_contact=" + contactId);
            }
        } finally {
            closeStatement(getContactPhotoStmt);
        }
        return photo;
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
                Contact contact = mapParamFromResultSetToContact(resultSet);
                contacts.add(contact);
            }
        } catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
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
            AppLogger.error(sqle.getMessage());
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
            AppLogger.error(sqle.getMessage());
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
            rowsUpdated = updateOnlyAttachmentFields(attachmentId, attachment);
        } else {
            rowsUpdated = updateFullAttachment(attachmentId, attachment);
        }
        return rowsUpdated;
    }

    private int updateOnlyAttachmentFields(final int attachmentId, final Attachment attachment)
            throws ContactDaoException{
        int rowsUpdated;
        PreparedStatement updateAttachmentFromContactStmt = null;

        final String updateAttachmentFromContactSqlQuery =
                "UPDATE attachments \n" +
                        "SET name = ?, commentary = ? \n" +
                        "WHERE id_attachment = ?";
        try {
            updateAttachmentFromContactStmt = connection.prepareStatement(updateAttachmentFromContactSqlQuery);

            updateAttachmentFromContactStmt.setString(1, attachment.getFileName());
            updateAttachmentFromContactStmt.setString(2, attachment.getComment());
            updateAttachmentFromContactStmt.setInt(3, attachmentId);

            rowsUpdated = updateAttachmentFromContactStmt.executeUpdate();
        } catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(updateAttachmentFromContactStmt);
        }
        return rowsUpdated;
    }

    private int updateFullAttachment(final int attachmentId, final Attachment attachment)
            throws ContactDaoException{
        int rowsUpdated;
        PreparedStatement updateAttachmentFromContactStmt = null;

        final String updateAttachmentFromContactSqlQuery =
                "UPDATE attachments \n" +
                        "SET name = ?, download_date = ?, commentary = ?, attachment = ? \n" +
                        "WHERE id_attachment = ?";
        try {
            updateAttachmentFromContactStmt = connection.prepareStatement(updateAttachmentFromContactSqlQuery);

            updateAttachmentFromContactStmt.setString(1, attachment.getFileName());
            updateAttachmentFromContactStmt.setDate(2,
                    java.sql.Date.valueOf(attachment.getDownloadDate()));
            updateAttachmentFromContactStmt.setString(3, attachment.getComment());
            updateAttachmentFromContactStmt.setBinaryStream(4,
                    attachment.getFileStream(), attachment.getFileSize());
            updateAttachmentFromContactStmt.setInt(5, attachmentId);

            rowsUpdated = updateAttachmentFromContactStmt.executeUpdate();
        } catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(updateAttachmentFromContactStmt);
        }
        return rowsUpdated;
    }

    @Override
    public int deleteAttachmentFromContact(final int attachmentId) throws ContactDaoException {
        int rowsDeleted;
        final String deleteAttachmentSqlQuery = "DELETE FROM attachments WHERE id_attachment = ?";
        try {
            rowsDeleted = deleteRowsByIdTemplate(deleteAttachmentSqlQuery, attachmentId);
        }catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
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
            AppLogger.error(sqle.getMessage());
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
            AppLogger.error(sqle.getMessage());
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
            rowsDeleted = deleteRowsByIdTemplate(deletePhoneSqlQuery, phoneId);
        }catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
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
                AppLogger.error(sqle.getMessage());
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
                Contact contact = mapParamFromResultSetToContact(resultSet);
                contactList.add(contact);
            }
        } catch (SQLException sqle){
            AppLogger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(findContactsStmt);
        }
        return contactList;
    }

    private Contact mapParamFromResultSetToContact(ResultSet resultSet) throws SQLException{
        Contact contact = new Contact();

        contact.setContactId(resultSet.getInt(1));
        contact.setFirstName(resultSet.getString(2));
        contact.setSurname(resultSet.getString(3));
        contact.setPatronymic(resultSet.getString(4));
        contact.setCountry(resultSet.getString(5));
        contact.setCity(resultSet.getString(6));
        contact.setAddress(resultSet.getString(7));
        contact.setIndexNumber(resultSet.getString(8));
        contact.setCompany(resultSet.getString(9));
        contact.setBirthday(resultSet.getString(10));

        return contact;
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
            AppLogger.error(sqle.getMessage());
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
            AppLogger.error(e.getMessage());
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
            AppLogger.error(sqle.getMessage());
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
                AppLogger.error(sqlex.getMessage());
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
            AppLogger.error(sqle.getMessage());
        }
    }
}
