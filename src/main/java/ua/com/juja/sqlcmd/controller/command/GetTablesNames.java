package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Set;


public class GetTablesNames extends Command {

    public GetTablesNames(DatabaseManager manager, View view) {
        super(manager, view);

    }

    @Override
    public void process(String input) {
        validationParameters(input);
        Set<String> tableNames = manager.getTableNames();
        String message = tableNames.toString();
        view.write(message);
    }

    @Override
    public String description() {
        return "display list of tables in the database";
    }

    @Override
    public String commandFormat() {
        return "tables";
    }
}

