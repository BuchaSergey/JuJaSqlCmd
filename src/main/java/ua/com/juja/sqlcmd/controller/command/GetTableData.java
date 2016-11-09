package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.TableConstructor;
import ua.com.juja.sqlcmd.view.View;


public class GetTableData implements Command {

    private final DatabaseManager manager;
    private final View view;

    public GetTableData(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("show|");
    }

    @Override
    public void process(CheckInput command) {
        command.parametersValidation(format());
        String[] data = command.getParameters();

        TableConstructor constructor = new TableConstructor(
                manager.getTableColumns(data[1]), manager.getTableData(data[1]));
        view.write(constructor.getTableString());
    }

    @Override
    public String description() {
        return "display table data";
    }

    @Override
    public String format() {
        return "show|tableName";
    }
}
