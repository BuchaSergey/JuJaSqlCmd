package ua.com.juja.sqlcmd.integration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd.Main;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PostgreSQLManager;
import ua.com.juja.sqlcmd.model.PropertiesLoader;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class IntegrationTest {

    private final static String TABLE_NAME = "test";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " pass VARCHAR (50) NOT NULL)";
    private final static String DB_NAME2 = "database2";
    private final static String TABLE_NAME2 = "qwe";
    private final static String SQL_CREATE_TABLE2 = TABLE_NAME2 + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " pass VARCHAR (50) NOT NULL)";

    private static DatabaseManager manager;
    private final static PropertiesLoader PL = new PropertiesLoader();
    private final static String DB_USER = PL.getUserName();
    private final static String DB_PASSWORD = PL.getPassword();
    private final static String DB_NAME = "database1";
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);

        manager.createDatabase(DB_NAME);
        manager.createDatabase(DB_NAME2);

        manager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);
        manager.disconnectFromDB();

        manager.connect(DB_NAME2, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE2);
        manager.disconnectFromDB();
    }

    @AfterClass
    public static void clearAfterAllTests() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDB(DB_NAME);
        manager.dropDB(DB_NAME2);
    }

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Hello user!\n" +
                        "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                        "======== SQLCmd  Help ======== \n" +
                        "\n" +
                        "Существующие команды:\n" +
                        "\n" +
                        "==============================\n" +
                        "\u001B[34m\tconnect|database|userName|password\n" +
                        "\u001B[0m\t\tconnect to specific database\n" +
                        "\u001B[34m\tcreateDB|databaseName\n" +
                        "\u001B[0m\t\tcreate new database\n" +
                        "\u001B[34m\tcreateTable|tableName\n" +
                        "\u001B[0m\t\tcreate the table in current database (id SERIAL PRIMARY KEY, username text, password text)\n" +
                        "\u001B[34m\tclear|tableName\n" +
                        "\u001B[0m\t\tclear table data\n" +
                        "\u001B[34m\tcreateEntry|tableName|column1|value1|column2|value2|...|columnN|valueN\n" +
                        "\u001B[0m\t\tcreate entry in specific table\n" +
                        "\u001B[34m\ttables\n" +
                        "\u001B[0m\t\tdisplay list of tables in the database\n" +
                        "\u001B[34m\tshow|tableName\n" +
                        "\u001B[0m\t\tdisplay table data\n" +
                        "\u001B[34m\tdatabases\n" +
                        "\u001B[0m\t\tdisplay list of databases\n" +
                        "\u001B[34m\tdropDB|databaseName\n" +
                        "\u001B[0m\t\tdelete database\n" +
                        "\u001B[34m\tdisconnect\n" +
                        "\u001B[0m\t\tdisconnect from database\n" +
                        "\u001B[34m\tdropTable|tableName\n" +
                        "\u001B[0m\t\tdelete table\n" +
                        "\u001B[34m\thelp\n" +
                        "\u001B[0m\t\tdisplay list of existing commands\n" +
                        "\u001B[34m\texit\n" +
                        "\u001B[0m\t\texit from app\n" +
                        "==============================\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "Good Bye!\n", getData());
    }

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r\n", "\n");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testExit() {
        // given
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                "Good Bye!\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                "Failure! because: null\n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "Good Bye!\n", getData());
    }

    @Test
    public void testShowTableWithoutConnect() {
        // given
        in.add("show|" + TABLE_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                "Failure! because: null\n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "Good Bye!\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                // unsupported
                "Failure! because: null\n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // exit
                "Good Bye!\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("unsupported");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                // connect
                "Connection to database '" + DB_NAME + "' is successful!\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // unsupported
                "Failure! because: null\n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "disconnected\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // exit
                "Good Bye!\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                "Connection to database 'database1' is successful!\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "[test]\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "disconnected\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "Good Bye!\n", getData());
    }

    @Test
    public void testBadConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD + "WRONG");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                "Failure! because: Check the correctness of the connect data, \n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "Good Bye!\n", getData());
    }

    @Test
    public void testShowTableAfterClear() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("clear|" + TABLE_NAME);
        in.add("y");
        in.add("createEntry|" + TABLE_NAME + "|id|1111|name|serge|pass|****");
        in.add("show|" + TABLE_NAME);
        in.add("clear|" + TABLE_NAME);
        in.add("y");
        in.add("show|" + TABLE_NAME);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Hello user!\n" +
                        "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                        "Connection to database 'database1' is successful!\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "\u001B[31mDo you want delete data from table 'test'? Y/N\u001B[0m\n" +
                        "Table test was successful cleared.\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "An entry {id=1111, name=serge, pass=****} is created successfully in the table 'test'.\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "+----+-----+----+\n" +
                        "|id  |name |pass|\n" +
                        "+----+-----+----+\n" +
                        "|1111|serge|****|\n" +
                        "+----+-----+----+\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "\u001B[31mDo you want delete data from table 'test'? Y/N\u001B[0m\n" +
                        "Table test was successful cleared.\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "+--+----+----+\n" +
                        "|id|name|pass|\n" +
                        "+--+----+----+\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "disconnected\n" +
                        "Enter the command (or \"help\" for tips):\n" +
                        "Good Bye!\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");

        in.add("connect|" + DB_NAME2 + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                // connect sqlcmd
                "Connection to database '" + DB_NAME + "' is successful!\n" +
                "Enter the command (or \"help\" for tips):\n" +
                //tables
                "[" + TABLE_NAME + "]\n" +
                "Enter the command (or \"help\" for tips):\n" +
                //disconnect
                "disconnected\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // connect test
                "Connection to database '" + DB_NAME2 + "' is successful!\n" +
                "Enter the command (or \"help\" for tips):\n" +
                //tables
                "[" + TABLE_NAME2 + "]\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "disconnected\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // exit
                "Good Bye!\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|" + DB_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                // connect sqlcmd
                "Failure! because: Incorrect number of parameters separated by '|', expected 4 but was: 2\n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // exit
                "Good Bye!\n", getData());
    }

    @Test
    public void testCreateRowInTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("clear|" + TABLE_NAME);
        in.add("y");
        in.add("createEntry|" + TABLE_NAME + "|id|13|name|Stiven|pass|*****");
        in.add("createEntry|" + TABLE_NAME + "|id|14|name|Pupkin|pass|+++++");
        in.add("show|" + TABLE_NAME);
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                // connect
                "Connection to database '" + DB_NAME + "' is successful!\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // clear|user
                "\u001B[31mDo you want delete data from table '" + TABLE_NAME + "'? Y/N\u001B[0m\n" +
                "Table " + TABLE_NAME + " was successful cleared.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "An entry {id=13, name=Stiven, pass=*****} is created successfully in the table '" + TABLE_NAME + "'.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "An entry {id=14, name=Pupkin, pass=+++++} is created successfully in the table '" + TABLE_NAME + "'.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "+--+------+-----+\n" +
                "|id|name  |pass |\n" +
                "+--+------+-----+\n" +
                "|13|Stiven|*****|\n" +
                "+--+------+-----+\n" +
                "|14|Pupkin|+++++|\n" +
                "+--+------+-----+\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "disconnected\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "Good Bye!\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("clear|wrong|param|test");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                // connect
                "Connection to database '" + DB_NAME + "' is successful!\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // clear|wrong|param|test
                "Failure! because: Incorrect number of parameters separated by '|', expected 2 but was: 4\n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "disconnected\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // exit
                "Good Bye!\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("createTable|" + TABLE_NAME + "|error");
        in.add("disconnect");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\n" +
                "Please, enter the name of database, user name and password in format: connect|database|userName|password\n" +
                // connect
                "Connection to database '" + DB_NAME + "' is successful!\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // create|user|error
                "Failure! because: Incorrect number of parameters separated by '|', expected 2 but was: 3\n" +
                "Try again.\n" +
                "Enter the command (or \"help\" for tips):\n" +
                "disconnected\n" +
                "Enter the command (or \"help\" for tips):\n" +
                // exit
                "Good Bye!\n", getData());
    }
}
