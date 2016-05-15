package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 13.05.2016.
 */
public class MainController {

    private Command[] commands;
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{
                new Connect (view, manager),
                new Help(view),
                new Exit(view),
                new IsConnected (view, manager),
                new List(view, manager),
                new Find(view, manager),
                new Unsupported (view)
        };
    }

    public void run() {

        view.write("Привет юзер");
        view.write("Введи пожалуйста имя базы данных, имя пользователя и пароль в формате: connect|database|userName|password");


        while (true) {

            String input = view.read();

            for (Command command : commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
            view.write("Введи команду или help для помощи:");
        }




}



}
