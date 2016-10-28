package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;


public class UnsupportedTest {

    private View view;
    private Command command;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        command = new Unsupported(view);
    }

    @Test
    public void testCanProcess() {

        // when
        boolean canP = command.canProcess("");

        //then
        assertTrue(canP);
    }

    @Test
    public void testProcess() {
        // when
        command.process(new CheckInput("wrongCom"));
        verify(view).write("Command is not exist: wrongCom");

    }

}
