package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class DisconnectFromDB implements Command {

    private final DatabaseManager manager;
    private final View view;

    public DisconnectFromDB(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("disconnect");
    }

    @Override
    public void process(String command) {
        manager.disconnectFromDB();
        view.write("Disconnected");
    }
}
