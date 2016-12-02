package ua.com.juja.sqlcmd.controller;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ua.com.juja.sqlcmd.controller.MainController;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PostgreSQLManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;


public class Main {
    public static void main(String[] args) {

        //Disable log4j
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.OFF);

        View view = new Console();
        DatabaseManager manager = new PostgreSQLManager();
        MainController controller = new MainController(view, manager);
        controller.run();
    }
}
