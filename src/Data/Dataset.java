package Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dataset {
    public List<Table> tables;

    public Dataset(List<Table> tables) {
        this.tables = tables;
    }

    public List<List<List<String>>> sortAndRemoveDuplicates() {

        List<List<List<String>>> listBasedDataset = new ArrayList<>(tables.size());
        for (Table table: tables) {
            List<List<String>> listBasedTable = new ArrayList<>(table.numCols);
            for (List<String> column : table.values) {
                listBasedTable.add(column.stream().sorted().distinct().toList());
            }
            listBasedDataset.add(listBasedTable);
        }

        return listBasedDataset;
    }

    public void printStatistics() {
        System.out.println("Dataset contains " + tables.size() + " tables totaling " +
                tables.stream().mapToInt(x -> x.numCols).sum() + " columns and " +
                tables.stream().mapToInt(x -> x.values.get(0).size()).sum() + " rows.");
    }
}
