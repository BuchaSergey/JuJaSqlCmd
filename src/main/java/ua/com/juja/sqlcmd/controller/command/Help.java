package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;


public class Help implements Command {

    private View view;
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";


    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {

        view.write("======== SQLCmd  Help ======== ");
        view.write("");
        view.write("Существующие команды:");
        view.write("");
        view.write("==============================");

        view.write(ANSI_BLUE + "\tconnect|databaseName|userName|password");
        view.write(ANSI_RESET + "\t\tдля подключения к базе данных, с которой будем работать");

        view.write(ANSI_BLUE + "\ttables");
        view.write(ANSI_RESET + "\t\tдля получения списка всех таблиц базы, к которой подключились");

        view.write(ANSI_BLUE + "\tclear|tableName");
        view.write(ANSI_RESET + "\t\tдля очистки всей таблицы");

        view.write(ANSI_BLUE + "\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write(ANSI_RESET + "\t\tдля создания записи в таблице");

        view.write(ANSI_BLUE + "\tshow|tableName");
        view.write(ANSI_RESET + "\t\tдля получения содержимого таблицы 'tableName'");

        view.write(ANSI_BLUE + "\thelp");
        view.write(ANSI_RESET + "\t\tдля вывода этого списка на экран");

        view.write(ANSI_BLUE + "\texit");
        view.write(ANSI_RESET + "\t\tдля выхода из программы");
        view.write("==============================");
    }
}

