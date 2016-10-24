package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class MainController {

    private final Command[] commands;
    private final View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Connect(manager, view),
                new CreateDatabase(manager, view),
                new CreateTable(manager, view),
                new ClearTable(manager, view),
                new CreateEntry(manager, view),

                new GetDatabasesNames(manager, view),
                new GetTablesNames(manager, view),
                new GetTableData(manager, view),

                new DisconnectFromDB(manager, view),
                new DropDB(manager, view),
                new DropTable(manager, view),

                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
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
        view.write("Hello user!");
        view.write("Please, enter the name of database, user name and password in format: connect|database|userName|password");

        //noinspection InfiniteLoopStatement
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
                    view.write("Failure! because: " + message);
                    view.write("Try again.");
                    break;
                }
            }
            view.write("Enter the command (or \"help\" for tips):");
        }
    }

}
