package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class HelpTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
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
        command.process("help");
        verify(view).write("Существующие команды:");
    }
}
