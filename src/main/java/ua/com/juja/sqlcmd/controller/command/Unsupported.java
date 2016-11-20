package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;


public class Unsupported extends Command {

    public Unsupported(View view) {
        super(view);
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String input) {
        view.write("Command is not exist: " + input);
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
