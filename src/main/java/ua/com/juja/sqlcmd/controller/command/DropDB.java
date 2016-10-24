package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class DropDB implements Command {

    private DatabaseManager manager;
    private View view;

    public DropDB(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropDB|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'dropDB|databaseName', а ты ввел: " + command);
        }

        manager.dropDB(data[1]);
        view.write(String.format("Database '%s' was deleted.",data[1]));
    }

    @Override
    public String description() {
        return "Delete database";
    }

    @Override
    public String format() {
        return "dropDB|databaseName";
    }
}
