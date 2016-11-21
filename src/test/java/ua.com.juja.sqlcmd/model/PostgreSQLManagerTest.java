package ua.com.juja.sqlcmd.model;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;


@RunWith(MockitoJUnitRunner.class)

public class PostgreSQLManagerTest {

    private final static String TABLE_NAME = "test";
    private final static String NOT_EXIST_TABLE = "notExistTable";
    private final static String DATABASE_NAME = "database1";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY, username text, password text)";
    private final static PropertiesLoader PROPERTIES_LOADER = new PropertiesLoader();
    private final static String DB_USER = PROPERTIES_LOADER.getUserName();
    private final static String DB_PASSWORD = PROPERTIES_LOADER.getPassword();
    private final static String DB_NAME_FROM_PROPERTIES = PROPERTIES_LOADER.getDatabaseName();
    private static DatabaseManager manager;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect(DB_NAME_FROM_PROPERTIES, DB_USER, DB_PASSWORD);
        manager.createDatabase(DATABASE_NAME);
        manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDB(DATABASE_NAME);
    }

    @Mock
    private Connection mockConn;

    @InjectMocks
    private PostgreSQLManager managerInjected;

    @Before
    public void setup() throws Exception {
        manager.createTable(SQL_CREATE_TABLE);
    }

    @After
    public void clear() {
        manager.dropTable(TABLE_NAME);
    }

    @Test
    public void testClear() {
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

    @Test(expected = DatabaseManagerException.class)
    public void testClearNotExistTable() {
        //when
        manager.clear(NOT_EXIST_TABLE);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testConnectToNotExistDatabase() {
        //when
        try {
            manager.connect(NOT_EXIST_TABLE, DB_USER, DB_PASSWORD);
            fail();
        } catch (Exception e) {
            //then
            manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
            throw e;
        }
    }

    @Test(expected = DatabaseManagerException.class)
    public void testConnectToDatabaseWhenIncorrectUserAndPassword() {
        //when
        try {
            manager.connect(DATABASE_NAME, "notExistUser", "badPassword");
            fail();
        } catch (Exception e) {
            //then
            manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
            throw e;
        }
    }

    @Test(expected = DatabaseManagerException.class)
    public void testConnectToServerWhenIncorrectUserAndPassword() {
        //when
        try {
            manager.connect("", "notExistUser", "randomPass");
        } catch (Exception e) {
            //then
            manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
            throw e;
        }
    }

    @Test(expected = DatabaseManagerException.class)
    public void testCreateTableWrongQuery() {
        //given
        String query = "testTable(qwerty)";

        //when
        manager.createTable(query);
    }

    @Test
    public void testDropDatabase() {
        //given
        String newDatabase = "dropdatabasetest";
        manager.createDatabase(newDatabase);

        //when
        manager.dropDB(newDatabase);

        //then
        Set<String> databases = manager.getDatabasesNames();
        if (databases.contains(newDatabase)) {
            fail();
        }
    }

    @Test
    public void testDropTable() {
        //given
        String tableName = "secondTable";
        Set<String> expected = new LinkedHashSet<>(Collections.singletonList(TABLE_NAME));
        manager.createTable(tableName + "(id serial PRIMARY KEY)");

        //when
        manager.dropTable(tableName);

        //then
        Set<String> actual = manager.getTableNames();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDatabases() {
        //given
        //when
        Set<String> actual = manager.getDatabasesNames();

        //then
        assertNotNull(actual);
    }

    @Test
    public void testGetTableColumns() {
        //given
        Set<String> expected = new LinkedHashSet<>(Arrays.asList("id", "username", "password"));

        //when
        Set<String> actual = manager.getTableColumns(TABLE_NAME);

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTableNames() {
        //given
        Set<String> expected = new LinkedHashSet<>(Collections.singletonList(TABLE_NAME));

        //when
        Set<String> actual = manager.getTableNames();

        //then
        assertEquals(expected, actual);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testInsertNotExistTable() {
        //given
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "Bob");
        newData.put("password", "*****");
        newData.put("id", 1);

        //when
        //then
        manager.createEntry(NOT_EXIST_TABLE, newData);
    }

    @Test
    public void testInsertWithId() {
        //given
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "Bob");
        newData.put("password", "*****");
        newData.put("id", 1);

        //when
        manager.createEntry(TABLE_NAME, newData);

        //then
        Map<String, Object> user = manager.getTableData(TABLE_NAME).get(0);
        assertEquals(newData, user);
    }

    @Test
    public void testUpdate() {
        //given
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put("username", "testUser");
        newData.put("password", "azerty");
        newData.put("id", 2);

        manager.createEntry(TABLE_NAME, newData);

        //when
        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("username", "Bill");
        updateData.put("password", "qwerty");
        updateData.put("id", 2);

        manager.update(TABLE_NAME, 2, updateData);

        //then
        Map<String, Object> user = manager.getTableData(TABLE_NAME).get(0);
        assertEquals(updateData, user);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testUpdateNotExistTable() {
        //when
        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("username", "Bill");
        updateData.put("password", "qwerty");
        updateData.put("id", 1);

        //then
        manager.update(NOT_EXIST_TABLE, 1, updateData);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionGetTableNames2() throws SQLException {

        doThrow(new SQLException()).when(mockConn).createStatement();
        managerInjected.getTableNames();
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionCreateDatabase() throws SQLException {

        doThrow(new SQLException()).when(mockConn).createStatement();
        managerInjected.createDatabase(DATABASE_NAME);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionDropDB() throws SQLException {

        doThrow(new SQLException()).when(mockConn).createStatement();
        managerInjected.dropDB(DATABASE_NAME);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionDropTable() throws SQLException {

        doThrow(new SQLException()).when(mockConn).createStatement();
        managerInjected.dropTable(TABLE_NAME);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testDisconnectFromDB() {
        //given
        DatabaseManager managerWithoutConnectionToDatabase = new PostgreSQLManager();
        //when
        managerWithoutConnectionToDatabase.disconnectFromDB();
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionDisconnectFromDB() throws SQLException {
        doThrow(new SQLException()).when(mockConn).close();
        managerInjected.disconnectFromDB();
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionGetTableData() throws SQLException {
        doThrow(new SQLException()).when(mockConn).createStatement();
        managerInjected.getTableData(TABLE_NAME);
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionGetTableColumns() throws SQLException {
        doThrow(new SQLException()).when(mockConn).prepareStatement(anyString());
        managerInjected.getTableColumns(TABLE_NAME);
    }
}
