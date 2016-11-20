package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.LinkedHashMap;
import java.util.Map;


public class CreateEntry extends Command {

    public CreateEntry(DatabaseManager manager, View view) {
        super(manager, view);

    }

    @Override
    public void process(String input) {
        validationPairParameters(input);

        String[] data = getParameters(input);
        Map<String, Object> tableData = new LinkedHashMap<>();
        for (int index = 1; index < (data.length / 2); index++) {
            String column = data[index * 2];
            String value = data[index * 2 + 1];
            tableData.put(column, value);
        }
        manager.createEntry(data[1], tableData);
        view.write(String.format("An entry %s is created successfully in the table '%s'.", tableData, data[1]));
    }

    @Override
    public String description() {
        return "create entry in specific table";
    }

    @Override
    public String commandFormat() {
        return "createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN";
    }
}
