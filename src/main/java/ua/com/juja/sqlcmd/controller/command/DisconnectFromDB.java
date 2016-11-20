package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class DisconnectFromDB extends Command {

    public DisconnectFromDB(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        manager.disconnectFromDB();
        view.write("disconnected");
    }

    @Override
    public String description() {
        return "disconnect from database";
    }

    @Override
    public String commandFormat() {
        return "disconnect";
    }
}
