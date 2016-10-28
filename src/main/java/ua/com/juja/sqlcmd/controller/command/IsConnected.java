package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class IsConnected implements Command {

    private final DatabaseManager manager;
    private final View view;

    public IsConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(CheckInput command) {
        view.write(String.format("You could't not use the command '%s' while " +
                "You are not connected to database in this format: " +
                "connect|databaseName|userName|password", command));
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String format() {
        return null;
    }
}
