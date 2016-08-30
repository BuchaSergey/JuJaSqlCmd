package ua.com.juja.sqlcmd.model;

import java.util.List;
import java.util.Set;


public interface DatabaseManager {

    void connect(String database, String userName, String password);
    boolean isConnected();

    List<DataSet> getTableData(String tableName);
    Set<String> getTableNames();
    Set<String> getTableColumns(String tableName);

    void createDatabase(String databaseName);
    void createTable(String tableName);
    void deleteTable(String tableName);
    void deleteDatabase(String databaseName);
    void clear(String tableName);
    void create(String tableName, DataSet input);
    void update(String tableName, int id, DataSet newValue);
}
