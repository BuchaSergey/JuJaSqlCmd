package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;


public class DropTable extends Command {

    public DropTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String tableName = getParameters(input)[1];

        manager.dropTable(tableName);
        view.write(String.format("Table '%s' was deleted.", tableName));
    }

    @Override
    public String description() {
        return "delete table";
    }

    @Override
    public String commandFormat() {
        return "dropTable|tableName";
    }
}
