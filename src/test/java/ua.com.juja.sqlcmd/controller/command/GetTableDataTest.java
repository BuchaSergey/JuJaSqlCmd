package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
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

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new GetTableData(manager, view);

    }

    @Test
    public void testPrintTableData() {
        //given
        setupTableColumns("user", "id", "name", "password");

        DataSet user1 = new DataSetImpl();
        user1.put("id", 12);
        user1.put("name", "Stiven");
        user1.put("password", "*****");

        DataSet user2 = new DataSetImpl();
        user2.put("id", 13);
        user2.put("name", "Eva");
        user2.put("password", "+++++");

        when(manager.getTableData("user")).thenReturn(Arrays.asList(user1, user2));

        //when
        command.process("find|user");

        //then
        shouldPrint("[+--+------+--------+\n" +
                    "|id|name  |password|\n" +
                    "+--+------+--------+\n" +
                    "|12|Stiven|*****   |\n" +
                    "+--+------+--------+\n" +
                    "|13|Eva   |+++++   |\n" +
                    "+--+------+--------+]");
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
        // given

        // when
        boolean canProcess = command.canProcess("qwe");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
//given
        String tableName = "test";
        Set<String> columns = new LinkedHashSet<>(Arrays.asList("id", "name"));

        List<DataSet> records = new LinkedList<>();

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
        setupTableColumns("test", "id");

        DataSet user1 = new DataSetImpl();
        user1.put("id", 12);

        DataSet user2 = new DataSetImpl();
        user2.put("id", 13);

        when(manager.getTableData("test"))
                .thenReturn(Arrays.asList(user1, user2));

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
