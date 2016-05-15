package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 15.05.2016.
 */
public class Connect implements Command {
    private View view;
    private DatabaseManager manager;

    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {



            try {
                String[] data = command.split("[|]");

                if (data.length != 4) { //TODO 4 - magic
                    throw new IllegalArgumentException("Неверно количество параметров разделенных знаков '|', ожидается 3, но есть " + data.length);
                }
                String databaseName = data[1];
                String userName = data[2];
                String password = data[3];

                manager.connect(databaseName, userName, password);
                view.write("Успех!");

            } catch (Exception e) {

                printError(e);
            }





    }


    private void printError(Exception e) {
        String message =  e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " +  cause.getMessage();
        }
        view.write("Неудача! по причине: " + message);
        view.write("Повтори попытку.");
    }
}
