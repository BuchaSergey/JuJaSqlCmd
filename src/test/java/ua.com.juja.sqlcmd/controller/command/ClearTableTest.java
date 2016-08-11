package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class ClearTableTest {

    private DatabaseManager manager;
    private View view;
    private Command command;


    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new ClearTable(manager, view);
    }


    @Test
    public void testCanProcessClearWithParametersString() {
        // when
        boolean canProcess = command.canProcess("clear|user");


        // then
        assertTrue(canProcess);
    }

    @Test
    public void testClearTableWrongCommand() {
        boolean  canNotProcess = command.canProcess("cleardf|user");
        assertFalse(canNotProcess);
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

    @Test
    public void testExceptionClear() {

            Command command = new ClearTable(manager, view);
            command.process("clear|test");
            // then


        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[\u001B[31mУдаляем данные с таблицы 'test'. Y/N\u001B[0m, Ошибка удаления таблицы 'test', по причине: null]",captor.getAllValues().toString());

    }

    @Test
    public void testFull() {

        Command command = new ClearTable(manager, view);
        command.process("clear|test");
        // then

        verify(view).write("\u001B[31mУдаляем данные с таблицы 'test'. Y/N\u001B[0m");

        view.write("y");
        verify(view).write("Таблица test была успешно очищена.");



    }
}