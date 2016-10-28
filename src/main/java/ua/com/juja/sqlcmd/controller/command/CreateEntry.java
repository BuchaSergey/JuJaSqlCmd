package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.utilCheckInput.CheckInput;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.LinkedHashMap;
import java.util.Map;


public class CreateEntry implements Command {

    private DatabaseManager manager;
    private View view;

    public CreateEntry(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createEntry|");
    }

    @Override
    public void process(CheckInput command) {
        command.parametersValidation(format());

        String[] data = command.getParameters();

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
        return "create entry in tables";
    }

    @Override
    public String format() {
        return "createEntry|tableName|column1|value1|column2|value2|...|columnN|valueN";
    }
}
