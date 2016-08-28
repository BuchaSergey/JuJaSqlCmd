package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PostgreSQLManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class IntegrationTest {

    private ConfigurableInputStream in;

    private ByteArrayOutputStream out;
    private static DatabaseManager manager;
    private final static String DB_USER = "postgres";

    private final static String DB_PASSWORD = "postgres";
    private final static String DB_NAME = "sqlcmd";
    private final static String TABLE_NAME = "test";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + "(id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";
    private final static String DB_NAME2 = "test";
    private final static String TABLE_NAME2 = "qwe";
//db = test
    //tables = qwe

    @Before
    public void setup() {
        manager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

    }

//    @BeforeClass
//    public static void init() {
//
//    }
//
//    @After

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Привет юзер!\r\n" +
                        "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +

                        "======== SQLCmd  Help ======== \r\n" +
                        "\r\n" +
                        "Существующие команды:\r\n" +
                        "\r\n" +
                        "==============================\r\n" +
                        "\u001B[34m\tconnect|databaseName|userName|password\r\n" +
                        "\u001B[0m\t\tдля подключения к базе данных, с которой будем работать\r\n" +
                        "\u001B[34m\ttables\r\n" +
                        "\u001B[0m\t\tдля получения списка всех таблиц базы, к которой подключились\r\n" +
                        "\u001B[34m\tclear|tableName\r\n" +
                        "\u001B[0m\t\tдля очистки всей таблицы\r\n" +
                        "\u001B[34m\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                        "\u001B[0m\t\tдля создания записи в таблице\r\n" +
                        "\u001B[34m\tshow|tableName\r\n" +
                        "\u001B[0m\t\tдля получения содержимого таблицы 'tableName'\r\n" +
                        "\u001B[34m\thelp\r\n" +
                        "\u001B[0m\t\tдля вывода этого списка на экран\r\n" +
                        "\u001B[34m\texit\r\n" +
                        "\u001B[0m\t\tдля выхода из программы\r\n" +
                        "==============================\r\n" +
                        "Введи команду (или help для помощи):\r\n" +
                        "До скорой встречи!\r\n"

                , getData());
    }

    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
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
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // tables
                "Вы не можете пользоваться командой 'tables' пока не подключитесь с помощью комманды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testShowTableWithoutConnect() {
        // given
        in.add("show|" + TABLE_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // show|user
                "Вы не можете пользоваться командой 'show|" + TABLE_NAME +"' пока не подключитесь с помощью комманды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // unsupported
                "Вы не можете пользоваться командой 'unsupported' пока не подключитесь с помощью комманды connect|databaseName|userName|password\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
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
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе '" + DB_NAME +"' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // unsupported
                "Несуществующая команда: unsupported\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
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
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе '" + DB_NAME +"' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                //tables
                "[" + TABLE_NAME + "]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testBadConnect() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD + "WRONG");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // badConnect
                "Неудача! по причине: Проверьте правильность введенных данных, " +
                "Ошибка при попытке подсоединения.\r\n" +
                "Повтори попытку.\r\n" +
                // exit
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testShowTableAfterClear() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD);
        in.add("create|" + TABLE_NAME + "|name|serge|fam|otec|id|1111");
        in.add("show|" + TABLE_NAME);
        in.add("clear|"+ TABLE_NAME);
        in.add("y");
        in.add("show|" + TABLE_NAME);
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals(
                "Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись {names:[name, fam, id], values:[serge, otec, 1111]} была успешно создана в таблице 'test'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "+------+-----+----+\r\n" +
                "|name  |fam  |id  |\r\n" +
                "+------+-----+----+\r\n" +
                "|Stiven|*****|13  |\r\n" +
                "+------+-----+----+\r\n" +
                "|Eva   |+++++|14  |\r\n" +
                "+------+-----+----+\r\n" +
                "|serge |otec |1111|\r\n" +
                "+------+-----+----+\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "\u001B[31mУдаляем данные с таблицы 'test'. Y/N\u001B[0m\r\n" +
                "Таблица test была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "+----+---+--+\r\n" +
                "|name|fam|id|\r\n" +
                "+----+---+--+\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
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
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect sqlcmd
                "Подключение к базе '" + DB_NAME +"' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                //tables
                "[test]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // connect test
                "Подключение к базе '" + DB_NAME2 +"' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                //tables
                "[" + TABLE_NAME2 + "]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|sqlcmd");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect sqlcmd
                "Неудача! по причине: Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: 2\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testCreateRowInTable() {
        // given


                in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD); 
        in.add("clear|test");
        in.add("y");

        in.add("create|test|name|Stiven|fam|*****|id|13");
        in.add("create|test|name|Eva|fam|+++++|id|14");
        in.add("show|test");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе '" + DB_NAME +"' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // clear|user
                "\u001B[31mУдаляем данные с таблицы 'test'. Y/N\u001B[0m\r\n" +
                "Таблица test была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|id|13|name|Stiven|password|*****
                "Запись {names:[name, fam, id], values:[Stiven, *****, 13]} была успешно создана в таблице 'test'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|id|14|name|Eva|password|+++++
                "Запись {names:[name, fam, id], values:[Eva, +++++, 14]} была успешно создана в таблице 'test'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // show|user
                "+------+-----+--+\r\n" +
                "|name  |fam  |id|\r\n" +
                "+------+-----+--+\r\n" +
                "|Stiven|*****|13|\r\n" +
                "+------+-----+--+\r\n" +
                "|Eva   |+++++|14|\r\n" +
                "+------+-----+--+\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
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
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе '" + DB_NAME +"' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // clear|sadfasd|fsf|fdsf
                "Неудача! по причине: Формат команды 'clear|tableName', а ты ввел: clear|sadfasd|fsf|fdsf\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testCreateWithErrors() {
        // given
        in.add("connect|" + DB_NAME + "|" + DB_USER + "|" + DB_PASSWORD); 
        in.add("create|test|error");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе '" + DB_NAME +"' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|error
                "Неудача! по причине: Должно быть четное количество параметров в формате 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'create|test|error'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }
}
