package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Help extends Command {

    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";

    private final View view;
    private final List<Command> commands;

    public Help(View view) {
        this.view = view;
        commands = new ArrayList<>(Arrays.asList(
                new Connect(manager, view),
                new CreateDatabase(manager, view),
                new CreateTable(manager, view),
                new ClearTable(manager, view),
                new CreateEntry(manager, view),
                new GetTablesNames(manager, view),
                new GetTableData(manager, view),
                new GetDatabasesNames(manager, view),
                new DropDB(manager, view),
                new DisconnectFromDB(manager, view),
                new DropTable(manager, view),
                this,
                new Exit(view)
        ));
    }

    @Override
    public void process(String input) {

        view.write("======== SQLCmd  Help ======== ");
        for (Command commands : this.commands) {
            view.write(ANSI_BLUE + "\t" + commands.commandFormat() + ANSI_RESET + "\t//" + commands.description());
        }
        view.write("==============================");
    }

    @Override
    public String description() {
        return "display list of existing commands";
    }

    @Override
    public String commandFormat() {
        return "help";
    }

}

