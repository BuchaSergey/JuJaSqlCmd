package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.view.View;


public class Exit extends Command {

    private final View view;

    public Exit(View view) {
        this.view = view;
    }


    @Override
    public void process(String input) {
        view.write("Good Bye!");
        throw new ExitException();
    }

    @Override
    public String description() {
        return "exit from app";
    }

    @Override
    public String commandFormat() {
        return "exit";
    }
}

