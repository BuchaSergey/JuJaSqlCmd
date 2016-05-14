package ua.com.juja.sqlcmd.model;

import java.util.Arrays;

/**
 * Created by Серый on 13.05.2016.
 */
public class InMemoryDatabaseManager implements  DatabaseManager {

    public static final String TABLE_NAME = "user, test"; // TODO implement multitables
    private DataSet[] data = new DataSet[1000];
    private  int freeIndex = 0;

    @Override
    public DataSet[] getTableData(String tableName) {
        validateTable(tableName);
        return Arrays.copyOf(data, freeIndex);// TODO without null
    }

    private void validateTable(String tableName) {
        if (!"user".equals(tableName)) {
            throw  new UnsupportedOperationException("Only for 'user', but you try to work with: " + tableName);
        }
    }

    @Override
    public String[] getTableNames() {
        return new String[] {TABLE_NAME};
    }

    @Override
    public void connect(String database, String userName, String password) {
        //do nothing
    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);
        data = new DataSet[1000];
        freeIndex = 0;
    }



    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);
        data[freeIndex] = input;
        freeIndex++;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        for (int index = 0; index < freeIndex; index++) {
            if(data[freeIndex].get("id") == id ) {
                data[index].updateFrom(newValue);
            }
        }
    }

    @Override
    public String[] getTableColumns(String tableName) {
        return new String[] {"name", "password" , "id"};
    }
}
