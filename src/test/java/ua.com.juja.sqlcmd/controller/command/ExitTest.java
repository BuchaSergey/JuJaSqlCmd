package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ExitTest {

    private Command command;
    private View view;

    @Before
    public void setup() {
        view = mock(View.class);
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExitString() {
        // given
        Command command = new Exit(view);

        // when
        boolean canProcess = command.canProcess("exit");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessQweString() {
        // given
        Command command = new Exit(view);

        // when
        boolean canProcess = command.canProcess("qwe");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testProcessExitCommand_thowsExitException() {
        // given
        Command command = new Exit(view);

        // when
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            // do nothing
        }
        // then
        verify(view).write("До скорой встречи!");

    }
}
