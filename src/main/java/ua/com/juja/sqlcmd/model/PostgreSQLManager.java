package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.*;

/**
 * Created by indigo on 21.08.2015.
 */
public class PostgreSQLManager implements DatabaseManager {

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/";
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
    public List<DataSet> getTableData(String tableName) {
        List<DataSet> result = new LinkedList();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                DataSet dataSet = new DataSetImpl();
                result.add(dataSet);
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i + 1), rs.getObject(i + 1));
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public int getSize(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName)) {
            rsCount.next();
            int size = rsCount.getInt(1);
            return size;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
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
            e.printStackTrace();
            return tables;
        }
    }

    @Override
    public void connect(String database, String userName, String password) {

        try {
            connection = DriverManager.getConnection(
                    DATABASE_URL + database, userName,
                    password);
        } catch (SQLException e) {
            throw new RuntimeException("Проверьте правильность введенных данных, " + e.getMessage());
        }

    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate("DELETE FROM " + tableName);
        } catch (SQLException e) {
            throw new RuntimeException("Неправильное имя таблицы. ");
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try (Statement stmt = connection.createStatement();) {
            String tableNames = getNameFormated(input, "%s,");
            String values = getValuesFormated(input, "'%s',");
            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                    "VALUES (" + values + ")");
        } catch (SQLException e) {
            throw new RuntimeException("Проверьте правильность введенных данных, " + e.getMessage());        }
    }

    private String getValuesFormated(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        try {
            String tableNames = getNameFormated(newValue, "%s = ?,");
            String sql = "UPDATE public." + tableName + " SET " + tableNames + " WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            int index = 1;
            for (Object value : newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setInt(index, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
            return tables;
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    private String getNameFormated(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}
