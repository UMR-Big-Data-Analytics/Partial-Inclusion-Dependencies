package Data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Table {
    private final String[] columnNames;
    public List<List<String>> values;
    public final int numCols;
    public String name;

    public Table(String[] columnNames, List<List<String>> values, String name) {
        this.columnNames = columnNames;
        this.values = values;
        // values.forEach(x -> x.sort(Comparator.naturalOrder()));
        this.numCols = columnNames.length;
        this.name = name;
    }

    public String getColumnName(int position) {
        return columnNames[position];
    }
}


