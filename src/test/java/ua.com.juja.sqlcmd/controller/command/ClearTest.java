package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Серый on 17.05.2016.
 */

public class ClearTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        // given

        // when
        command.process("clear|user");

        // then
        try {
            verify(manager).clear("user");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        verify(view).write("Таблица user была успешно очищена.");
    }


    @Test
    public void testCanProcessClearWithParametersString() {
        // when
        boolean canProcess = command.canProcess("clear|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessClearWithoutParametersString() {
        // when
        boolean canProcess = command.canProcess("clear");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan2() {
        // when
        try {
            command.process("clear");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'clear|tableName', а ты ввел: clear", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        // when
        try {
            command.process("clear|table|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Формат команды 'clear|tableName', а ты ввел: clear|table|qwe", e.getMessage());
        }
    }
}