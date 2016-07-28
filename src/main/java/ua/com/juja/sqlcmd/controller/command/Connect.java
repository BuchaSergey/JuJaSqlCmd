package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 15.05.2016.
 *        connect|sqlcmd|postgres|postgres
 *        create|test|name|anna|fam|ill|
 */
public class Connect implements Command {

    private static String COMMAND_SAMPLE = "connect|sqlcmd|postgres|postgres";

    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    private int count() {
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
        try {
            manager.connect(database, userName, password);
            view.write(String.format("Подключение к базе '%s' прошло успешно!", database));

        } catch (Exception e) {
                    view.write(String.format("Подключение к базе '%s' для user '%s' не удалось по причине: %s. ",
                    database, userName, e.getMessage()))  ;
        }
    }




}

//        connect|sqlcmd|postgres|postgres
