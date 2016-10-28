package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class GetTablesNamesTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new GetTablesNames(manager, view);

    }

    @Test
    public void testCanProcess() {
        // when
        boolean canP = command.canProcess("tables");
        //then
        assertTrue(canP);

    }

    @Test
    public void testProcess() {
        //given
        Set<String> tableNames = new LinkedHashSet<>(
                Arrays.asList("table", "test", "table1", "test2", "test3"));
        when(manager.getTableNames()).thenReturn(tableNames);

        //when
        CheckInput input = new CheckInput("tables");
        command.process(input);
        //then
        verify(manager).getTableNames();


    }

}
