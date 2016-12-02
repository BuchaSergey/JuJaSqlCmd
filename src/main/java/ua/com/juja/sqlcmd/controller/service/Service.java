package ua.com.juja.sqlcmd.controller.service;

import java.util.List;

/**
 * Created by Sergio on 02.12.2016.
 */
public interface Service {

    List<String> commandsList();

    void connect(String databaseName, String userName, String password);
}

