package ua.com.juja.sqlcmd.view;

import java.util.Scanner;

/**
 * Created by Серый on 13.05.2016.
 */
public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
    }
}