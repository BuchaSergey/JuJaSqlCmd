package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class IsConnected extends Command {

    public IsConnected(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String input) {
        view.write(String.format("You could't not use the command '%s' while " +
                "You are not connected to database in this format: " +
                "connect|databaseName|userName|password", input));
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String commandFormat() {
        return null;
    }
}
