package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
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
    public void process(CheckInput command) {
        command.parametersValidation(format());
        String[] data = command.getParameters();
        manager.createTable(data[1]);
        view.write(String.format("Table '%s' is created.", data[1]));
    }

    @Override
    public String description() {
        return "create the table in current database";
    }

    @Override
    public String format() {
        return "createTable|tableName";
    }
}
