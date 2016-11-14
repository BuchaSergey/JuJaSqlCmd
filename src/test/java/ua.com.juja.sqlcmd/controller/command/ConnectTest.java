package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ConnectTest {

    private Command command;
    private DatabaseManager manager;
    private View view;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Connect(manager, view);
    }

    @Test
    public void testCanProcessWithParameters() {
        // when
        boolean canProcess = command.canProcess("connect|sqlcmd|postgres|postgres");
        // then
        assertTrue(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan4() {
        // when
        CheckInput input = new CheckInput("connect|sqlcmd|postgres");
        try {
            command.process(input);

        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Incorrect number of parameters separated by '|', expected 4 but was: 3", e.getMessage());
        }
    }

    @Test
    public void testCorrectConnectCommand() {
        CheckInput input = new CheckInput("connect|sqlcmd|postgres|postgres");
        //when
        command.process(input);

        //then
        verify(view).write("Connection to database 'sqlcmd' is successful!");
    }
}

