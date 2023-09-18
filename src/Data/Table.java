package Data;

public class Table {
    private final String[] columnNames;
    public String[][] values;

    public Table(String[] columnNames, String[][] values) {
        this.columnNames = columnNames;
        this.values = values;
    }

    public String getColumnName(int position) {
        return columnNames[position];
    }
}


