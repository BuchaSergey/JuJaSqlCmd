package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by Seriy on 27.08.2016.
 */
public class IsConnectedTest {
    DatabaseManager manager;
    View view;
    Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new IsConnected(manager, view);
    }

    @Test
    public void testProcess() {
        command.process("if does not connected");
        verify(view).write("Вы не можете пользоваться командой 'if does not connected' пока не подключитесь с помощью комманды connect|databaseName|userName|password"
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
