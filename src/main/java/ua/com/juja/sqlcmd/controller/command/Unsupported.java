package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;


public class Unsupported implements Command {

    private final View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("Command is not exist: " + command);
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
