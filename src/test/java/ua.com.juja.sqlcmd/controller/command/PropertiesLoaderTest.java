package ua.com.juja.sqlcmd.controller.command;


import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PropertiesLoader;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertEquals;


public class PropertiesLoaderTest {
    private PropertiesLoader properties;
    private DatabaseManager manager;
    private View view;
    private Command command;

    private  PropertiesLoader pl = new PropertiesLoader();

    private final static String DB_NAME = "sqlcmd";
    private final static String SERVER_NAME = "localhost";
    private final static String DB_PORT = "5432";
    private final static String DB_DRIVER = "jdbc:postgresql://";
    private final static String DB_USER_NAME = "postgres";
    private final static String DB_PASSWORD = "postgres";



    @Test
    public void testGetDatabaseName()
    {
        String propDBName = pl.getDatabaseName();
        assertEquals(DB_NAME, propDBName);
    }

    @Test
    public void testGetServerName()
    {
        String serverName = pl.getServerName();
        assertEquals(SERVER_NAME, serverName);
    }

    @Test
    public void testGetDriver()
    {
        String driver = pl.getDriver();
        assertEquals(DB_DRIVER, driver);
    }

    @Test
    public void testGetUserName()
    {
        String userName = pl.getUserName();
        assertEquals(DB_USER_NAME, userName);
    }

    @Test
    public void testGetPassword()
    {
        String password = pl.getPassword();
        assertEquals(DB_PASSWORD, password);
    }

    @Test
    public void testGetPort()
    {
        String databasePort = pl.getDatabasePort();
        assertEquals(DB_PORT, databasePort);
    }
}
