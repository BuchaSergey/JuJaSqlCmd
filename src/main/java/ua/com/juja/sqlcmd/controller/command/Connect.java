package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 15.05.2016.
 */
public class Connect implements Command {

    private static String COMMAND_SAMPLE = "connect|sqlcmd|postgres|postgres";

    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
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
        String databaseName = data[1];
        String userName = data[2];
        String password = data[3];

        manager.connect(databaseName, userName, password);

        view.write("Успех!");
    }

    private int count() {
        return COMMAND_SAMPLE.split("\\|").length;
    }


}
