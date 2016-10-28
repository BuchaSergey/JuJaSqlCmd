package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
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
            CheckInput input = new CheckInput(view.read());


            for (Command command : commands) {
                String[] splitFormat = command.format().split("\\|");
                String[] parameters = input.getParameters();
                boolean equals = parameters[0].equals(splitFormat[0]);
                try {
                    if (equals) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw e;
                    }
                    printError(e);
                    break;
                }
            }
            view.write("Enter the command (or \"help\" for tips):");
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        if (cause != null) {
            message += " " + cause.getMessage();
        }
        view.write("Failure! because: " + message);
        view.write("Try again.");
    }

}
