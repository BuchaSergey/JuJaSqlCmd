package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public abstract class DatabaseManagerTest {
    protected DatabaseManager manager;

    public abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() throws SQLException {
        manager = getDatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");
        manager.getTableData("user");
        manager.getTableData("test");

    }

    @Test
    public void testGetAllTableNames() {
        Set<String> tablesNames = manager.getTableNames();

        assertEquals("[user, test]", tablesNames.toString());
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clear("user");

        //when
        DataSet input = new DataSetImpl();
        input.putNewValueDataSet("name", "Stiven");
        input.putNewValueDataSet("password", "pass");
        input.putNewValueDataSet("id", 13);
        manager.createEntry("user", input);

        //then
        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Stiven, pass, 13]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clear("user");

        DataSet input = new DataSetImpl();
        input.putNewValueDataSet("name", "Stiven");
        input.putNewValueDataSet("password", "pass");
        input.putNewValueDataSet("id", 13);
        manager.createEntry("user", input);

        //when
        DataSet newValue = new DataSetImpl();
        newValue.putNewValueDataSet("password", "pass2");
        newValue.putNewValueDataSet("name", "Pup");
        manager.update("user", 13, newValue);

        //then
        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", user.getNames().toString());
        assertEquals("[Pup, pass2, 13]", user.getValues().toString());
    }

    @Test
    public void testGetColumnNames() {
        //given
        manager.clear("user");
        //when
        Set<String> columns = manager.getTableColumns("user");
        //then
        assertEquals("[name, password, id]", columns.toString());
    }
}
