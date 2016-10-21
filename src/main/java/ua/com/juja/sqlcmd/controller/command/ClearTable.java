package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class ClearTable implements Command {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private final DatabaseManager manager;
    private final View view;

    public ClearTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'clear|tableName', а ты ввел: " + command);
        }
        String clearTableName = data[1];
        confirmAndClearTable(clearTableName);
    }

    private void confirmAndClearTable(String clearTableName) {
        try {
            view.write(String.format(ANSI_RED + "Do you want delete data from table '%s'? Y/N" + ANSI_RESET, clearTableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.clear(clearTableName);
                view.write(String.format("Table %s was successful cleared.", clearTableName));
            } else view.write(String.format("TableClear is canceled."));
        } catch (Exception e) {
            view.write(String.format("Exception deleting table data from '%s', because: %s", clearTableName, e.getMessage()));
        }
    }
}

