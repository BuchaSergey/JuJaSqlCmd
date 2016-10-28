package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;


public class IsConnectedTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new IsConnected(manager, view);
    }

    @Test
    public void testProcess() {
        CheckInput inputCommand = new CheckInput("if does not connected");
        command.process(inputCommand);
        verify(view).write("You could't not use the command \'if does not connected\' while You are not connected to database in this format: connect|databaseName|userName|password"
        );
    }

    @Test
    public void testCanProcess() {
        // when
        boolean canP = command.canProcess("connect|");
        //then
        assertTrue(canP);
    }
}
