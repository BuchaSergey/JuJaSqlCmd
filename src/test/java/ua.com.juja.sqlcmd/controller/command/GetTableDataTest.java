package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class GetTableDataTest {

    private DatabaseManager manager;
    private View view;
    private Command command;
    private String tableName = "test";

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new GetTableData(manager, view);

    }

    @Test
    public void testPrintTableData() {
        //given

        Set<String> columns = new LinkedHashSet<>(Arrays.asList("id", "name"));

        Map<String, Object> firstEntry = new LinkedHashMap<>();
        firstEntry.put("id", 1);
        firstEntry.put("name", "FirstUser");

        Map<String, Object> secondEntry = new LinkedHashMap<>();
        secondEntry.put("id", 2);
        secondEntry.put("name", "SecondUser");

        List<Map<String, Object>> records = new LinkedList<>(Arrays.asList(firstEntry, secondEntry));

        when(manager.getTableColumns(tableName)).thenReturn(columns);
        when(manager.getTableData(tableName)).thenReturn(records);

        //when
        command.process(String.format("show|%s", tableName));

        shouldPrint(
                        "[+--+----------+\n" +
                        "|id|name      |\n" +
                        "+--+----------+\n" +
                        "|1 |FirstUser |\n" +
                        "+--+----------+\n" +
                        "|2 |SecondUser|\n" +
                        "+--+----------+]"
        );

    }

    private void setupTableColumns(String tableName, String... columns) {
        when(manager.getTableColumns(tableName))
                .thenReturn(new LinkedHashSet<String>(Arrays.asList(columns)));

    }

    public void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        // given
        Command command = new GetTableData(manager, view);
        // when
        boolean canProcess = command.canProcess("show|user");
        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessFindWithOutParametersString() {
        // given
        Command command = new GetTableData(manager, view);

        // when
        boolean canProcess = command.canProcess("show");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessFindQweString() {
        // when
        boolean canProcess = command.canProcess("qwe");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        //given

        Set<String> columns = new LinkedHashSet<>(Arrays.asList("id", "name"));

        List<Map<String, Object>> records = new LinkedList<>();

        when(manager.getTableColumns(tableName)).thenReturn(columns);
        when(manager.getTableData(tableName)).thenReturn(records);

        //when
        command.process(String.format("show|%s", tableName));

        //then
        shouldPrint(
                "[" +
                        "+--+----+\n" +
                        "|id|name|\n" +
                        "+--+----+]"
        );
    }
    @Test
    public void testPrintTableDataWithOneColumn() {
        //given

        Set<String> columns = new LinkedHashSet<>(Collections.singletonList("id"));

        Map<String, Object> firstEntry = new LinkedHashMap<>();
        firstEntry.put("id", 12);

        Map<String, Object> secondEntry = new LinkedHashMap<>();
        secondEntry.put("id", 13);

        List<Map<String, Object>> records = new LinkedList<>(Arrays.asList(firstEntry, secondEntry));

        when(manager.getTableColumns(tableName)).thenReturn(columns);
        when(manager.getTableData(tableName)).thenReturn(records);

        //when
        command.process("show|test");

        //then
        shouldPrint("[+--+\n" +
                    "|id|\n" +
                    "+--+\n" +
                    "|12|\n" +
                    "+--+\n" +
                    "|13|\n" +
                    "+--+]");
    }

    @Test
    public void testErrorWhenBadCommandFormat() {
        //when
        try {
            command.process("show|user|zaza");
            fail("Expected Exp");
        } catch (IllegalArgumentException e) {
            //then
            assertEquals("Формат команды 'show|tableName', а ты ввел: show|user|zaza", e.getMessage());
        }
        //then


    }

}
