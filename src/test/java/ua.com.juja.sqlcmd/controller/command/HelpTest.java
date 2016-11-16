package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class HelpTest {
    private View view;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        command = new Help(view);
    }

    @Test
    public void testCanProcess() {
        // when
        boolean canP = command.canProcess("help");
        //then
        assertTrue(canP);
    }

    @Test
    public void testProcess() throws Exception {
        CheckInput checkInput = new CheckInput("help");
        command.process(checkInput);
        verify(view).write("======== SQLCmd  Help ======== ");
    }
}
