package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect implements Command {

    private final DatabaseManager manager;
    private final View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(CheckInput command) {

        String[] data = command.getParameters();
        command.parametersValidation(format());

        int DB_NUMBER = 1;
        int USER_NUMBER = 2;
        int PASSWORD_NUMBER = 3;
        String database = data[DB_NUMBER];
        String userName = data[USER_NUMBER];
        String password = data[PASSWORD_NUMBER];

        manager.connect(database, userName, password);
        view.write(String.format("Connection to database '%s' is successful!", database));
    }

    @Override
    public String description() {
        return "connect to specific database";
    }

    @Override
    public String format() {
        return "connect|database|userName|password";
    }
}


