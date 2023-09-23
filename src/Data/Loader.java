package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private final String delimiter;

    public Loader(String delimiter) {
        this.delimiter = delimiter;
    }

    public Table loadTable(Path path) throws IOException {

        // int cast since we do not expect more than 2.4B lines
        // a dataset of this size would most likely not fit into a systems main memory anyway

        int rowCount = (int) Files.lines(path).count();

        BufferedReader br = new BufferedReader(new FileReader(path.toString()));

        String line = br.readLine();

        String[] columnNames = line.split(delimiter);
        int columnCount = columnNames.length;

        String[][] values = new String[columnCount][rowCount - 1];

        int currentRow = 0;
        while ((line = br.readLine()) != null) {
            String[] lineValues = line.split(delimiter);

            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                values[columnIndex][currentRow] = lineValues[columnIndex];
            }
            currentRow++;
        }

        return new Table(columnNames, values, path.toString());
    }

    public Dataset loadDataset(String folderPath) {

        // get a list of all csv files in the folder
        File [] files = new File(folderPath).listFiles(obj -> obj.isFile() && obj.getName().endsWith(".csv"));
        assert files != null;

        List<Table> tables = new ArrayList<>(files.length);

        for (File f: files) {
            try {
                tables.add(loadTable(f.toPath()));
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                //System.out.println("Unable to load table: " + f.getName());
            }
        }

        return new Dataset(tables.toArray(new Table[0]));

    }
}
