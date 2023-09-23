package Data;

import java.util.Arrays;

public class Dataset {
    public Table[] tables;

    public Dataset(Table[] tables) {
        this.tables = tables;
    }

    public void printStatistics() {
        System.out.println("Dataset contains " + tables.length + " tables totaling " +
                Arrays.stream(tables).mapToInt(x -> x.values.length).sum() + " columns.");
    }
}
