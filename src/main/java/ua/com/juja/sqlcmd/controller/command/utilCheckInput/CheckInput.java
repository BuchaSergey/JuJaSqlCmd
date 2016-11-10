package ua.com.juja.sqlcmd.controller.command.utilCheckInput;


public class CheckInput {

    private final String command;

    public CheckInput(String command) {
        this.command = command;
    }

    public String[] getParameters() {
        return command.split("\\|");
    }

    public void pairValidation(String commandFormat) {
        if (command.split("\\|").length % 2 != 0) {
            throw new IllegalArgumentException("Invalid input, you must enter an even " +
                    "number of parameters in the following format: " + commandFormat);
        }
    }

    public void parametersValidation(String format) {
        int normalLength = format.split("\\|").length;
        if (normalLength != getInputLength()) {
            throw new IllegalArgumentException(String.format("Incorrect number of parameters " +
                    "separated by '|', expected %s but was: %s", normalLength, getInputLength()));
        }
    }

    @Override
    public String toString() {
        return command;
    }

    private int getInputLength() {
        return command.split("\\|").length;
    }
}


