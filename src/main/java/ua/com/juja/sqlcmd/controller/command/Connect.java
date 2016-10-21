package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect implements Command {

    private final DatabaseManager manager;
    private final View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    private int count() {
        String COMMAND_SAMPLE = "connect|sqlcmd|postgres|postgres";
        return COMMAND_SAMPLE.split("\\|").length;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {

        String[] data = command.split("\\|");
        if (data.length != count()) {
            throw new IllegalArgumentException(
                    String.format("Неверно количество параметров разделенных " +
                                    "знаком '|', ожидается %s, но есть: %s",
                            count(), data.length));
        }

        String database = data[1];
        String userName = data[2];
        String password = data[3];

        manager.connect(database, userName, password);
        view.write(String.format("Connection to database '%s' is successful", database));


    }
}


