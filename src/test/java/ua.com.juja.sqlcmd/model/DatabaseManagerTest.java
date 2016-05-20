

package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;

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
        String[] tablesNames = manager.getTableNames();

        assertEquals("[user, test]", Arrays.toString(tablesNames));

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
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
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
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
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
        String[] columns = manager.getTableColumns("user");

        //then
        assertEquals("[name, password, id]",Arrays.toString(columns));

    }
}
