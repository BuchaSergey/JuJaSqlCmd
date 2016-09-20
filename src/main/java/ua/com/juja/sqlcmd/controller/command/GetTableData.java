package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.TableConstructor;
import ua.com.juja.sqlcmd.view.View;


public class GetTableData implements Command {

    private DatabaseManager manager;
    private View view;

    public GetTableData(DatabaseManager manager, View view) {
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

        TableConstructor constructor = new TableConstructor(
                manager.getTableColumns(data[1]), manager.getTableData(data[1]));
        view.write(constructor.getTableString());
    }
}
