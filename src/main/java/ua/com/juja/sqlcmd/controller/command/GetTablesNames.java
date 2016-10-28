package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Set;


public class GetTablesNames implements Command {

    private DatabaseManager manager;
    private View view;

    public GetTablesNames(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("tables");
    }

    @Override
    public void process(CheckInput command) {
        Set<String> tableNames = manager.getTableNames();

        String message = tableNames.toString();

        view.write(message);
    }

    @Override
    public String description() {
        return "a list of tables in the database";
    }

    @Override
    public String format() {
        return "tables";
    }
}

