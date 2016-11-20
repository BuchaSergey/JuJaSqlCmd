package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class GetDatabasesNames extends Command {

    public GetDatabasesNames(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        {
            for (String database : manager.getDatabasesNames()) {
                view.write(database);
            }
        }
    }

    @Override
    public String description() {
        return "display list of databases";
    }

    @Override
    public String commandFormat() {
        return "databases";
    }
}
