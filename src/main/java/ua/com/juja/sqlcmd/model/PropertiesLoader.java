package ua.com.juja.sqlcmd.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertiesLoader {

    public static final String CONFIG_SQLCMD_PROPERTIES = "config/sqlcmd.properties";
    private Properties properties;


    public PropertiesLoader() {
        FileInputStream fileInput = null;
        properties = new Properties();
        File file = new File(CONFIG_SQLCMD_PROPERTIES);
        try {
            fileInput = new FileInputStream(file);
            properties.load(fileInput);
        } catch (Exception e) {
            System.out.println("Error loading config " + file.getAbsolutePath());
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    // do nothing;
                }
            }
        }
    }

    public Configuration getConfiguration() {
        return new Configuration(
                getDatabaseName(),
                getServerName(),
                getDatabasePort(),
                getUserName(),
                getDriver(),
                getPassword()
        );
    }


    public String getServerName() {
        return properties.getProperty("database.server.name");
    }

    public String getDatabaseName() {
        return properties.getProperty("database.name");
    }

    public String getDatabasePort() {
        return properties.getProperty("database.port");
    }

    public String getDriver() {
        return properties.getProperty("database.jdbc.driver");
    }

    public String getUserName() {
        return properties.getProperty("database.user.name");
    }

    public String getPassword() {
        return properties.getProperty("database.user.password");
    }


}


