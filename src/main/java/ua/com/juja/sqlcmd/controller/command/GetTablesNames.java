package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Set;


public class GetTablesNames implements Command {

    private final DatabaseManager manager;
    private final View view;

    public GetTablesNames(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("tables");
    }

    @Override
    public void process(String command) {
        Set<String> tableNames = manager.getTableNames();

        String message = tableNames.toString();

        view.write(message);
    }
}

