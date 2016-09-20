package ua.com.juja.sqlcmd.model;

import java.util.List;
import java.util.Set;


public interface DataSet {

    void putNewValueDataSet(String name, Object value);
    void updateFrom(DataSet newValue);

    List<Object> getValues();
    Set<String> getNames();
    Object get(String name);
}
