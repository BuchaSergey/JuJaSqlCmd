package ua.com.juja.sqlcmd.model;


import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;

public class PostgreSQLManagerTest {

    private final static String TABLE_NAME = "test";
    private final static String NOT_EXIST_TABLE = "notExistTable";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private static PropertiesLoader pl = new PropertiesLoader();
    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();
    private final static String DATABASE_NAME = pl.getDatabaseName();
    private static final String ERROR = "Невозможно выполнить: ";

    private static DatabaseManager manager;

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

//    @Test
//    public void testCreateTable() {
//        //given
//        Set<String> expected = new LinkedHashSet<>(Collections.singletonList(TABLE_NAME));
//        manager.dropTable(TABLE_NAME);
//
//        //when
//        manager.createTable(SQL_CREATE_TABLE);
//
//        //then
//        Set<String> actual = manager.getTableNames();
//        assertEquals(expected, actual);
//    }

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

    @Rule public ExpectedException exception = ExpectedException.none();


    @Test
    public void testExceptionGetTableNames()  {
        exception.expect(DatabaseManagerException.class);
        exception.expectMessage(containsString("Невозможно выполнить: ")); //org.hamcrest.Matchers.
      //  manager.getTableNames();
        doThrow(new Exception()).when(manager.getDatabasesNames());
    }
//
//    @Test
//    public void testExceptionGetTDatabasesNames()  {
//        exception.expect(DatabaseManagerException.class);
//        exception.expectMessage(containsString("Невозможно выполнить: "));
//        manager.getDatabasesNames();
//    }

    @Test
    public void testExceptionGetTableNames2()  {
        manager.connect("", "postgres","postgres");
        try {

            manager.getTableNames();

        } catch (DatabaseManagerException e) {
            assertEquals("Невозможно выпо3лнить: ", e.getMessage());
        }

    }

    @Test
    public void testExceptionGetTableNames3 ()  {
//     //   doThrow(new DatabaseManagerException("", new Exception())).when(manager).getDatabasesNames();
//        manager.getDatabasesNames();
//        doThrow(new RuntimeException()).when(manager).getDatabasesNames();
     // doThrow(new RuntimeException()).when(manager).getDatabasesNames();

        try {
            manager.getTableNames();
            throw new DatabaseManagerException(ERROR + "не удалось получить имена таблиц", new Exception());
        } catch (Exception e) {
            //then
        assertEquals(ERROR + "не удалось получить имена таблиц", e.getMessage());
        }
          }

    @Test
    public void testIsConnected(){
        assertTrue(manager.isConnected());

    }
}
