package Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dataset {
    public Table[] tables;

    public Dataset(Table[] tables) {
        this.tables = tables;
    }

    public List<List<List<String>>> sortAndRemoveDuplicates() {

        List<List<List<String>>> listBasedDataset = new ArrayList<>(tables.length);
        for (Table table: tables) {
            List<List<String>> listBasedTable = new ArrayList<>(table.numCols);
            for (String[] column : table.values) {
                listBasedTable.add(Arrays.stream(column).sorted().distinct().toList());
            }
            listBasedDataset.add(listBasedTable);
        }

        return listBasedDataset;
    }

    public void printStatistics() {
        System.out.println("Dataset contains " + tables.length + " tables totaling " +
                Arrays.stream(tables).mapToInt(x -> x.values.length).sum() + " columns.");
    }
}
