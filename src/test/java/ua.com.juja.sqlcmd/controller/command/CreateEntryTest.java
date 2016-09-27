package ua.com.juja.sqlcmd.controller.command;


import org.junit.Before;
import org.junit.Test;
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

        boolean create = command.canProcess("create|test");
        assertTrue(create);

    }

    @Test
    public void testValidationErrorWhenCountParametersIsNotEven() {
        // when
        try {
            command.process("create");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Должно быть четное количество параметров в формате 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'create'", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenOnlyNameOfTable() {
        // when
        try {
            command.process("create|taaat|");
            //fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Должно быть четное количество параметров в формате 'create|tableName|column1|value1|column2|value2|...|columnN|valueN', а ты прислал: 'create'", e.getMessage());
        }
    }
}
