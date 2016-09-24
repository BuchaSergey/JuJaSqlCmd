package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.*;


public class PostgreSQLManager implements DatabaseManager {


    private static final String ERROR = "Невозможно выполнить: ";
    static PropertiesLoader propertiesLoader = new PropertiesLoader();
    private static final String HOST = propertiesLoader.getServerName();
    private static final String PORT = propertiesLoader.getDatabasePort();
    private static final String DRIVER = propertiesLoader.getDriver();
    private static final String DATABASE_URL = DRIVER + HOST + ":" + PORT + "/";
    private static final String USER_NAME = propertiesLoader.getUserName();
    private static final String PASSWORD = propertiesLoader.getPassword();

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

    private Connection connection;

    @Override
    public List<Map<String, Object>> getTableData(String tableName) {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName))
        {
            ResultSetMetaData rsmd = rs.getMetaData();

            List<Map<String, Object>> result = new LinkedList<>();
            while (rs.next()) {
                Map<String, Object> data = new LinkedHashMap<>();
                for (int index = 1; index <= rsmd.getColumnCount(); index++) {
                    data.put(rsmd.getColumnName(index), rs.getObject(index));
                }
                result.add(data);
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseManagerException(ERROR, e);
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new LinkedHashSet();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables" +
                     " WHERE table_schema='public' AND table_type='BASE TABLE'");) {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new DatabaseManagerException( ERROR + "не удалось получить имена таблиц", e);
        }
    }

    @Override
    public void connect(String database, String userName, String password) {

        closeOpenedConnection(connection);
        try {
            connection = DriverManager.getConnection(DATABASE_URL + database, userName, password);
        } catch (SQLException e) {
            throw new DatabaseManagerException("Проверьте правильность введенных данных, ", e);
        }
    }

    private void closeOpenedConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException("Не могу закрыть connection", e);
            }
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate("DELETE FROM " + tableName);
        } catch (SQLException e) {
            throw new DatabaseManagerException("Неправильное имя таблицы.", e);
        }
    }

    @Override
    public void createEntry(String tableName, Map<String, Object> input) {

        String rowNames = getFormatedName(input, "\"%s\",");
        String values = getFormatedValues(input, "'%s',");
        String sql = "INSERT INTO " + tableName + " (" + rowNames + ") " + "VALUES (" +  values + ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException(ERROR, e);
        }
    }

    private String getFormatedName(Map<String, Object> newValue, String format) {
        String string = "";
        for (String name : newValue.keySet()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }

    private String getFormatedValues(Map<String, Object> input, String format) {
        String values = "";
        for (Object value : input.values()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }




    @Override
    public void createDatabase(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + databaseName);
        } catch (SQLException e) {
            throw new DatabaseManagerException("Не могу создать БД " + databaseName, e);
        }

    }

    @Override
    public void createTable(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + query);
        } catch (SQLException e) {
            throw new DatabaseManagerException("Не могу создать таблицу из запроса ", e);
        }
    }

    @Override
    public void dropDB(String databaseName) {

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + databaseName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(
                    String.format("Не могу удалить БД: %s", databaseName), e);
        }
    }

    @Override
    public void dropTable(String tableName) {

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
        } catch (SQLException e) {
            throw new DatabaseManagerException(
                    String.format("Не могу удалить таблицу: %s", tableName), e);
        }
    }

    @Override
    public void disconnectFromDB() {
        connection = null;
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> tables = new LinkedHashSet();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.columns " +
                     "WHERE table_schema = 'public' AND table_name = '" + tableName + "'")) {
            while (rs.next()) {
                tables.add(rs.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new DatabaseManagerException(ERROR, e);
        }
    }

    @Override
    public Set<String> getDatabasesNames() {
        connect("", USER_NAME, PASSWORD);
        String sql = "SELECT datname FROM pg_database WHERE datistemplate = false;";
        try (Statement ps = connection.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            Set<String> result = new LinkedHashSet<>();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseManagerException(ERROR + "не удалось получить имена БД", e);
        }
    }


    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void update(String tableName, int id, Map<String, Object> newValue) {
        String tableNames = getFormatedName(newValue, "\"%s\" = ?,");
        String sql = "UPDATE " + tableName +  " SET " +  tableNames + " WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int index = 1;
            for (Object value : newValue.values()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setObject(index, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseManagerException(ERROR, e);
        }
    }
}
