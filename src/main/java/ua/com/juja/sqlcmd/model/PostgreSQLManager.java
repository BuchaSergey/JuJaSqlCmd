package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.*;


public class PostgreSQLManager implements DatabaseManager {

    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static final String HOST = propertiesLoader.getServerName();
    private static final String PORT = propertiesLoader.getDatabasePort();
    private static final String DRIVER = propertiesLoader.getDriver();
    private static final String DATABASE_URL = DRIVER + HOST + ":" + PORT + "/";
    private Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            try {
                DriverManager.registerDriver(new org.postgresql.Driver());
            } catch (SQLException e1) {
                try {
                    throw new SQLException("Couldn't register driver in case -", e1);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getTableData(String tableName) {
        try (Statement statement = connection.createStatement();
             ResultSet tableData = statement.executeQuery("SELECT * FROM " + tableName)) {
            ResultSetMetaData metaData = tableData.getMetaData();

            List<Map<String, Object>> result = new LinkedList<>();
            while (tableData.next()) {
                Map<String, Object> data = new LinkedHashMap<>();
                for (int index = 1; index <= metaData.getColumnCount(); index++) {
                    data.put(metaData.getColumnName(index), tableData.getObject(index));
                }
                result.add(data);
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It's impossible to get data from the table %s", tableName), e);
        }
    }

    @Override
    public void connect(String database, String userName, String password) {

        closeOpenedConnection(connection);
        try {
            connection = DriverManager.getConnection(DATABASE_URL + database, userName, password);
        } catch (SQLException e) {
            throw new DatabaseManagerException("Check the correctness of the connect data, ", e);
        }
    }

    private void closeOpenedConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException("Can't close the connection", e);
            }
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM " + tableName);
        } catch (SQLException e) {
            throw new DatabaseManagerException("Wrong table name.", e);
        }
    }

    @Override
    public void createEntry(String tableName, Map<String, Object> input) {

        String rowNames = getFormattedName(input, "\"%s\",");
        String values = getFormattedValues(input);
        String query = String.format("INSERT INTO %s  (%s) VALUES (%s)",tableName,rowNames,values);

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It's impossible to create a row of data in table %s", tableName), e);
        }
    }

    private String getFormattedName(Map<String, Object> newValue, String format) {
        String string = "";
        for (String name : newValue.keySet()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getFormattedValues(Map<String, Object> input) {
        String values = "";
        for (Object value : input.values()) {
            values += String.format("'%s',", value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public void createDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + databaseName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("Cannot create databases %s", databaseName), e);
        }
    }

    @Override
    public void createTable(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + query);
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("Cannot create table %s", query), e);
        }
    }

    @Override
    public void dropDB(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + databaseName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(
                    String.format("Cannot delete database %s", databaseName), e);
        }
    }

    @Override
    public void dropTable(String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(
                    String.format("Cannot delete table: %s", tableName), e);
        }
    }

    @Override
    public void disconnectFromDB() {
        if (connection == null) {
            throw new DatabaseManagerException(
                    "First do connect to database", new Exception());
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseManagerException(
                    "Can't close connection in disconnectFromDB(): ", e);
        }
        connection = null;
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void update(String tableName, int id, Map<String, Object> newValue) {
        String tableNames = getFormattedName(newValue, "\"%s\" = ?,");
        String query = String.format("UPDATE  %s SET  %s WHERE id = ?",tableName,tableNames);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int index = 1;
            for (Object value : newValue.values()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setObject(index, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It's impossible update row in table %s", tableName), e);
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new HashSet<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables" +
                     " WHERE table_schema='public' AND table_type='BASE TABLE'")) {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new DatabaseManagerException("Failed to get table names", e);
        }
    }

    @Override
    public Set<String> getDatabasesNames() {
        if (connection == null) {
            throw new DatabaseManagerException(
                    "Need to connect to database", new Exception());
        }
        String query = "SELECT datname FROM pg_database WHERE datistemplate = false;";
        try (Statement ps = connection.createStatement();
             ResultSet rs = ps.executeQuery(query)) {
            Set<String> result = new LinkedHashSet<>();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseManagerException("Failed to get the names of the database", e);
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        String query = "SELECT * FROM information_schema.columns WHERE table_schema = 'public' AND table_name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setObject(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                Set<String> result = new LinkedHashSet<>();
                while (rs.next()) {
                    result.add(rs.getString("column_name"));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException(String.format("It's impossible to get column's name from table %s", tableName), e);
        }
    }
}
