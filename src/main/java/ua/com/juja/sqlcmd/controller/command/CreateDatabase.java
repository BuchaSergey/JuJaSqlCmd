package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class CreateDatabase extends Command {

    public CreateDatabase(DatabaseManager manager, View view) {
        super(manager, view);

    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String databaseName = getParameters(input)[1];

        manager.createDatabase(databaseName);
        view.write(String.format("Database '%s' is create.", databaseName));
    }

    @Override
    public String description() {
        return "create new database";
    }

    @Override
    public String commandFormat() {
        return "createDB|databaseName";
    }
}
