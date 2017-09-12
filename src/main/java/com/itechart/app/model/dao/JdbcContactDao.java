package com.itechart.app.model.dao;

import com.itechart.app.logging.AppLogger;
import com.itechart.app.model.entities.Attachment;
import com.itechart.app.model.entities.Contact;
import com.itechart.app.model.entities.Phone;
import com.itechart.app.model.utils.DatabaseConnectionManager;

import java.sql.*;
import java.util.List;

/**
 * provides interface to access database with contacts using JDBC
 */
public class JdbcContactDao implements ContactDao {

    private Connection connection;

    /**
     * incapsulates creating of new JdbcContactDao instances
     * @return JdbcContactDao instances
     * @throws SQLException when there is no available Connection
     * or DatabaseConnectionManager
     */
    public static JdbcContactDao newInstance() throws SQLException{
        DatabaseConnectionManager manager = DatabaseConnectionManager.getInstance();
        if(manager == null){
            throw new SQLException();
        }
        Connection connection = manager.getConnection();
        return new JdbcContactDao(connection);
    }

    private JdbcContactDao(Connection connection){
        this.connection = connection;
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
            e.printStackTrace();
            if(connection != null){
                try{
                    connection.rollback();
                } catch (SQLException ex){
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e){
                e.printStackTrace();
            }
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
            return findIdTemplate(findExistedCompanySqlQuery, idName, contact.getCompany());
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

        return findIdTemplate(findExistedGenderSqlQuery, idName, contact.getGender());
    }

    private int getContactNationalityId(final Contact contact) throws SQLException{
        // first try to find existed nationality with such name

        // make searching case-insensitive
        final String findExistedNationalitySqlQuery = "SELECT id_nationality FROM nationalities "
                + "WHERE nationality COLLATE UTF8_GENERAL_CI = ?";

        final String idName = "id_nationality";

        try {
            return findIdTemplate(findExistedNationalitySqlQuery, idName, contact.getNationality());
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

        return findIdTemplate(findExistedNationalitySqlQuery, idName, contact.getMaritalStatus());
    }

    /**
     * @return id of find record
     * @throws SQLException if no such record stored in database
     */
    private int findIdTemplate(final String selectSqlQuery, final String idName,
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

        final String addContactSqlQuery = "INSERT INTO contacts(first_name, surname, patronymic, birthday, "
                + "website, email, id_address, id_company, id_gender, id_nationality, id_marital_status) "
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
        return 0;
    }

    @Override
    public Contact getContact(final int contactId) {
        return null;
    }

    @Override
    public List<Contact> getContacts(final int recordOffset, final int recordCount) {
        return null;
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
}
