package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class DropDBTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DropDB(manager, view);
    }

    @Test
    public void testCanProcess() {
        // when
        boolean create = command.canProcess("dropDB|test");
        assertTrue(create);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsNotEven() {
        // when
        CheckInput input = new CheckInput("dropDB");

        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Incorrect number of parameters separated by '|', expected 2 but was: 1", e.getMessage());
        }
    }

    @Test
    public void testWithConfirmDropDB() {
        //when
        CheckInput input = new CheckInput("dropDB|test");

        command.process(input);
        //then
        verify(manager).dropDB("test");
        verify(view).write("Database 'test' was deleted.");
    }
}
