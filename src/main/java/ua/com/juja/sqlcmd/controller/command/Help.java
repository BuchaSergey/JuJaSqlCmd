package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class Help implements Command {

    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";
    private final View view;
    Command[] commands;

    public Help(View view, DatabaseManager manager) {
        this.view = view;
        this.commands = new Command[]{
                new Connect(manager, view),
                new CreateDatabase(manager, view),
                new CreateTable(manager, view),
                new GetDatabasesNames(manager, view),
                new DisconnectFromDB(manager, view),
                new DropDB(manager, view),
                new DropTable(manager, view),
                this,
                new Exit(view),
                new IsConnected(manager, view),
                new GetTablesNames(manager, view),
                new ClearTable(manager, view),
                new CreateEntry(manager, view),
                new GetTableData(manager, view),
                new Unsupported(view)
        };
    }


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

        for (Command command1 : commands) {
            view.write(ANSI_BLUE + "\t" + command1.format()
                    + ANSI_RESET + "\t\t" +command1.description());
        }
/*
        view.write(ANSI_BLUE + "\tconnect|databaseName|userName|password");
        view.write(ANSI_RESET + "\t\tдля подключения к базе данных, с которой будем работать");

        view.write(ANSI_BLUE + "\tdatabases");
        view.write(ANSI_RESET + "\t\tсписок баз данных");

        view.write(ANSI_BLUE + "\tdisconnect");
        view.write(ANSI_RESET + "\t\tдля отключение от БД");

        view.write(ANSI_BLUE + "\tcreateDB|databaseName");
        view.write(ANSI_RESET + "\t\tдля создания база данных");

        view.write(ANSI_BLUE + "\tdropDB|databaseName");
        view.write(ANSI_RESET + "\t\tдля удаления база данных");

        view.write(ANSI_BLUE + "\ttables");
        view.write(ANSI_RESET + "\t\tдля получения списка всех таблиц базы, к которой подключились");

        view.write(ANSI_BLUE + "\tcreateTable|tableName(name text, pass text, id serial, PRIMARY KEY(id))");
        view.write(ANSI_RESET + "\t\tдля создания таблицы. ");

        view.write(ANSI_BLUE + "\tcreateEntry|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write(ANSI_RESET + "\t\tдля создания записи в таблице");

        view.write(ANSI_BLUE + "\tclear|tableName");
        view.write(ANSI_RESET + "\t\tдля очистки всей таблицы");

        view.write(ANSI_BLUE + "\tdropTable|tableName");
        view.write(ANSI_RESET + "\t\tдля удаления таблицы таблицы");

        view.write(ANSI_BLUE + "\tshow|tableName");
        view.write(ANSI_RESET + "\t\tдля получения содержимого таблицы");

        view.write(ANSI_BLUE + "\thelp");
        view.write(ANSI_RESET + "\t\tдля вывода этого списка на экран");

        view.write(ANSI_BLUE + "\texit");
        view.write(ANSI_RESET + "\t\tдля выхода из программы");*/
        view.write("==============================");
    }

    @Override
    public String description() {
        return "list of existing commands";
    }

    @Override
    public String format() {
        return "help";
    }
}

