package com.itechart.app.model.utils;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class DatabaseConnectionManagerTest {

    private DatabaseConnectionManager manager = DatabaseConnectionManager.getInstance();

    @Test
    public void getDatabaseConnectionManagerTest(){
        final String errorMessage = "Expected not null database connection manager instance";

        Assert.assertNotNull(errorMessage, manager);
    }

    @Test
    public void getConnectionFromConnectionPoolTest() throws Exception {
        Connection connection = manager.getConnection();
        final String errorMessage = "Expected not null connection instance from connection pool";

        Assert.assertNotNull(errorMessage, connection);
    }
}