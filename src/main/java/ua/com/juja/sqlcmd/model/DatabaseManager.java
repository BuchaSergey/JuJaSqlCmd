package ua.com.juja.sqlcmd.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Created by Серый on 13.05.2016.
 */
public interface DatabaseManager {

    List<DataSet> getTableData(String tableName) ;

    Set<String> getTableNames() ;

    void connect(String database, String userName, String password) throws SQLException;

    void clear(String tableName) throws SQLException;

    void create(String tableName, DataSet input) ;

    void update(String tableName, int id, DataSet newValue) ;

    Set<String> getTableColumns(String tableName) ;

    boolean isConnected();
}
