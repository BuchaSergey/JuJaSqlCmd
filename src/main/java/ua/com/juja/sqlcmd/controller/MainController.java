package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Exit;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by Серый on 13.05.2016.
 */
public class MainController {

    private Command[] commands;
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{new Exit((view))};
    }

    public void run() {

        connectToDb();

        while (true) {
            view.write("Введи команду или help для помощи:");
            String command = view.read();
            if (command.equals("list")) {
                doList();
            } else if (command.equals("help")) {
                doHelp();
            } else if (command.startsWith("find")) {
                doFind(command);
            } else if (commands[0].canProcess(command)) {
            commands[0].process(command);
            } else {
                view.write("Несуществующая команда: " + command);
            }
        }
    }

    private void doFind(String command) {
        String[] data = command.split("[|]");
        String tableName = data[1];


        String[] tableColums = manager.getTableColumns(tableName);
        printHeader(tableColums);

        DataSet[] tableData = manager.getTableData(tableName);
        printTable(tableData);


    }

    private void printTable(DataSet[] tableData) {

        for (DataSet row : tableData) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";
        for (Object value :
                values) {
            result += value + "|";
        }
        view.write(result);
    }

    private void printHeader(String[] tableColums) {

        String header = "";
        for (String name : tableColums) {
            header += name + "|";
        }
        view.write("------------");
        view.write(header);
        view.write("------------");
    }


    private void doHelp() {
        view.write("Существующие команды: ");

        view.write("\tlist");
        view.write("\t\tдля получения списка всех таблиц базы, к которой подключились");

        view.write("\thelp");
        view.write("\t\tдля выводы этого списка на экран");

        view.write("\tfind|tableName");
        view.write("\t\tдля получения содержимого таблицы 'tableName'");

        view.write("\texit");
        view.write("\t\tдля выхода из программы");
    }

    private void doList() {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString((tableNames));
        view.write(message);
    }

    private void connectToDb() {


        view.write("Привет юзер");
        view.write("Введи пожалуйста имя базы данных, имя пользователя и пароль в формате: database|userName|password");

        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("[|]");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Неверно количество параметров разделенных знаков '|', ожидается 3, но есть " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {

                printError(e);
            }


        }
        view.write("Успех!");
    }

    private void printError(Exception e) {
        String message = /*e.getClass().getSimpleName() + ": " + */ e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + /*cause.getClass().getSimpleName() + ": " + */ cause.getMessage();
        }
        view.write("Неудача! по причине: " + message);
        view.write("Повтори попытку.");
    }
}
