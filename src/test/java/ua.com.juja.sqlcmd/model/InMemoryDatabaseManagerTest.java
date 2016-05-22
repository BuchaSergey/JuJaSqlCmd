package ua.com.juja.sqlcmd.model;

/**
 * Created by Серый on 13.05.2016.
 */
public class InMemoryDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManager();
    }
}