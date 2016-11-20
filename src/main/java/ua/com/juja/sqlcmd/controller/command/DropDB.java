package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class DropDB extends Command {

    public DropDB(DatabaseManager manager, View view) {
        super(manager, view);

    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String databaseName = getParameters(input)[1];

        manager.dropDB(databaseName);
        view.write(String.format("Database '%s' was deleted.", databaseName));
    }

    @Override
    public String description() {
        return "delete database";
    }

    @Override
    public String commandFormat() {
        return "dropDB|databaseName";
    }
}
