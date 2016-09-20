package ua.com.juja.sqlcmd.model;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PostgreSQLManagerTest  {

    private static PropertiesLoader pl = new PropertiesLoader();

    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();
    private final static String DB_NAME = pl.getDatabaseName();
    private final static String TABLE_NAME = "user";
    private final static String TABLE_NAME2 = "test";
    private final static String NOT_EXIST_TABLE = "notExistTable";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " pass VARCHAR (50) NOT NULL)";
        private static DatabaseManager manager;


    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);

        manager.createDatabase(DB_NAME);
        manager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);

        manager.disconnectFromDB();
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDB(DB_NAME);
    }

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
    }

        @Test
        public void testGetAllTableNames() {
            Set<String> tablesNames = manager.getTableNames();

            assertEquals("[" + TABLE_NAME + ", "   + TABLE_NAME2 + "]", tablesNames.toString());
        }

        @Test
        public void testGetTableData() {
            //given
            List<Map<String, Object>> expected = new ArrayList<>();

            Map<String, Object> newData = new LinkedHashMap<>();
            newData.put("username", "Bob");
            newData.put("password", "*****");
            newData.put("id", 1);
            manager.createEntry(TABLE_NAME, newData);

            //when
            manager.clear(TABLE_NAME);
            List<Map<String, Object>> tests = manager.getTableData(TABLE_NAME);

            //then
            assertEquals(expected, tests);
        }

        @Test
        public void testUpdateTableData() {
            //given
            manager.clear(TABLE_NAME);

            Map<String, Object> input = new LinkedHashMap<>();
            input.put("name", "Stiven");
            input.put("password", "pass");
            input.put("id", 13);
            manager.createEntry(TABLE_NAME, input);

            //when
            Map<String, Object> newValue = new LinkedHashMap<>();
            newValue.put("password", "pass2");
            newValue.put("name", "Pup");

            manager.update(TABLE_NAME, 1, newValue);

            //then
            Map<String, Object> user = manager.getTableData(TABLE_NAME).get(0);
            assertEquals(newValue, user);
        }

        @Test
        public void testGetColumnNames() {
            //given
            manager.clear(TABLE_NAME);
            //when
            Set<String> columns = manager.getTableColumns(TABLE_NAME);
            //then
            assertEquals("[name, password, id]", columns.toString());
        }
    }

