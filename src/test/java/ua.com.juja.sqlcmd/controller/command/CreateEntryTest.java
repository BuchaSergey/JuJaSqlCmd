package ua.com.juja.sqlcmd.controller.command;


import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class CreateEntryTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateEntry(manager, view);
    }

    @Test
    public void testCanProcess() {
        // when
        boolean create = command.canProcess("createEntry|test");
        assertTrue(create);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsNotEven() {
        // when
        CheckInput input = new CheckInput("createEntry|");
        try {
            command.process(input);
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Invalid input, you must enter an even number of parameters in the following format: createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenOnlyNameOfTable() {
        // when
        CheckInput input = new CheckInput("createEntry|taaat|");

        try {
            command.process(input);
            //fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Incorrect number of parameters separated by '|', expected 4 but was: 2", e.getMessage());
        }
    }
}
