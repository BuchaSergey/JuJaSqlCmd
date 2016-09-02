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

/**
 * Created by Seriy on 30.08.2016.
 */
public class CreateTableTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateTable(manager, view);
    }

    @Test
    public void testCanProcess() {
        // when
        boolean create = command.canProcess("createTable|test");
        assertTrue(create);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsNotEven() {
        // when
        try {
            command.process("createTable");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'createTable|tableName', а ты ввел: createTable", e.getMessage());
        }
    }

    @Test
    public void testWithConfirmDropDB() {
        //when
        command.process("createTable|test");
        //then
        verify(manager).createTable("test");
        verify(view).write("Tаблица 'test' создана.");
    }
}

