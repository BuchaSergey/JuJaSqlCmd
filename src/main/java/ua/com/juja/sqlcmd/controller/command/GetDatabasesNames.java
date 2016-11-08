package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class GetDatabasesNames implements Command {

    private final DatabaseManager manager;
    private final View view;

    public GetDatabasesNames(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("databases");
    }

    @Override
    public void process(CheckInput command) {
        {
            for (String database : manager.getDatabasesNames()) {
                view.write(database);
            }
        }
    }

    @Override
    public String description() {
        return "a list of database names";
    }

    @Override
    public String format() {
        return "databases";
    }
}
