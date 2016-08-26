package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class MainController {

    private Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
                new GetTablesNames(manager, view),
                new ClearTable(manager, view),
                new CreateEntry(manager, view),
                new GetTableData(manager, view),
                new Unsupported(view)
        };
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            // do nothing
        }
    }

    private void doWork() {
        view.write("Привет юзер!");
        view.write("Введи, пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw e;
                    }
                    String message = e.getMessage();
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        message += " " + cause.getMessage();
                    }
                    view.write("Неудача! по причине: " + message);
                    view.write("Повтори попытку.");
                    break;
                }
            }
            view.write("Введи команду (или help для помощи):");
        }
    }

}
