package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 15.05.2016.
 */
public class Help implements Command {

    private View view;
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";


    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {

        view.write("\n======== SQLCmd  Help ======== \n");
        view.write("Существующие команды:\n");
        view.write("==============================");

        view.write(ANSI_BLUE+"\tconnect|databaseName|userName|password");
        view.write(ANSI_RESET+"\t\tдля подключения к базе данных, с которой будем работать");

        view.write(ANSI_BLUE+"\ttables");
        view.write(ANSI_RESET+"\t\tдля получения списка всех таблиц базы, к которой подключились");

        view.write(ANSI_BLUE+"\tclear|tableName");
        view.write(ANSI_RESET+"\t\tдля очистки всей таблицы"); // TODO а если юзер случайно ввел команду? Может переспросить его?

        view.write(ANSI_BLUE+"\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write(ANSI_RESET+"\t\tдля создания записи в таблице");

        view.write(ANSI_BLUE+"\tfind|tableName");
        view.write(ANSI_RESET+"\t\tдля получения содержимого таблицы 'tableName'");

        view.write(ANSI_BLUE+"\thelp");
        view.write(ANSI_RESET+"\t\tдля вывода этого списка на экран");

        view.write(ANSI_BLUE+"\texit");
        view.write(ANSI_RESET+"\t\tдля выхода из программы");
        view.write("==============================");
    }
}

