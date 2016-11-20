package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public abstract class Command {

    DatabaseManager manager;
    View view;

    Command(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    Command(View view) {
        this.view = view;
    }

    Command() {
    }

    public abstract void process(String input);

    protected abstract String commandFormat();

    protected abstract String description();

    public boolean canProcess(String input) {
        String[] parametersCommandFormat = getParameters(commandFormat());
        String[] parametersInput = getParameters(input);
        return parametersInput[0].equalsIgnoreCase(parametersCommandFormat[0]);
    }

    void validationPairParameters(String input) {
        if (parametersLength(input) % 2 != 0) {
            throw new IllegalArgumentException("Invalid input, you must enter an even " +
                    "number of parameters in the following format: " + commandFormat());
        }
    }

    void validationParameters(String input) {
        int formatLength = parametersLength(commandFormat());
        int inputLength = parametersLength(input);

        if (formatLength != inputLength) {
            throw new IllegalArgumentException(String.format("Incorrect number of parameters " +
                    "separated by '|', expected %s but was: %s", formatLength, inputLength));
        }
    }

    String[] getParameters(String input) {
        return input.split("\\|");
    }

    private int parametersLength(String input) {
        return input.split("\\|").length;
    }


}
