package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by Серый on 13.05.2016.
 */
public class MainController {

    private View view;
    private DatabaseManager manager;

    public  MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run () {
        connectToDb();
    }

    private  void connectToDb() {


        view.write("Привет юзер34234!");
        view.write("Успех!");view.write("Введи пожалуйста имя базы данных, имя пользователя и пароль в формате: database|userName|password");

        while (true) {
            String string = view.read();

            String[] data = string.split("[|]");
            String databaseName = data[0];
            String userName = data[1];
            String password = data[2];

            try {
                manager.connect(databaseName, userName, password);


                break;
            } catch (Exception e) {
                view.write("Неудача! по причине: " + e.getMessage() + " "+ e.getCause().getMessage());
                view.write("Повтори попытку");
            }


        }
        view.write("Успех!");
    }
}
