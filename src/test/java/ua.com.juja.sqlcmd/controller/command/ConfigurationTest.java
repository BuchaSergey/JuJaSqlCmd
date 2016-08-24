package ua.com.juja.sqlcmd.controller.command;


import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.Configuration;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class ConfigurationTest {
    private Properties properties;
    private Configuration conf;
    private DatabaseManager manager;
    private View view;
    private Command command;


    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);

    }

    @Test
    public void testGetDatabaseName()
    {
     String databaseName = conf.getDatabaseName();
  //      String databaseName = properties.getProperty("database.name");
        assertEquals("sqlcmd", databaseName);
    }
}
