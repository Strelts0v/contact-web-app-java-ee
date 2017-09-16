package com.itechart.app.model.dao;

import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.entities.*;
import com.itechart.app.model.utils.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * provides interface to access database with contacts using JDBC
 */
public class JdbcContactDao implements ContactDao {

    private Connection connection;

    private JdbcContactDao(Connection connection){
        this.connection = connection;
    }

    /**
     * incapsulates creating of new JdbcContactDao instances
     * @return JdbcContactDao instances
     * @throws SQLException when there is no available Connection
     * or DatabaseConnectionManager
     */
    public static JdbcContactDao newInstance() {
        DatabaseConnectionManager manager = DatabaseConnectionManager.getInstance();
        Connection connection;
        JdbcContactDao dao = null;
        try {
            if (manager == null) {
                throw new SQLException();
            }
            connection = manager.getConnection();
            dao = new JdbcContactDao(connection);
        } catch (SQLException e){
            AppLogger.error(e.getMessage());
        } finally {
            return dao;
        }
    }

    @Override
    public Contact createContact(Contact contact) {
        try {
            connection.setAutoCommit(false);

            int addressId = getContactAddressId(contact);
            int companyId = getContactCompanyId(contact);
            int genderId = getContactGenderId(contact);
            int nationalityId = getContactNationalityId(contact);
            int maritalStatusId = getContactMaritalStatusId(contact);

            int contactId = addContactToDatabase(contact, addressId, companyId,
                    genderId, nationalityId, maritalStatusId);
            contact.setContactId(contactId);

            connection.commit();
        } catch (SQLException e){
            AppLogger.error(e.getMessage());
            if(connection != null){
                try{
                    connection.rollback();
                } catch (SQLException ex){
                    AppLogger.error(ex.getMessage());
                }
            }
        } finally {
            return contact;
        }
    }

    /**
     * creates new address in database
     * @param contact data transfer object
     * @return - id of created address
     * @throws SQLException - if creating of address is failed
     */
    private int getContactAddressId(final Contact contact) throws SQLException{
        final String addAddressSqlQuery = "INSERT INTO addresses (country, city, address, index_number)"
                + " VALUES (?, ?, ?, ?)";

        final String addressIdName = "id_address";

        final String[] values = {
                contact.getAddress().getCountry(),
                contact.getAddress().getCity(),
                contact.getAddress().getAddress(),
                contact.getAddress().getIndex()
        };

        return createNewRecordTemplate(addAddressSqlQuery, addressIdName, values);
    }

    private int getContactCompanyId(final Contact contact) throws SQLException{
        // first try to find existed company with such name

        // make searching case-insensitive
        final String findExistedCompanySqlQuery = "SELECT id_company FROM companies "
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

    private int getContactGenderId(final Contact contact) throws SQLException {

        // make searching case-insensitive
        final String findExistedGenderSqlQuery = "SELECT id_gender FROM genders "
                + "WHERE gender COLLATE UTF8_GENERAL_CI = ?";

        final String idName = "id_gender";

        return findIdByValueTemplate(findExistedGenderSqlQuery, idName, contact.getGender());
    }

    private int getContactNationalityId(final Contact contact) throws SQLException{
        // first try to find existed nationality with such name

        // make searching case-insensitive
        final String findExistedNationalitySqlQuery = "SELECT id_nationality FROM nationalities "
                + "WHERE nationality COLLATE UTF8_GENERAL_CI = ?";

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

    /**
     * @return id of find marital status
     * @throws SQLException if no such marital status in database
     */
    private int getContactMaritalStatusId(final Contact contact) throws SQLException{
        // make searching case-insensitive
        final String findExistedNationalitySqlQuery = "SELECT id_marital_status FROM marital_status "
                + "WHERE marital_status_name COLLATE UTF8_GENERAL_CI = ?";

        final String idName = "id_marital_status";

        return findIdByValueTemplate(findExistedNationalitySqlQuery, idName, contact.getMaritalStatus());
    }

    /**
     * @return id of find record
     * @throws SQLException if no such record stored in database
     */
    private int findIdByValueTemplate(final String selectSqlQuery, final String idName,
                                      final String searchValue) throws SQLException{
        int id;
        PreparedStatement findExistedIdStmt;

        findExistedIdStmt = connection.prepareStatement(selectSqlQuery);
        findExistedIdStmt.setString(1, searchValue);

        ResultSet resultSet = findExistedIdStmt.executeQuery();
        if(resultSet.next()){
            id = resultSet.getInt(idName);
        } else {
            throw new SQLException("Cannot find " + idName + " according value " + searchValue);
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
        PreparedStatement insertStmt;

        insertStmt = connection.prepareStatement(insertSqlQuery,
                Statement.RETURN_GENERATED_KEYS);

        // insert values in statement
        for(int i = 1; i < values.length + 1; i++){
            insertStmt.setString(i, values[i-1]);
        }

        int affectedRows = insertStmt.executeUpdate();
        if(affectedRows == 0){
            throw new SQLException("Creating of new record is failed, no rows affected");
        }

        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        if(generatedKeys.next()){
            generatedRecordId = generatedKeys.getInt(1);
        }
        else {
            throw new SQLException("Creating of new record is failed, no ID (" + idName + ") obtained ");
        }
        return generatedRecordId;
    }

    /**
     * @return id of created contact
     * @throws SQLException if creating of new contact is failed
     */
    private int addContactToDatabase(final Contact contact, final int addressId, final int companyId,
                                     final int genderId, final int nationalityId, final int maritalStatusId)
            throws SQLException {

        int contactId;
        PreparedStatement addContactStmt;

        final String addContactSqlQuery = "INSERT INTO contacts(first_name, surname, patronymic, birthday, \n"
                + "website, email, id_address, id_company, id_gender, id_nationality, id_marital_status) \n"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        addContactStmt = connection.prepareStatement(addContactSqlQuery,
                Statement.RETURN_GENERATED_KEYS);

        addContactStmt.setString(1, contact.getFirstName());
        addContactStmt.setString(2, contact.getSurname());
        addContactStmt.setString(3, contact.getPatronymic());
        addContactStmt.setDate(4, new java.sql.Date(contact.getBirthday().getTime()));
        addContactStmt.setString(5, contact.getWebsite());
        addContactStmt.setString(6, contact.getEmail());
        addContactStmt.setInt(7, addressId);
        addContactStmt.setInt(8, companyId);
        addContactStmt.setInt(9, genderId);
        addContactStmt.setInt(10, nationalityId);
        addContactStmt.setInt(11, maritalStatusId);

        int affectedRows = addContactStmt.executeUpdate();
        if(affectedRows == 0){
            throw new SQLException("Creating of new contact is failed, no rows affected");
        }

        ResultSet generatedKeys = addContactStmt.getGeneratedKeys();
        if(generatedKeys.next()){
            contactId = generatedKeys.getInt(1);
        }
        else {
            throw new SQLException("Creating of new contact is failed, no ID (id_contact) obtained ");
        }
        return contactId;
    }

    @Override
    public int updateContact(final int contactId, final Contact contact) {
        return 0;
    }

    @Override
    public int deleteContact(final int contactId) {
        int deletedRows = 0;
        try {
            connection.setAutoCommit(false);

            deletedRows += deleteContactPhones(contactId);
            deletedRows += deleteContactAttachments(contactId);
            deletedRows += deleteContactPhotos(contactId);

            // get addressId of deleted contact
            int addressId = getContactAddressId(contactId);
            deletedRows += deleteContactInfo(contactId);
            deletedRows += deleteContactAddress(addressId);

            connection.commit();
        } catch (SQLException e){
            AppLogger.error(e.getMessage());
            deletedRows = 0;
            if(connection != null){
                try{
                    connection.rollback();
                } catch (SQLException ex){
                    AppLogger.error(ex.getMessage());
                }
            }
        } finally {
            return deletedRows;
        }
    }

    private int deleteContactAddress(int addressId) throws SQLException {
        final String deleteContactAddressSqlQuery = "DELETE FROM addresses WHERE id_address = ?";
        return deleteRowsByIdTemplate(deleteContactAddressSqlQuery, addressId);
    }

    private int deleteContactInfo(int contactId) throws SQLException {
        final String deleteContactInfoSqlQuery = "DELETE FROM contacts WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactInfoSqlQuery, contactId);
    }

    private int deleteContactPhotos(int contactId) throws SQLException {
        final String deleteContactPhotosSqlQuery = "DELETE FROM photos WHERE id_contact = ?";
        return deleteRowsByIdTemplate(deleteContactPhotosSqlQuery, contactId);
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
        return deletedRows;
    }

    private int getContactAddressId(int contactId) throws SQLException {
        int addressId;
        PreparedStatement getContactAddressIdStmt;

        final String getContactAddressIdSqlQuery = "SELECT id_address FROM contacts WHERE id_contact = ?";

        getContactAddressIdStmt = connection.prepareStatement(getContactAddressIdSqlQuery);
        getContactAddressIdStmt.setInt(1, contactId);

        ResultSet resultSet = getContactAddressIdStmt.executeQuery();
        if(resultSet.next()){
            addressId = resultSet.getInt(1);
        } else {
            throw new SQLException("Cannot find id_address according id_contact = " + contactId);
        }
        return addressId;
    }

    @Override
    public Contact getContact(final int contactId) {
        Contact contact = null;
        try {
            contact = getContactInfo(contactId);
            contact.setAttachments(getContactAttachments(contactId));
            contact.setPhones(getContactPhones(contactId));
            contact.setPhoto(getContactPhoto(contactId));
        }catch (SQLException e){
            AppLogger.error(e.getMessage());
        }finally {
            return contact;
        }

    }

    private Contact getContactInfo (final int contactId) throws SQLException{
        Contact contact = new Contact();
        PreparedStatement getContactInfoStmt;

        final String getContactInfoSqlQuery =
                "SELECT  c.first_name, c.surname, c.patronymic,\n" +
                        "c.birthday, c.website, c.email, a.country, a.city, a.address,\n" +
                        "a.index_number, comp.company, g.gender, n.nationality, m.marital_status_name\n" +
                        "FROM contacts AS c\n" +
                        "INNER JOIN addresses AS a ON c.id_address = a.id_address\n" +
                        "INNER JOIN companies AS comp ON c.id_company = comp.id_company\n" +
                        "INNER JOIN genders AS g ON c.id_gender = g.id_gender\n" +
                        "INNER JOIN nationalities AS n ON c.id_nationality = n.id_nationality\n" +
                        "INNER JOIN marital_status AS m ON c.id_marital_status = m.id_marital_status\n" +
                        "WHERE c.id_contact = ?";

        getContactInfoStmt = connection.prepareStatement(getContactInfoSqlQuery);
        getContactInfoStmt.setInt(1,contactId);

        ResultSet resultSet = getContactInfoStmt.executeQuery();
        if(resultSet.next()){
            contact.setContactId(contactId);
            contact.setFirstName(resultSet.getString(1));
            contact.setSurname(resultSet.getString(2));
            contact.setPatronymic(resultSet.getString(3));
            contact.setBirthday(resultSet.getDate(4));
            contact.setWebsite(resultSet.getString(5));
            contact.setEmail(resultSet.getString(6));

            Address address = new Address();
            address.setCountry(resultSet.getString(7));
            address.setCity(resultSet.getString(8));
            address.setAddress(resultSet.getString(9));
            address.setIndex(resultSet.getString(10));

            contact.setAddress(address);

            contact.setCompany(resultSet.getString(11));
            contact.setGender(resultSet.getString(12));
            contact.setNationality(resultSet.getString(13));
            contact.setMaritalStatus(resultSet.getString(14));
        } else {
            throw new SQLException("No contact with such id_contact");
        }
        return contact;
    }

    private List<Attachment> getContactAttachments (final int contactId) throws SQLException{
        List<Attachment> attachments = new ArrayList<>();
        PreparedStatement getContactAttachmentsStmt;

        final String getContactAttachmentsSqlQuery =
                "SELECT a.id_attachment, a.name, a.download_date, a.commentary, a.attachment\n" +
                "FROM attachments AS a WHERE id_contact = ?";

        getContactAttachmentsStmt = connection.prepareStatement(getContactAttachmentsSqlQuery);
        getContactAttachmentsStmt.setInt(1, contactId);

        ResultSet resultSet = getContactAttachmentsStmt.executeQuery();
        while(resultSet.next()){
            Attachment attachment = new Attachment();
            attachment.setAttachmentId(resultSet.getInt(1));
            attachment.setFileName(resultSet.getString(2));
            attachment.setDownloadDate(resultSet.getDate(3));
            attachment.setComment(resultSet.getString(4));
            attachment.setFileStream(resultSet.getBinaryStream(5));

            attachments.add(attachment);
        }

        return attachments;
    }

    private List<Phone> getContactPhones (final int contactId) throws SQLException{
        List<Phone> phones = new ArrayList<>();
        PreparedStatement getContactPhonesStmt;

        final String getContactPhonesSqlQuery =
                "SELECT p.id_phone, p.phone_number, p.commentary, pt.phone_type\n" +
                "FROM phones AS p\n" +
                "INNER JOIN phone_types AS pt ON p.id_phone_type = pt.id_phone_type\n" +
                "WHERE p.id_contact = ?";

        getContactPhonesStmt = connection.prepareStatement(getContactPhonesSqlQuery);
        getContactPhonesStmt.setInt(1, contactId);

        ResultSet resultSet = getContactPhonesStmt.executeQuery();
        while(resultSet.next()){
            Phone phone = new Phone();
            phone.setPhoneId(resultSet.getInt(1));
            phone.setPhoneNumber(resultSet.getString(2));
            phone.setComment(resultSet.getString(3));
            phone.setPhoneType(resultSet.getString(4));

            phones.add(phone);
        }

        return phones;
    }

    private Photo getContactPhoto (final int contactId) throws SQLException{
        Photo photo;
        PreparedStatement getContactPhotoStmt;

        final String getContactPhotoSqlQuery =
                "SELECT p.id_photo, p.photo FROM photos AS p \n" +
                "WHERE p.id_contact = ?";

        getContactPhotoStmt = connection.prepareStatement(getContactPhotoSqlQuery);
        getContactPhotoStmt.setInt(1, contactId);

        ResultSet resultSet = getContactPhotoStmt.executeQuery();
        if(resultSet.next()){
            photo = new Photo();
            photo.setPhotoId(resultSet.getInt(1));
            photo.setPhotoStream(resultSet.getBinaryStream(2));
        } else {
            photo = Photo.EMPTY_PHOTO;
        }
        return photo;
    }

    @Override
    public List<Contact> getContacts(final int recordOffset, final int recordCount) {
        List<Contact> contacts = null;
        PreparedStatement getContactsStmt;
        final String getContactsSqlQuery =
                "SELECT SQL_CALC_FOUND_ROWS c.id_contact, c.first_name, c.surname, c.patronymic, \n"
                        + "c.birthday, c.website, a.country, a.city, a.address, a.index_number \n"
                        + "FROM contacts AS c \n"
                        + "INNER JOIN addresses AS a ON c.id_address = a.id_address \n"
                        + "LIMIT ?, ?";

        try {
            getContactsStmt = connection.prepareStatement(getContactsSqlQuery);
            getContactsStmt.setInt(1, recordOffset);
            getContactsStmt.setInt(2, recordCount);

            ResultSet rSet = getContactsStmt.executeQuery();
            contacts = new ArrayList<>(recordCount);

            while(rSet.next()){
                Contact c = new Contact();
                c.setContactId(rSet.getInt(1));
                c.setFirstName(rSet.getString(2));
                c.setSurname(rSet.getString(3));
                c.setPatronymic(rSet.getString(4));
                c.setBirthday(rSet.getDate(5));
                c.setWebsite((rSet.getString(6)));

                Address a = new Address();
                a.setCountry(rSet.getString(7));
                a.setCity(rSet.getString(8));
                a.setAddress(rSet.getString(9));
                a.setIndex(rSet.getString(10));

                c.setAddress(a);
                contacts.add(c);
            }
        } catch (SQLException e){
            AppLogger.error(e.getMessage());
        } finally {
            return contacts;
        }
    }

    @Override
    public int addAttachmentToContact(final int contactId, final Attachment attachment) {
        return 0;
    }

    @Override
    public int updateAttachmentFromContact(final int attachmentId, final Attachment attachment) {
        return 0;
    }

    @Override
    public int deleteAttachmentFromContact(final int attachmentId) {
        return 0;
    }

    @Override
    public int addPhoneToContact(final int contactId, final Phone phone) {
        return 0;
    }

    @Override
    public int updatePhoneFromContact(final int phoneId, Phone phone) {
        return 0;
    }

    @Override
    public int deletePhoneFromContact(final int phoneId) {
        return 0;
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
}
