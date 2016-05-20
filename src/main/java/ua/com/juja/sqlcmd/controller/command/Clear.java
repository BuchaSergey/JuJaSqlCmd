package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.sql.SQLException;

/**
 * Created by Серый on 17.05.2016.
 */
public class Clear implements Command {

    private DatabaseManager manager;
    private View view;

    public Clear(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Формат команды 'clear|tableName', а ты ввел: " + command);
        }
        try {
            manager.clear(data[1]);

        } catch (SQLException e) {
            view.write(String.format("Ошибка удаления таблицы '%s', по причине:", data[1], e.getMessage()));
        }

        view.write(String.format("Таблица %s была успешно очищена.", data[1]));
    }
}
