package ua.com.juja.sqlcmd.controller.command;

/**
 * Created by Серый on 15.05.2016.
 */
public interface Command {
    boolean canProcess(String command);

    void process(String command);

}
