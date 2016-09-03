package ua.com.juja.sqlcmd.integration;

import org.junit.*;
import ua.com.juja.sqlcmd.controller.Main;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PostgreSQLManager;
import ua.com.juja.sqlcmd.model.PropertiesLoader;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private static DatabaseManager manager;
    private static PropertiesLoader pl = new PropertiesLoader();

    private final static String DB_USER = pl.getUserName();
    private final static String DB_PASSWORD = pl.getPassword();
    private final static String DB_NAME = pl.getDatabaseName();
    private final static String TABLE_NAME = "test";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " pass VARCHAR (50) NOT NULL)";

    private final static String DB_NAME2 = "test";
    private final static String TABLE_NAME2 = "qwe";
    private final static String SQL_CREATE_TABLE2 = TABLE_NAME2 + " (id SERIAL PRIMARY KEY," +
            " name VARCHAR (50) UNIQUE NOT NULL," +
            " pass VARCHAR (50) NOT NULL)";



    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

    }

    @BeforeClass
    public static void init() {
        manager = new PostgreSQLManager();
        manager.connect("", DB_USER, DB_PASSWORD);
        manager.dropDB(DB_NAME);
        manager.dropDB(DB_NAME2);

        manager.createDatabase(DB_NAME);
        manager.createDatabase(DB_NAME2);

        manager.connect(DB_NAME, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE);

        manager.connect(DB_NAME2, DB_USER, DB_PASSWORD);
        manager.createTable(SQL_CREATE_TABLE2);
        manager.disconnectFromDB();

    }

       @AfterClass
       public static void clearAfterAllTests() {
           manager.connect(DB_NAME,DB_USER,DB_PASSWORD);
           manager.dropTable(TABLE_NAME);

           manager.connect(DB_NAME2,DB_USER,DB_PASSWORD);
           manager.dropTable(TABLE_NAME2);
           manager.disconnectFromDB();

           manager.connect("",DB_USER, DB_PASSWORD);
           manager.dropDB(DB_NAME);
           manager.dropDB(DB_NAME2);


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
                "Привет юзер!\n" +
                        "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +

                        "======== SQLCmd  Help ======== \n" +
                        "\n" +
                        "Существующие команды:\n" +
                        "\n" +
                        "==============================\n" +
                        "\u001B[34m\tconnect|databaseName|userName|password\n" +
                        "\u001B[0m\t\tдля подключения к базе данных, с которой будем работать\n" +
                        "\u001B[34m\tcreateDB|databaseName\n" +
                        "\u001B[0m\t\tдля создания база данных\n" +
                        "\u001B[34m\tdropDB|databaseName\n" +
                        "\u001B[0m\t\tдля удаления база данных\n" +
                        "\u001B[34m\ttables\n" +
                        "\u001B[0m\t\tдля получения списка всех таблиц базы, к которой подключились\n" +
                        "\u001B[34m\tcreateTable|tableName(name text, pass text, id serial, PRIMARY KEY(id)\n" +
                        "\u001B[0m\t\tдля создания таблицы. \n" +
                        "\u001B[34m\tcreateEntry|tableName|column1|value1|column2|value2|...|columnN|valueN\n" +
                        "\u001B[0m\t\tдля создания записи в таблице\n" +
                        "\u001B[34m\tclear|tableName\n" +
                        "\u001B[0m\t\tдля очистки всей таблицы\n" +
                        "\u001B[34m\tdropTable|tableName\n" +
                        "\u001B[0m\t\tдля удаления таблицы таблицы\n" +
                        "\u001B[34m\tshow|tableName\n" +
                        "\u001B[0m\t\tдля получения содержимого таблицы\n" +
                        "\u001B[34m\thelp\n" +
                        "\u001B[0m\t\tдля вывода этого списка на экран\n" +
                        "\u001B[34m\texit\n" +
                        "\u001B[0m\t\tдля выхода из программы\n" +
                        "==============================\n" +
                        "Введи команду (или help для помощи):\n" +
                        "До скорой встречи!\n"

                , getData());
    }

    public String getData() {
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
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // tables
                "Вы не можете пользоваться командой 'tables' пока не подключитесь с помощью комманды connect|databaseName|userName|password\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testShowTableWithoutConnect() {
        // given
        in.add("show|" + TABLE_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // show|user
                "Вы не можете пользоваться командой 'show|" + TABLE_NAME + "' пока не подключитесь с помощью комманды connect|databaseName|userName|password\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // unsupported
                "Вы не можете пользоваться командой 'unsupported' пока не подключитесь с помощью комманды connect|databaseName|userName|password\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // connect
                "Подключение к базе '" + DB_NAME + "' прошло успешно!\n" +
                "Введи команду (или help для помощи):\n" +
                // unsupported
                "Несуществующая команда: unsupported\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // connect
                "Подключение к базе '" + DB_NAME + "' прошло успешно!\n" +
                "Введи команду (или help для помощи):\n" +
                //tables
                "[" + TABLE_NAME + "]\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testBadConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD + "WRONG");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // badConnect
                "Неудача! по причине: Проверьте правильность введенных данных, " +
                "Ошибка при попытке подсоединения.\n" +
                "Повтори попытку.\n" +
                // exit
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testShowTableAfterClear() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("create|" + TABLE_NAME + "|id|1111|name|serge|pass|****");
        in.add("show|" + TABLE_NAME);
        in.add("clear|" + TABLE_NAME);
        in.add("y");
        in.add("show|" + TABLE_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Привет юзер!\n" +
                        "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                        "Подключение к базе '" + DB_NAME + "' прошло успешно!\n" +
                        "Введи команду (или help для помощи):\n" +
                        "Запись {names:[id, name, pass], values:[1111, serge, ****]} была успешно создана в таблице '" + TABLE_NAME +"'.\n" +
                        "Введи команду (или help для помощи):\n" +
                        "+----+-----+----+\n" +
                        "|id  |name |pass|\n" +
                        "+----+-----+----+\n" +
                        "|1111|serge|****|\n" +
                        "+----+-----+----+\n" +
                        "Введи команду (или help для помощи):\n" +
                        "\u001B[31mУдаляем данные с таблицы '" + TABLE_NAME + "'. Y/N\u001B[0m\n" +
                        "Таблица " + TABLE_NAME + " была успешно очищена.\n" +
                        "Введи команду (или help для помощи):\n" +
                        "+--+----+----+\n" +
                        "|id|name|pass|\n" +
                        "+--+----+----+\n" +
                        "Введи команду (или help для помощи):\n" +
                        "До скорой встречи!\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);

        in.add("tables");
        in.add("connect|" + DB_NAME2 + "|" + DB_USER + "|" + DB_PASSWORD);

        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // connect sqlcmd
                "Подключение к базе '" + DB_NAME + "' прошло успешно!\n" +
                "Введи команду (или help для помощи):\n" +
                //tables
                "[" + TABLE_NAME + "]\n" +
                "Введи команду (или help для помощи):\n" +
                // connect test
                "Подключение к базе '" + DB_NAME2 + "' прошло успешно!\n" +
                "Введи команду (или help для помощи):\n" +
                //tables
                "[" + TABLE_NAME2 + "]\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|" + DB_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // connect sqlcmd
                "Неудача! по причине: Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: 2\n" +
                "Повтори попытку.\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateRowInTable() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("clear|" + TABLE_NAME);
        in.add("y");

        in.add("create|" + TABLE_NAME + "|id|13|name|Stiven|pass|*****");
        in.add("create|" + TABLE_NAME + "|id|14|name|Pupkin|pass|+++++");
        in.add("show|" + TABLE_NAME );
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // connect
                "Подключение к базе '" + DB_NAME + "' прошло успешно!\n" +
                "Введи команду (или help для помощи):\n" +
                // clear|user
                "\u001B[31mУдаляем данные с таблицы '" + TABLE_NAME + "'. Y/N\u001B[0m\n" +
                "Таблица " + TABLE_NAME + " была успешно очищена.\n" +
                "Введи команду (или help для помощи):\n" +
                // create|user|id|13|name|Stiven|password|*****
                "Запись {names:[id, name, pass], values:[13, Stiven, *****]} была успешно создана в таблице '" + TABLE_NAME + "'.\n" +
                "Введи команду (или help для помощи):\n" +
                "Запись {names:[id, name, pass], values:[14, Pupkin, +++++]} была успешно создана в таблице '" + TABLE_NAME + "'.\n" +
                "Введи команду (или help для помощи):\n" +
                "+--+------+-----+\n" +
                "|id|name  |pass |\n" +
                "+--+------+-----+\n" +
                "|13|Stiven|*****|\n" +
                "+--+------+-----+\n" +
                "|14|Pupkin|+++++|\n" +
                "+--+------+-----+\n" +
                "Введи команду (или help для помощи):\n" +
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("clear|sadfasd|fsf|fdsf");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // connect
                "Подключение к базе '" + DB_NAME + "' прошло успешно!\n" +
                "Введи команду (или help для помощи):\n" +
                // clear|sadfasd|fsf|fdsf
                "Неудача! по причине: Формат команды 'clear|tableName', а ты ввел: clear|sadfasd|fsf|fdsf\n" +
                "Повтори попытку.\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("create|" + TABLE_NAME + "|error");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\n" +
                // connect
                "Подключение к базе '" + DB_NAME + "' прошло успешно!\n" +
                "Введи команду (или help для помощи):\n" +
                // create|user|error
                "Неудача! по причине: Должно быть четное количество параметров в формате 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'create|" + TABLE_NAME + "|error'\n" +
                "Повтори попытку.\n" +
                "Введи команду (или help для помощи):\n" +
                // exit
                "До скорой встречи!\n", getData());
    }
}
