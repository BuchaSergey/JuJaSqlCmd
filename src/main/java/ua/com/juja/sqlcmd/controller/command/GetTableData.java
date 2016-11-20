package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.TableConstructor;
import ua.com.juja.sqlcmd.view.View;


public class GetTableData extends Command {

    public GetTableData(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String tableName = getParameters(input)[1];

        TableConstructor constructor = new TableConstructor(
                manager.getTableColumns(tableName), manager.getTableData(tableName));
        view.write(constructor.getTableString());
    }

    @Override
    public String description() {
        return "display table data";
    }

    @Override
    public String commandFormat() {
        return "show|tableName";
    }
}
