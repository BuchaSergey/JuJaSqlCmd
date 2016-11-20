package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect extends Command {

    public Connect(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String[] data = getParameters(input);

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
    public String commandFormat() {
        return "connect|database|userName|password";
    }
}


