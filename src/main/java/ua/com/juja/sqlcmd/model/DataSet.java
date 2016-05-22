package ua.com.juja.sqlcmd.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Seriy on 22.05.2016.
 */
public interface DataSet {
    void put(String name, Object value);

    List<Object> getValues();

    Set<String> getNames();

    Object get(String name);

    void updateFrom(DataSet newValue);
}
