package ua.com.juja.sqlcmd.controller.command;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public abstract class Command {

    protected DatabaseManager manager;
    protected View view;

    public Command(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    public Command(View view) {
        this.view = view;
    }

    public Command() {
    }

    public abstract void process(String input);

    public abstract String commandFormat();

    public abstract String description();

    public boolean canProcess(String input) {
        String[] parametersCommandFormat = splitInput(commandFormat());
        String[] parametersInput = splitInput(input);
        return parametersInput[0].toLowerCase().equals(parametersCommandFormat[0].toLowerCase());
    }

    public void pairValidation(String commandFormat) {
        if (commandFormat.split("\\|").length % 2 != 0) {
            throw new IllegalArgumentException("Invalid input, you must enter an even " +
                    "number of parameters in the following format: " + commandFormat);
        }
    }

    public void parametersValidation(String input) {
        int formatLength = parametersLength(commandFormat());
        int inputLength = parametersLength(input);

        if (formatLength != inputLength) {
            throw new IllegalArgumentException(String.format("Incorrect number of parameters " +
                    "separated by '|', expected %s but was: %s", commandFormat(), input));
        }
    }



    public String[] splitInput(String input) {
        return input.split("\\|");
    }

    public int parametersLength(String input) {
        return input.split("\\|").length;
    }


}
