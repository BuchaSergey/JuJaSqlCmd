package ua.com.juja.sqlcmd.model;

/**
 * Created by Seriy on 29.07.2016.
 */
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Set;
public class TableConstructor {

    private Set<String> columns;
    private Table table;
    private List<DataSet> tableData;

    public TableConstructor(Set<String> columns, List<DataSet> tableData) {
        this.columns = columns;
        this.tableData = tableData;
        table = new Table(columns.size(), BorderStyle.CLASSIC, ShownBorders.ALL);
    }

    public String getTableString() {
        build();
        return table.render();
    }

    private void build() {
        buildHeader();
        buildRows();
    }

    private void buildHeader() {
        for (String column: columns) {
            table.addCell(column);
        }
    }

    private void buildRows() {

        for (DataSet row: tableData) {
                for (Object value : row.getValues())
                table.addCell(value.toString());

        }
    }
}