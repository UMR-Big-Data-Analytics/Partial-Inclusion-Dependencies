package Data;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Loader {
    private final char delimiter;

    public Loader(char delimiter) {
        this.delimiter = delimiter;
    }

    public Table loadTable(Path path, int minRows) throws IOException {

        // int cast since we do not expect more than 2.4B lines
        // a dataset of this size would most likely not fit into a systems main memory anyway

        BufferedReader br = new BufferedReader(new FileReader(path.toString()));

        String[] nextRow;
        CSVReader reader = new CSVReader(br, delimiter);
        String[] columnNames = reader.readNext();
        int columnCount = columnNames.length;

        int records = 0;
        while ((nextRow = reader.readNext()) != null) {
            if (nextRow.length == columnCount) {
                records++;
            }
        }
        br.close();
        reader.close();

        if (records < minRows) {
            return null;
        }

        br = new BufferedReader(new FileReader(path.toString()));
        reader = new CSVReader(br, delimiter);
        reader.readNext(); // skip column names
        List<List<String>> values = new LinkedList<>();

        for (int i = 0; i < columnCount; i++) {
            values.add(new LinkedList<>());
        }

        int colCounter;
        while ((nextRow = reader.readNext()) != null) {
            if (nextRow.length == columnCount) {
                colCounter = 0;
                for (List<String> col : values) {
                    col.add(nextRow[colCounter]);
                    colCounter++;
                }
            }
        }

        br.close();
        reader.close();

        return new Table(columnNames, values, path.getFileName().toString());
    }

    public Dataset loadDataset(String folderPath, int minRows) {

        // get a list of all csv files in the folder
        File[] files = new File(folderPath).listFiles(obj -> obj.isFile() && (obj.getName().endsWith(".csv") || obj.getName().endsWith(".tsv")));
        assert files != null;

        List<Table> tables = new ArrayList<>(files.length);

        for (File f : files) {
            try {
                Table t = loadTable(f.toPath(), minRows);

                if (t != null) {
                    tables.add(t);
                }
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Unable to load table: " + f.getName());
                System.out.println(e);
            }
        }

        return new Dataset(tables);

    }
}
