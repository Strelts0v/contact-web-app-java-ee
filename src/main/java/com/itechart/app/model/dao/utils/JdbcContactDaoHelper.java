package com.itechart.app.model.dao.utils;

import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.Phone;
import com.itechart.app.model.entities.Photo;
import com.itechart.app.model.exceptions.ContactDaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcContactDaoHelper {

    private final Logger logger = LoggerFactory.getLogger(JdbcContactDao.class);

    private Connection connection;

    public JdbcContactDaoHelper(Connection connection){
        this.connection = connection;
    }

    /**
     * @return id of created record
     * @throws SQLException if creating of new record is failed
     */
    public int createNewRecordTemplate(final String insertSqlQuery, final String idName,
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
     * @return id of find record
     * @throws SQLException if no such record stored in database
     */
    public int findIdByValueTemplate(final String selectSqlQuery, final String idName,
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

    public int deleteContactAttachments(int contactId) throws SQLException {
        final String deleteContactAttachmentsSqlQuery = "DELETE FROM attachments WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactAttachmentsSqlQuery, contactId);
    }

    public int deleteContactPhones(int contactId) throws SQLException {
        final String deleteContactPhonesSqlQuery = "DELETE FROM phones WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactPhonesSqlQuery, contactId);
    }

    public int deleteContactInfo(int contactId) throws SQLException {
        final String deleteContactInfoSqlQuery = "DELETE FROM contacts WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactInfoSqlQuery, contactId);
    }

    public int deleteContactPhoto(int photoId) throws SQLException {
        final String deleteContactPhotosSqlQuery = "DELETE FROM photos WHERE id_photo = ?";
        return deleteRowsByIdTemplate(deleteContactPhotosSqlQuery, photoId);
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

    public int deleteRowsByIdTemplate(String deleteSqlQuery, int rowId) throws SQLException{
        int deletedRows;
        PreparedStatement deleteRowsStmt;

        deleteRowsStmt = connection.prepareStatement(deleteSqlQuery);
        deleteRowsStmt.setInt(1, rowId);

        deletedRows = deleteRowsStmt.executeUpdate();
        closeStatement(deleteRowsStmt);
        return deletedRows;
    }

    public Contact getContactInfo (final int contactId) throws SQLException{
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

    public List<Attachment> getContactAttachments (final int contactId) throws SQLException{
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

    public Photo getContactPhoto (final int contactId) throws SQLException{
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

    public List<Phone> getContactPhones (final int contactId) throws SQLException{
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

    public int updateContactInfo(final int contactId, final Contact contact) throws SQLException {
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

    public int updateContactPhoto(Contact contact) throws SQLException{
        final String updateContactPhotoSqlQuery = "UPDATE photos SET photo = ? WHERE id_photo = ?";
        int rowsUpdated;
        PreparedStatement updateContactPhotoStmt = null;

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

    public int updateOnlyAttachmentFields(final int attachmentId, final Attachment attachment)
            throws ContactDaoException {
        int rowsUpdated;
        final String updateAttachmentFromContactSqlQuery =
                "UPDATE attachments \n" +
                        "SET name = ?, commentary = ? \n" +
                        "WHERE id_attachment = ?";
        PreparedStatement updateAttachmentFromContactStmt = null;

        try {
            updateAttachmentFromContactStmt = connection.prepareStatement(updateAttachmentFromContactSqlQuery);

            updateAttachmentFromContactStmt.setString(1, attachment.getFileName());
            updateAttachmentFromContactStmt.setString(2, attachment.getComment());
            updateAttachmentFromContactStmt.setInt(3, attachmentId);

            rowsUpdated = updateAttachmentFromContactStmt.executeUpdate();
        } catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(updateAttachmentFromContactStmt);
        }
        return rowsUpdated;
    }

    public int updateFullAttachment(final int attachmentId, final Attachment attachment)
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
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(updateAttachmentFromContactStmt);
        }
        return rowsUpdated;
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

    public Contact mapParamFromResultSetToContact(ResultSet resultSet) throws SQLException{
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

    public List<Integer> getContactDataIdsTemplate(String sqlQuery, int contactId) throws ContactDaoException{
        List<Integer> ids;

        PreparedStatement getContactDataIdsStmt = null;
        try{
            getContactDataIdsStmt = connection.prepareStatement(sqlQuery);
            getContactDataIdsStmt.setInt(1, contactId);

            ResultSet resultSet = getContactDataIdsStmt.executeQuery();
            ids = new ArrayList<>();
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                ids.add(id);
            }
        }catch (SQLException sqle){
            logger.error(sqle.getMessage());
            throw new ContactDaoException(sqle);
        } finally {
            closeStatement(getContactDataIdsStmt);
        }
        return ids;
    }

    public int getContactCompanyId(final Contact contact) throws SQLException{
        // first try to find existed company with such name

        // make searching case-insensitive
        final String findExistedCompanySqlQuery =
                "SELECT id_company FROM companies "
                        + "WHERE company COLLATE UTF8_GENERAL_CI = ?";

        final String idName = "id_company";
        try {
            return findIdByValueTemplate(findExistedCompanySqlQuery, idName, contact.getCompany());
        } catch (SQLException e){
            logger.info(e.getMessage());
        }

        // if not create new company in database
        final String insertCompanySqlQuery = "INSERT INTO companies(company) VALUES (?)";
        final String companyIdName = "id_company";
        final String[] values = { contact.getCompany()};

        return createNewRecordTemplate(insertCompanySqlQuery, companyIdName, values);
    }

    public int getContactNationalityId(final Contact contact) throws SQLException{
        // first try to find existed nationality with such name
        // make searching case-insensitive
        final String findExistedNationalitySqlQuery =
                "SELECT id_nationality FROM nationalities \n" +
                        "WHERE nationality COLLATE UTF8_GENERAL_CI = ?";
        final String idName = "id_nationality";

        try {
            return findIdByValueTemplate(findExistedNationalitySqlQuery, idName, contact.getNationality());
        } catch (SQLException e){
            logger.info(e.getMessage());
        }

        // if not create new nationality in database
        final String insertNewNationalityQuery = "INSERT INTO nationalities(nationality) VALUES (?)";
        final String nationalityIdName = "id_nationality";
        final String[] values = { contact.getNationality()};

        return createNewRecordTemplate(insertNewNationalityQuery, nationalityIdName, values);
    }

    public int getContactPhotoId(int contactId) throws SQLException {
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

    public int addContactPhoto(Contact contact) throws SQLException{
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
}
