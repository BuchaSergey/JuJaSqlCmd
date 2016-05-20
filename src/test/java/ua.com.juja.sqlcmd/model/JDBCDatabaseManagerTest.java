package ua.com.juja.sqlcmd.model;


import org.junit.Before;

import java.sql.SQLException;

/**
 * Created by Серый on 13.05.2016.
 */
public class JDBCDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManager();
    }

    @Before
    public void setup() {
        manager = new InMemoryDatabaseManager();
        try {
            manager.connect("sqlcmd", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}