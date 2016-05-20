package ua.com.juja.sqlcmd.model;

import java.sql.SQLException;

/**
 * Created by Серый on 13.05.2016.
 */
public interface DatabaseManager {

    DataSet[] getTableData(String tableName) ;

    String[] getTableNames() ;

    void connect(String database, String userName, String password) throws SQLException;

    void clear(String tableName) throws SQLException;

    void create(String tableName, DataSet input) ;

    void update(String tableName, int id, DataSet newValue) ;

    String [] getTableColumns(String tableName) ;

    boolean isConnected();
}
