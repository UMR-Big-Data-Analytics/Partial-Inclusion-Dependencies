package Data;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private final char delimiter;
    private String tempPath = "/temp/";

    public Loader(char delimiter) {
        this.delimiter = delimiter;
    }

    public Loader(char delimiter, String tempPath) {
        this.delimiter = delimiter;
        this.tempPath = tempPath;
    }

    public Table loadTable(Path path) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(path.toString()));

        String[] nextRow;
        CSVReader reader = new CSVReader(br, delimiter);
        String[] columnNames = reader.readNext();
        int columnCount = columnNames.length;
        reader.readNext(); // skip column names
        List<List<String>> values = new ArrayList<>();
        int lines = (int) Files.lines(path).count();
        System.out.println(lines);
        for (int i = 0; i < columnCount; i++) {
            values.add(new ArrayList<>(lines));
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
        Runtime.getRuntime().gc();

        return new Table(columnNames, values, path.getFileName().toString());
    }

    public Dataset loadDataset(String folderPath) {

        // get a list of all csv files in the folder
        File[] files = new File(folderPath).listFiles(obj -> obj.isFile() && (obj.getName().endsWith(".csv") || obj.getName().endsWith(".tsv")));
        assert files != null;

        List<Table> tables = new ArrayList<>(files.length);

        for (File f : files) {
            try {
                Table t = loadTable(f.toPath());

                if (t != null) {
                    tables.add(t);
                }
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Unable to load table: " + f.getName());
            }
        }

        return new Dataset(tables);

    }

    private int getMetadataTag(String value) {

        try {
            Integer.parseInt(value);
            return 1;
        } catch (NumberFormatException ignored) {}

        try {
            Long.parseLong(value);
            return 2;
        } catch (NumberFormatException ignored) {}

        try {
            Double.parseDouble(value);
            return 3;
        }
        catch (NumberFormatException ignored) {}

        try {
            Date.from(Instant.parse(value));
            return 3;
        }
        catch (NumberFormatException ignored) {}
    }
}
