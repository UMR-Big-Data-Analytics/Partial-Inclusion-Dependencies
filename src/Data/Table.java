package Data;

import java.util.Arrays;

public class Table {
    private final String[] columnNames;
    public String[][] values;
    public final int numCols;
    public String name;

    public Table(String[] columnNames, String[][] values, String name) {
        this.columnNames = columnNames;
        this.values = values;
        Arrays.stream(this.values).forEach(Arrays::sort);
        this.numCols = columnNames.length;
        this.name = name;
    }

    public String getColumnName(int position) {
        return columnNames[position];
    }
}


