package Algorithms;

import Data.Dataset;
import Data.Loader;

import java.util.List;
import java.util.ListIterator;

public class NaiveOrderCheck {

    public static void findUnaryPartialInclusionDependencies(Dataset dataset, double threshold) {

        long startTime = System.currentTimeMillis();

        List<List<List<String>>> data = dataset.sortAndRemoveDuplicates();

        int pINDs = 0;
        int tableIndex = 0;
        int columnIndex;
        for (List<List<String>> currentTable : data) {
            columnIndex = 0;
            for (List<String> currentColumn : currentTable) {

                int minRequiredMatches = (int) Math.ceil(currentColumn.size() * threshold);

                int testTableIndex = 0;
                int testColumnIndex;
                for (List<List<String>> testTable : data) {
                    testColumnIndex = 0;
                    for (List<String> testColumn : testTable) {
                        if (tableIndex == testTableIndex && columnIndex == testColumnIndex) {
                            continue;
                        }
                        if (minRequiredMatches > testColumn.size()) {
                            continue;
                        }
                        ListIterator<String> testIterator = testColumn.listIterator();

                        if (!testIterator.hasNext()) {
                            continue;
                        }

                        // order based overlap check
                        int leftoverMisses = testColumn.size() - minRequiredMatches;
                        int matches = 0;
                        String testValue = testIterator.next();
                        for (String val : currentColumn) {

                            int comp = testValue.compareTo(val); //
                            if (comp == 0) { // equal
                                matches++;
                                if (testIterator.hasNext()) {
                                    testValue = testIterator.next();
                                }
                            } else if (comp < 0) { // testValue < val
                                while (testValue.compareTo(val) < 0 && testIterator.hasNext()) {
                                    leftoverMisses--;
                                    testValue = testIterator.next();
                                }
                            } else { // testValue > val
                                leftoverMisses--;
                            }

                            if (matches >= minRequiredMatches) {
                                pINDs++;
                                System.out.println(dataset.tables[tableIndex].name +
                                        "." + dataset.tables[tableIndex].getColumnName(columnIndex) +
                                        " c " + dataset.tables[testTableIndex].name +
                                        "." + dataset.tables[testTableIndex].getColumnName(testColumnIndex));
                                break;
                            }
//                            if (leftoverMisses < 0) {
//                                break;
//                            }

                            //if (!testIterator.hasNext()) {break;}
                        }

                        testColumnIndex++;
                    }
                    testTableIndex++;
                }
                columnIndex++;
            }
            tableIndex++;
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Found " + pINDs + " pINDs using a threshold of " + threshold +
                ". Took:" + elapsedTime / 1000 + "sec");
    }

    public static void main(String[] args) {
        Loader loader = new Loader(",");
        Dataset dataset = loader.loadDataset("data/T2D Complete gold standard");

        dataset.printStatistics();

        findUnaryPartialInclusionDependencies(dataset, 0.9);

        // Found 49916 pINDs using a threshold of 0.9. Took:13sec
    }
}
