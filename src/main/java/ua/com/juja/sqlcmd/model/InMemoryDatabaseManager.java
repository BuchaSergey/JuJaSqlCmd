package ua.com.juja.sqlcmd.model;

import java.util.*;

public class InMemoryDatabaseManager implements DatabaseManager {

    private Map<String, List<DataSet>> tables = new LinkedHashMap();

    @Override
    public List<DataSet> getTableData(String tableName) {
        return get(tableName);
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public void connect(String database, String userName, String password) {
        // do nothing
    }

    @Override
    public void clear(String tableName) {
        get(tableName).clear();
    }

    private List<DataSet> get(String tableName) {
        if (!tables.containsKey(tableName)) {
            return tables.put(tableName, new LinkedList<DataSet>());
        }
        return tables.get(tableName);
    }

    @Override
    public void createEntry(String tableName, DataSet input) {
        get(tableName).add(input);
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        for (DataSet dataSet : get(tableName)) {
            if (dataSet.get("id").equals(id) ) {
                dataSet.updateFrom(newValue);
            }
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<String>(Arrays.asList("name", "password", "id"));
    }

    @Override
    public Set<String> getDatabasesNames() {
        return null;
    }

    @Override
    public void createDatabase(String databaseName) {

    }

    @Override
    public void createTable(String tableName) {

    }

    @Override
    public void disconnectFromDB() {

    }

    @Override
    public void dropTable(String tableName) {

    }

    @Override
    public void dropDB(String databaseName) {

    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
