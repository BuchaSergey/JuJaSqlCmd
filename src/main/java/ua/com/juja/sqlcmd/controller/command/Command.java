package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;

public interface Command {
    boolean canProcess(String command);

    void process(CheckInput inputCommand);

    String description();
    String format();

}
