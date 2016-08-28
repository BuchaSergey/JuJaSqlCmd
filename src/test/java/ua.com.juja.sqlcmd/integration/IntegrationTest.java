package ua.com.juja.sqlcmd.integration;

import org.junit.After;
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
    private static DatabaseManager databaseManager;

    private final static String DB_USER = "postgres";
    private final static String DB_PASSWORD = "postgres";
    private final static String DB_NAME = "sqlcmd";
    private final static String TABLE_NAME = "test";
    private final static String SQL_CREATE_TABLE = TABLE_NAME + "(id SERIAL PRIMARY KEY," +
            " username VARCHAR (50) UNIQUE NOT NULL," +
            " password VARCHAR (50) NOT NULL)";


    @Before
    public void setup() {
        databaseManager = new PostgreSQLManager();
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

//    @BeforeClass
//    public static void init() {
//        databaseManager.connect("postgres","postgres","postgres");
//    }

    @After

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
        in.add("show|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // show|user
                "Вы не можете пользоваться командой 'show|user' пока не подключитесь с помощью комманды connect|databaseName|userName|password\r\n" +
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
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
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
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                //tables
                "[test, user]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testBadConnect() {
        // given
        in.add("connect|sqlcmd|postgres|postgresDDD");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // badConnect
                "Подключение к базе 'sqlcmd' для user 'postgres' не удалось по причине: Проверьте правильность введенных данных, " +
                "Ошибка при попытке подсоединения.. \r\n" +
                // exit
                "Введи команду (или help для помощи):\r\n" +
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testShowTableAfterClear() {


        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("create|test|name|serge|fam|otec|id|1111");
        in.add("show|test");
        in.add("clear|test");
        in.add("y");
        in.add("show|test");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "Запись {names:[name, fam, id], values:[serge, otec, 1111]} была успешно создана в таблице 'test'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "+-----+----+----+\r\n" +
                "|name |fam |id  |\r\n" +
                "+-----+----+----+\r\n" +
                "|serge|otec|1111|\r\n" +
                "+-----+----+----+\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                "\u001B[31mВНИМАНИЕ!\u001B[0m Вы собираетесь удалить все данные с таблицы 'test'. 'y' для подтверждения, 'n' для отмены\r\n" +
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
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("tables");
        in.add("connect|test|postgres|postgres");
        in.add("tables");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect sqlcmd
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                //tables
                "[test, user]\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // connect test
                "Подключение к базе 'test' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                //tables
                "[qwe]\r\n" +
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


        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|public.user");
        in.add("y");

        in.add("create|user|id|13|name|Stiven|password|*****");
        in.add("create|user|id|14|name|Eva|password|+++++");
        in.add("show|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // clear|user
                "\u001B[31mВНИМАНИЕ!\u001B[0m Вы собираетесь удалить все данные с таблицы 'public.user'. 'y' для подтверждения, 'n' для отмены\r\n" +
                "Таблица public.user была успешно очищена.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|id|13|name|Stiven|password|*****
                "Запись {names:[id, name, password], values:[13, Stiven, *****]} была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|id|14|name|Eva|password|+++++
                "Запись {names:[id, name, password], values:[14, Eva, +++++]} была успешно создана в таблице 'user'.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // show|user
                "+------+--------+--+\n" +
                "|name  |password|id|\n" +
                "+------+--------+--+\n" +
                "|Stiven|*****   |13|\n" +
                "+------+--------+--+\n" +
                "|Eva   |+++++   |14|\n" +
                "+------+--------+--+\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|sadfasd|fsf|fdsf");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
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
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("create|user|error");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Привет юзер!\r\n" +
                "Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password\r\n" +
                // connect
                "Подключение к базе 'sqlcmd' прошло успешно!\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // create|user|error
                "Неудача! по причине: Должно быть четное количество параметров в формате 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'create|user|error'\r\n" +
                "Повтори попытку.\r\n" +
                "Введи команду (или help для помощи):\r\n" +
                // exit
                "До скорой встречи!\r\n", getData());
    }
}
