

package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Серый on 05.05.2016.
 */
public abstract class DatabaseManagerTest {
    protected DatabaseManager manager;
    public abstract DatabaseManager getDatabaseManager();

    @Before
    public  void setup() {
        manager = getDatabaseManager();
        try {
            manager.connect("sqlcmd", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testGetAllTableNames() {
       Set<String>  tablesNames = manager.getTableNames();

        assertEquals("[user, test]", tablesNames.toString());

    }


    @Test
    public void testGetTableData() {
        //given
        try {
            manager.clear("user");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //when
        DataSet input = new DataSet();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 13);
        manager.create("user", input);

        //then
        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Stiven, pass, 13]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        //given
        try {
            manager.clear("user");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSet input = new DataSet();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 13);
        manager.create("user", input);

        //when
        DataSet newValue = new DataSet();
        newValue.put("password", "pass2");
        newValue.put("name", "Pup");
        manager.update("user", 13, newValue);

        //then
        List<DataSet> users = manager.getTableData("user");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Pup, pass2, 13]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testGetColumnNames() {
        //given
        try {
            manager.clear("user");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //when
        Set<String> columns = manager.getTableColumns("user");

        //then
        assertEquals("[name, password, id]",columns.toString());
    }
}
