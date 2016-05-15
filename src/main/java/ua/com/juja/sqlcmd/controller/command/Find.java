package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 15.05.2016.
 */
public class Find implements Command {
    private View view;
    private DatabaseManager manager;

    public Find(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");
        String tableName = data[1];


        String[] tableColums = manager.getTableColumns(tableName);
        printHeader(tableColums);

        DataSet[] tableData = manager.getTableData(tableName);
        printTable(tableData);


    }

    private void printTable(DataSet[] tableData) {

        for (DataSet row : tableData) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";
        for (Object value :
                values) {
            result += value + "|";
        }
        view.write(result);
    }

    private void printHeader(String[] tableColums) {

        String header = "";
        for (String name : tableColums) {
            header += name + "|";
        }
        view.write("------------");
        view.write(header);
        view.write("------------");
    }
}