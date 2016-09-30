package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class CreateTable implements Command {
    private final DatabaseManager manager;
    private final View view;

    public CreateTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createTable|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'createTable|tableName', а ты ввел: " + command);
        }
        manager.createTable(data[1]);
        view.write("Tаблица '" + data[1] + "' создана.");
    }
}
