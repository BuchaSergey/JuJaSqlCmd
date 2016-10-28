package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
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
        boolean canNotProcess = command.canProcess("cleardf|user");
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
        CheckInput input = new CheckInput("clear");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Incorrect number of parameters separated by '|', expected 2 but was: 1", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        // when
        CheckInput input = new CheckInput("clear|table|qwe");
        try {
            command.process(input);
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
        CheckInput input = new CheckInput("clear|test");
        command.process(input);
        //then
       // verify(manager).clear("test");
        verify(view).write("Table 'test' successful cleared");
    }

    @Test
    public void testProcessWithoutConfirmedClearing() {
        //given
        when(view.read()).thenReturn("n");

        //when
        CheckInput input = new CheckInput("clear|test");
        command.process(input);

        //then
        verify(view).write("CANCEL");
    }
}