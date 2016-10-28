package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class GetDatabasesNamesTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new GetDatabasesNames(manager, view);
    }

    @Test
    public void testCanProcess() {
        // when
        boolean create = command.canProcess("databases");
        assertTrue(create);
    }

    @Test
    public void testWithConfirmDropDB() {
        //given
        Set<String> databases = new LinkedHashSet<>(Arrays.asList("database1", "database2", "database3"));
        when(manager.getDatabasesNames()).thenReturn(databases);

        //when
        command.process(new CheckInput("databases"));

        //then
        verify(manager).getDatabasesNames();
        for (String database : databases) {
            verify(view).write(database);
        }

    }
}
