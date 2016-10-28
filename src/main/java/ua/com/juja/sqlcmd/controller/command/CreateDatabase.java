package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
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
    public void process(CheckInput command) {
        String[] data = command.getParameters();
        command.parametersValidation(format());


        manager.createDatabase(data[1]);
        view.write(String.format("Database '%s' is create.", data[1]));
    }

    @Override
    public String description() {
        return "create databases";
    }

    @Override
    public String format() {
        return "createDB|databaseName";
    }
}
