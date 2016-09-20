package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class ClearTable implements Command {

    private DatabaseManager manager;
    private View view;
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public ClearTable(DatabaseManager manager, View view) {
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

            view.write(String.format(ANSI_RED + "Удаляем данные с таблицы '%s'. Y/N" + ANSI_RESET, tableName));
            if (view.read().equalsIgnoreCase("y")) {
                manager.clear(tableName);
                view.write(String.format("Таблица %s была успешно очищена.",tableName));
            }
        } catch (Exception e) {
            view.write(String.format("Ошибка удаления таблицы '%s', по причине: %s", tableName, e.getMessage()));
        }
    }
}

