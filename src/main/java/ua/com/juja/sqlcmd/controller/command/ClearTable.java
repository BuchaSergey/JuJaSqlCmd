package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class ClearTable extends Command {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private final DatabaseManager manager;
    private final View view;

    public ClearTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String tableName = getParameters(input)[1];
        confirmAndClearTable(tableName);
    }

    private void confirmAndClearTable(String clearTableName) {
        try {
            view.write(String.format(ANSI_RED + "Do you want delete data from table '%s'? Y/N" + ANSI_RESET, clearTableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.clear(clearTableName);
                view.write(String.format("Table %s was successful cleared.", clearTableName));
            } else view.write("Command 'clear' is canceled.");
        } catch (Exception e) {
            view.write(String.format("Exception deleting table data from '%s', because: %s", clearTableName, e.getMessage()));
        }
    }

    @Override
    public String description() {
        return "clear table data";
    }

    @Override
    public String commandFormat() {
        return "clear|tableName";
    }
}

