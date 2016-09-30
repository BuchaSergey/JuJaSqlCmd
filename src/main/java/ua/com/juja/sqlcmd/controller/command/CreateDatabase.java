package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class CreateDatabase implements Command {

    private final DatabaseManager manager;
    private final View view;

    public CreateDatabase(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createDB|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'createDB|databaseName', а ты ввел: " + command);
        }

        manager.createDatabase(data[1]);
        view.write("База '" + data[1] + "' создана.");
    }
}
