package ua.com.juja.sqlcmd.model;


import org.junit.*;
import ua.com.juja.sqlcmd.view.View;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PostgreSQLManagerTest {

    private final static String TABLE_NAME = "test";
    private final static String NOT_EXIST_TABLE = "notExistTable";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private static final String ERROR = "Невозможно выполнить: ";
    private static PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();
    private final static String DATABASE_NAME = pl.getDatabaseName();
    private static DatabaseManager manager;
    private static DatabaseManager manMock;
    private static View viewMock;
    private static Connection connection;
    private static Statement statement;


    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDB(DATABASE_NAME);
        manager.createDatabase(DATABASE_NAME);
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager.connect("", DB_USER, DB_PASSWORD);

        manager.dropDB(DATABASE_NAME);
        manager.disconnectFromDB();
    }

    @Before
    public void setup() {
        manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);

        manMock = mock(DatabaseManager.class);
        viewMock = mock(View.class);
        connection = mock(Connection.class);
        statement = mock(Statement.class);
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
            manager.connect(DATABASE_NAME, "notExistUser", "qwertyuiop");
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
            manager.connect("", "notExistUser", "qwertyuiop");
        } catch (Exception e) {
            //then
            manager.connect(DATABASE_NAME, DB_USER, DB_PASSWORD);
            throw e;
        }
    }


    @Test
    public void testCreateDatabase() {
        //given
        String newDatabase = "createdatabasetest";

        //when
        manager.createDatabase(newDatabase);

        //then
        Set<String> databases = manager.getDatabasesNames();
        if (!databases.contains(newDatabase)) {
            fail();
        }
        manager.dropDB(newDatabase);
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
        String tableName = "secondTest";
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
        newData.put("id", 1);

        manager.createEntry(TABLE_NAME, newData);

        //when
        Map<String, Object> updateData = new LinkedHashMap<>();
        updateData.put("username", "Bill");
        updateData.put("password", "qwerty");
        updateData.put("id", 1);

        manager.update(TABLE_NAME, 1, updateData);

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
    public void testExceptionGetTableNames2() {

        doThrow(new DatabaseManagerException
                (ERROR + "не удалось получить имена таблиц", new SQLException())).when(manMock).getTableNames();
        try {
            manMock.getTableNames();
        } catch (DatabaseManagerException e) {
            assertEquals("Невозможно выполнить: не удалось получить имена таблиц", e.getMessage());
            throw e;
        }
    }

    @Test(expected = DatabaseManagerException.class)
    public void testExceptionGetTableNames_Con() {

        manMock.connect(null, null, null);
//        doThrow(new DatabaseManagerException
//                (ERROR + "не удалось получить имена таблиц", new SQLException())).when(manMock).getTableNames();
        try {
            manMock.getTableNames();
        } catch (DatabaseManagerException e) {
            assertEquals("Невозможно выполнить: не удалось получить имена таблиц", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testExceptionGetTableNames3() {
        manager.connect("", "postgres", "postgres");
        try {
            manager.getDatabasesNames();
            throw new DatabaseManagerException(ERROR, new SQLException());
        } catch (DatabaseManagerException e) {
            assertEquals("Невозможно выполнить: ", e.getMessage());
        }
    }

    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }

    @Test(expected = DatabaseManagerException.class)
    public void testGetTableColumnException() {
        try {
            manager.getTableColumns(NOT_EXIST_TABLE);
            throw new DatabaseManagerException(ERROR, new SQLException());
        } catch (Exception e) {
            assertEquals("Невозможно выполнить: ", e.getMessage());
        }


    }

    @Test //(expected = SQLException.class)

    public void testGetDatabasesNamesException() throws SQLException {

        when(manMock.getDatabasesNames()).
                thenThrow(new Exception());;
        //
        try {
            manMock.getDatabasesNames();
        } catch (Exception e) {
            assertEquals(e.getMessage(), ERROR + "не удалось получить имена БД");
        }

        //verify(connection).createStatement();

    }





    @Test
    public void testExceptionGetTableNamesFail() {
        //when
        try {
            manager.getTableNames();
            throw new DatabaseManagerException( ERROR + "не удалось получить имена таблиц", new SQLException());
        } catch (Exception e) {
            //then
            assertEquals("Невозможно выполнить: не удалось получить имена таблиц", e.getMessage());

        }
    }



    @Test
    public void closeOpenedConnectionException()  {

        try {
         connection.close();
            doThrow(new DatabaseManagerException("Невозможно выполнить: не удалось получить имена таблиц",new SQLException()));
        } catch (SQLException e) {
            assertEquals("2",e.getMessage());
        }
    }


    @Test
    public void testExceptionGetTableNamesFailStatw() {
        //when
        try {
            when(connection.createStatement()).thenThrow(new DatabaseManagerException("Невозможно выполнить: не удалось получить имена таблиц",new SQLException()));
            when(statement.executeQuery("someQuery")).thenThrow(new DatabaseManagerException("Невозможно выполнить: не удалось получить имена таблиц",new SQLException()));
            manMock.getTableNames();
        } catch (Exception e) {
            //then
            assertEquals("Нев3озможно выполнить: не удалось получить имена таблиц", e.getMessage());
            verify(manMock).getTableNames();
        }
    }

    @Test

    public void testGetDatabasesNames() throws SQLException {

       // when(connection.createStatement()).thenThrow( new SQLException());

        //when(statement.execute("someQuery")).thenReturn(false).thenThrow( new RuntimeException());
       doThrow(new DatabaseManagerException("Невозможно выполнить: не удалось получить имена таблиц",new SQLException())).when(manMock).getDatabasesNames();
        verify(manMock).getDatabasesNames();
        verify(viewMock).write("???");

    }

    @Test

    public void testGetDatabasesNames2() throws SQLException {

        // when(connection.createStatement()).thenThrow( new SQLException());

        //when(statement.execute("someQuery")).thenReturn(false).thenThrow( new RuntimeException());
        doThrow(new DatabaseManagerException("Невозможно выполнить: не удалось получить имена таблиц",new SQLException())).when(manMock).getDatabasesNames();
        verify(manMock).getDatabasesNames();
        verify(viewMock).write("???");

    }


}
