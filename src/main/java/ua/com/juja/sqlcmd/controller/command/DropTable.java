package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class DropTable implements Command {
    private final DatabaseManager manager;
    private final View view;

    public DropTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropTable|");
    }

    @Override
    public void process(CheckInput command) {
        command.parametersValidation(format());
        String[] data = command.getParameters();

        manager.dropTable(data[1]);
        view.write(String.format("Table '%s' was deleted.", data[1]));
    }

    @Override
    public String description() {
        return "delete table";
    }

    @Override
    public String format() {
        return "dropTable|tableName";
    }
}
