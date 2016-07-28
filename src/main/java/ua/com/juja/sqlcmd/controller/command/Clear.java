package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 17.05.2016.
 */
public class Clear implements Command {

    private DatabaseManager manager;
    private View view;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

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
        String tableName = data[1];
        try {
            manager.clear(tableName);
            view.write(String.format(ANSI_RED + "ВНИМАНИЕ!" + ANSI_RESET + " Вы собираетесь удалить все данные " +
                    "с таблицы '%s'. 'y' для подтверждения, 'n' для отмены", tableName));
            String check = view.read();
            if (check.equalsIgnoreCase("Y")) {
                manager.clear(tableName);
                view.write(String.format("Таблица %s была успешно очищена.", data[1]));
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления таблицы '%s', по причине: %s", data[1], e.getMessage()));
        }
    }
}
