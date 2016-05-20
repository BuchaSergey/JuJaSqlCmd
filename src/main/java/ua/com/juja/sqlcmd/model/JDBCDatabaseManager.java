package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by indigo on 21.08.2015.
 */
public class JDBCDatabaseManager implements DatabaseManager {

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/";
    private Connection connection;

    @Override
    public DataSet[] getTableData(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName)) {
            int size = getSize(tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            DataSet[] result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    @Override
    public String[] getTableNames() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables" +
                     " WHERE table_schema='public' AND table_type='BASE TABLE'");) {
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public void connect(String database, String userName, String password) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Добавь к проекту jdbc.jar ", e);
        }

        connection = DriverManager.getConnection(
                DATABASE_URL + database, userName,
                password);

    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement();) {
            stmt.executeUpdate("DELETE FROM public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try(Statement stmt = connection.createStatement();) {


            String tableNames = getNameFormated(input, "%s,");
            String values = getValuesFormated(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                    "VALUES (" + values + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public String[] getTableColumns(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM information_schema.columns " +
                     "WHERE table_schema = 'public' AND table_name = '" + tableName + "'")) {
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("column_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
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
