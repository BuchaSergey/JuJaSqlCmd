package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class CreateDatabaseTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateDatabase(manager, view);
    }

    @Test
    public void testCanProcess() {
        // when
        boolean create = command.canProcess("createDB|test");
        assertTrue(create);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsNotEven() {
        // when
        try {
            command.process("createDB");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'createDB|databaseName', а ты ввел: createDB", e.getMessage());
        }
    }

    @Test
    public void testWithConfirmDropDB() {
        //when
        command.process("createDB|test");
        //then
        verify(manager).createDatabase("test");
        verify(view).write("База 'test' создана.");
    }
}
