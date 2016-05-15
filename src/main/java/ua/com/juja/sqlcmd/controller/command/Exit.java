package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 15.05.2016.
 */
public class Exit implements Command{

    private View view;

   public  Exit(View view) {
       this.view = view;
   }

    @Override
    public boolean canProcess(String command) {
        return "exit".equals(command);
    }

    @Override
    public void process(String command) {
        view.write("До скорой встречи!");
        System.exit(0);

    }
}
