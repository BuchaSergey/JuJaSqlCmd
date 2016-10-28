package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.view.View;


public class Exit implements Command {

    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(CheckInput command) {
        view.write("Good Bye!");
        throw new ExitException();
    }

    @Override
    public String description() {
        return "Exit from app";
    }

    @Override
    public String format() {
        return "exit";
    }
}

