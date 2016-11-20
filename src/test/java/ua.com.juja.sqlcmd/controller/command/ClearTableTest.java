package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
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
        boolean canNotProcess = command.canProcess("clearWrong|user");
        assertFalse(canNotProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan2() {
        // when
        try {
            command.process("clear");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Incorrect number of parameters separated by '|', expected 2 but was: 1", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        // when
        try {
            command.process("clear|table|thirdParam");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Incorrect number of parameters separated by '|', expected 2 but was: 3", e.getMessage());
        }
    }

    @Test
    public void testWithConfirmClear() {
        //given
        when(view.read()).thenReturn("y");

        //when
        command.process("clear|test");
        //then
        verify(manager).clear("test");
        verify(view).read();
    }

    @Test
    public void testProcessWithoutConfirmedClearing() {
        //given
        when(view.read()).thenReturn("n");

        //when
        command.process("clear|test");

        //then
        verify(view).read();
    }
}