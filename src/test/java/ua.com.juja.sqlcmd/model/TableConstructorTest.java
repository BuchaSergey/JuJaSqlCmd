package ua.com.juja.sqlcmd.model;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TableConstructorTest {

    @Test
    public void testTableConstructorWithOneParameters() {

        TableConstructor table = initTableConstructor();

        assertEquals("+--+----------+--------+\n" +
                "|id|name      |password|\n" +
                "+--+----------+--------+\n" +
                "|1 |FirstUser |+++++   |\n" +
                "+--+----------+--------+\n" +
                "|2 |SecondUser|#####   |\n" +
                "+--+----------+--------+", table.getTableString());
    }

    private TableConstructor initTableConstructor() {

        Set<String> columns = new LinkedHashSet<>(Arrays.asList("id", "name", "password"));

        Map<String, Object> firstEntry = new LinkedHashMap<>();
        firstEntry.put("id", 1);
        firstEntry.put("name", "FirstUser");
        firstEntry.put("password", "+++++");


        Map<String, Object> secondEntry = new LinkedHashMap<>();
        secondEntry.put("id", 2);
        secondEntry.put("name", "SecondUser");
        secondEntry.put("password", "#####");

        List<Map<String, Object>> tableData = new LinkedList<>(Arrays.asList(firstEntry, secondEntry));

        return new TableConstructor(columns, tableData);
    }
}
