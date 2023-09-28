package Data;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private final char delimiter;

    public Loader(char delimiter) {
        this.delimiter = delimiter;
    }

    public Table loadTable(Path path) throws IOException {

        // int cast since we do not expect more than 2.4B lines
        // a dataset of this size would most likely not fit into a systems main memory anyway

        int rowCount = (int) Files.lines(path).count();

        BufferedReader br = new BufferedReader(new FileReader(path.toString()));

        CSVReader reader = new CSVReader(br, delimiter);

        String[] columnNames = reader.readNext();
        int columnCount = columnNames.length;

        List<String[]> v = new ArrayList<>(rowCount);
        String[] nextRow;
        while ((nextRow = reader.readNext()) != null) {
            if (nextRow.length == columnCount) {
                v.add(nextRow);
            }
        }
        String[][] values = v.toArray(new String[0][0]);

        String[][] transposed_values = new String[values[0].length][values.length];

        for (int row = 0; row < values.length; row++) {
            for (int col = 0; col < values[0].length; col++) {
                transposed_values[col][row] = values[row][col];
            }
        }

        return new Table(columnNames, transposed_values, path.getFileName().toString());
    }

    public Dataset loadDataset(String folderPath, int minRows) {

        // get a list of all csv files in the folder
        File[] files = new File(folderPath).listFiles(obj -> obj.isFile() && (obj.getName().endsWith(".csv") || obj.getName().endsWith(".tsv")));
        assert files != null;

        List<Table> tables = new ArrayList<>(files.length);

        for (File f : files) {
            try {
                if ((int) Files.lines(f.toPath()).count() >= minRows) {
                    tables.add(loadTable(f.toPath()));
                }
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                //System.out.println("Unable to load table: " + f.getName());
            }
        }

        return new Dataset(tables);

    }
}
