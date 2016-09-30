package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;


public class UnsupportedTest {
    DatabaseManager manager;
    View view;
    Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
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
        command.process("wrongCom");
        verify(view).write("Несуществующая команда: wrongCom");

    }

}
