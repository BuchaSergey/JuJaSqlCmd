package ua.com.juja.sqlcmd.model;

import java.util.*;

/**
 * Created by indigo on 21.08.2015.
 */
public class DataSetImpl implements DataSet {

    private Map<String, Object> data = new LinkedHashMap();

    @Override
    public void put(String name, Object value) {
        data.put(name, value);
    }

    @Override
    public List<Object> getValues() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Set<String> getNames() {
        return data.keySet();
    }

    @Override
    public Object get(String name) {
        return data.get(name);
    }

    @Override
    public void updateFrom(DataSet newValue) {
        Set<String> columns = newValue.getNames();
        for (String name : columns) {
            Object data = newValue.get(name);
            this.put(name, data);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "names:" + getNames().toString() + ", " +
                "values:" + getValues().toString() +
                "}";
    }
}
