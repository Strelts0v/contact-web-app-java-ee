package com.itechart.app.model.utils;

import com.itechart.app.model.cryptography.Cryptographer;
import com.itechart.app.model.cryptography.CryptographerXor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * specifies mechanism of getting connections with database
 */
public class DatabaseConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);

    private ComboPooledDataSource dataSource;

    private volatile static DatabaseConnectionManager INSTANCE;

    /** object for extracting names of pages from resource bundle pages.properties */
    private static final ResourceBundle databaseConfigBundle
            = ResourceBundle.getBundle("dbconfig");

    private static final String DATABASE_DRIVER_KEY = "database.driver";
    private static final String DATABASE_URL_KEY = "database.url";
    private static final String DATABASE_USER_KEY = "database.user";
    private static final String DATABASE_PASSWORD_KEY = "database.password";
    private static final String DATABASE_CONNECTION_POOL_MIN_SIZE_KEY
            = "database.connection.pool.min.size";
    private static final String DATABASE_CONNECTION_POOL_ACQUIRE_INCREMENT_KEY
            = "database.connection.pool.acquire.increment";
    private static final String DATABASE_CONNECTION_POOL_MAX_SIZE_KEY
            = "database.connection.pool.max.size";

    private DatabaseConnectionManager() throws PropertyVetoException {
        dataSource = new ComboPooledDataSource();

        //loads the jdbc driver
        dataSource.setDriverClass(databaseConfigBundle.getString(DATABASE_DRIVER_KEY));
        dataSource.setJdbcUrl(databaseConfigBundle.getString(DATABASE_URL_KEY));
        dataSource.setUser(databaseConfigBundle.getString(DATABASE_USER_KEY));

        String decryptPassword = databaseConfigBundle.getString(DATABASE_PASSWORD_KEY);
        Cryptographer cryptographer = new CryptographerXor();
        dataSource.setPassword(cryptographer.decrypt(decryptPassword));

        // the settings below are optional -- c3p0 can work with defaults
        dataSource.setMinPoolSize(Integer.parseInt(
                databaseConfigBundle.getString(DATABASE_CONNECTION_POOL_MIN_SIZE_KEY)));
        dataSource.setAcquireIncrement(Integer.parseInt(
                databaseConfigBundle.getString(DATABASE_CONNECTION_POOL_ACQUIRE_INCREMENT_KEY)));
        dataSource.setMaxPoolSize(Integer.parseInt(
                databaseConfigBundle.getString(DATABASE_CONNECTION_POOL_MAX_SIZE_KEY)));
    }

    public static DatabaseConnectionManager getInstance() {
        try {
            if (INSTANCE == null) {
                synchronized (DatabaseConnectionManager.class){
                    if (INSTANCE == null) {
                        INSTANCE = new DatabaseConnectionManager();
                    }
                }
            }
        }catch (PropertyVetoException e){
            logger.error(e.getMessage());
        }
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
