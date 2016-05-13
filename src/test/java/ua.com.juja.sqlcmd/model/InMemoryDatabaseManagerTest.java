package ua.com.juja.sqlcmd.model;

import org.junit.Before;


/**
 * Created by Серый on 13.05.2016.
 */
public class InMemoryDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManager();
    }

    @Before
    public void setup() {
        manager = new InMemoryDatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");
    }


}