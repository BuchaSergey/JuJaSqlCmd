package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class CreateTable extends Command {

    public CreateTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String tableName = getParameters(input)[1];
        manager.createTable(tableName);
        view.write(String.format("Table '%s' is created.", tableName));
    }

    @Override
    public String description() {
        return "create the table in current database (id SERIAL PRIMARY KEY, username text, password text)";
    }

    @Override
    public String commandFormat() {
        return "createTable|tableName";
    }
}
