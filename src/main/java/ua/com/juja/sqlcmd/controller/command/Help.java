package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 15.05.2016.
 */
public class Help implements Command {
    private View view;

    public Help(View view) {

        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Существующие команды: ");

        view.write("\tconnect|databaseName|userName|password");
        view.write("\t\tдля подключения к базе данных, с которой будем работать");

        view.write("\tlist");
        view.write("\t\tдля получения списка всех таблиц базы, к которой подключились");

        view.write("\thelp");
        view.write("\t\tдля выводы этого списка на экран");

        view.write("\tfind|tableName");
        view.write("\t\tдля получения содержимого таблицы 'tableName'");

        view.write("\texit");
        view.write("\t\tдля выхода из программы");
    }
}
