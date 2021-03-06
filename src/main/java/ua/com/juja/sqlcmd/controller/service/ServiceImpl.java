package ua.com.juja.sqlcmd.controller.service;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.PostgreSQLManager;

import java.util.Arrays;
import java.util.List;

public class ServiceImpl implements Service {

    private DatabaseManager manager;

    public ServiceImpl() {
        manager = new PostgreSQLManager();
    }

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect");
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        manager.connect(databaseName, userName, password);
    }

}
