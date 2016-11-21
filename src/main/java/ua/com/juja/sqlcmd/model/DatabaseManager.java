package ua.com.juja.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface DatabaseManager {

    void connect(String database, String userName, String password);

    boolean isConnected();

    List<Map<String, Object>> getTableData(String tableName);

    Set<String> getTableNames();

    Set<String> getTableColumns(String tableName);

    Set<String> getDatabasesNames();

    void createDatabase(String databaseName);

    void disconnectFromDB();

    void dropDB(String databaseName);

    void createTable(String query);

    void dropTable(String tableName);

    void clear(String tableName);

    void createEntry(String tableName, Map<String, Object> input);

    void update(String tableName, int id, Map<String, Object> newValue);
}
