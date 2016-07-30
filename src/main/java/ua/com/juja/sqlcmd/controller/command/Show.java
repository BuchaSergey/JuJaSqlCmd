package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.TableConstructor;
import ua.com.juja.sqlcmd.view.View;

import java.util.List;
import java.util.Set;

/**
 * Created by Серый on 15.05.2016.
 */
public class Show implements Command {

    private DatabaseManager manager;
    private View view;

    public Show(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("show|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'show|tableName', а ты ввел: " + command);
        }
        String tableName = data[1];

        Set<String> tableColumns = manager.getTableColumns(tableName);
        List<DataSet> tableData2 = manager.getTableData(tableName);


        TableConstructor constructor = new TableConstructor(
                tableColumns, tableData2);
        view.write(constructor.getTableString());
    }
}
