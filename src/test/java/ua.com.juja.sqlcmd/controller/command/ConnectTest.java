package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PropertiesLoader;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Seriy on 12.08.2016.
 */
public class ConnectTest {

    private Command command;
    private DatabaseManager manager;
    private View view;

    PropertiesLoader properties;



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
        try {
            command.process("connect|sqlcmd|postgres");

        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Неверно количество параметров разделенных знаком '|', ожидается 4, но есть: 3", e.getMessage());
        }
    }

    @Test
    public void testCorrectConnectCommand() {
        //when
        command.process("connect|sqlcmd|postgres|postgres");

        //then
        verify(view).write("Подключение к базе 'sqlcmd' прошло успешно!");
    }


}

