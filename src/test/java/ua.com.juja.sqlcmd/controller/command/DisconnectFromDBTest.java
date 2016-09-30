package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class DisconnectFromDBTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DisconnectFromDB(manager, view);
    }

    @Test
    public void testCanProcess() {
        // when
        boolean create = command.canProcess("disconnect");
        assertTrue(create);
    }

    @Test
    public void testWithConfirmDropDB() {
        //when
        command.process("disconnect");
        //then
        verify(manager).disconnectFromDB();
        verify(view).write("Disconnected");
    }
}
