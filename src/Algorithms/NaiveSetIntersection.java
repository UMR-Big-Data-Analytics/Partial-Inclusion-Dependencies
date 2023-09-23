package Algorithms;

import Data.Dataset;
import Data.Loader;

import java.util.*;

public class NaiveSetIntersection {

    public static void findUnaryPartialInclusionDependencies(Dataset dataset, double threshold) {

        long startTime = System.currentTimeMillis();

        List<List<HashSet<String>>> l = Arrays.stream(dataset.tables)
                .map(x -> Arrays.stream(x.values)
                        .map(y -> new HashSet<>(List.of(y))).toList()).toList();

        int found = 0;

        for (int tableIndex = 0; tableIndex < dataset.tables.length; tableIndex++) {
            for (int columnIndex = 0; columnIndex < dataset.tables[tableIndex].numCols; columnIndex++) {
                //System.out.println(Arrays.toString(dataset.tables[tableIndex].values[columnIndex]));
                Set<String> currentSet = l.get(tableIndex).get(columnIndex);
                double currentSize = currentSet.size();

                int testTableIndex = 0;
                int testColumnIndex;
                for (List<HashSet<String>> table: l) {
                    testColumnIndex = 0;
                    for (Set<String> testColumn: table) {
                        if (testTableIndex == tableIndex && testColumnIndex == columnIndex) {
                            continue;
                        }
                        Set<String> copy = new HashSet<>(currentSet);
                        copy.retainAll(testColumn);
                        int intersect = copy.size();
                        if (intersect/currentSize >= threshold) {
                            found++;
                            // System.out.println(tableIndex + "." + columnIndex + " c " +
                            // testTableIndex + "." + testColumnIndex);
                        }
                        testColumnIndex++;
                    }
                    testTableIndex++;
                }
            }
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Found " + found + " pINDs using a threshold of " + threshold +
                ". Took:" + elapsedTime/1000 + "sec");
    }

    public static void main(String[] args) {
        Loader loader = new Loader(",");
        Dataset dataset = loader.loadDataset("data/T2D Complete gold standard");

        dataset.printStatistics();

        findUnaryPartialInclusionDependencies(dataset, 0.90);
    }

    /*
    Found 250146 pINDs using a threshold of 1.0. Took:135sec
    Found 252825 pINDs using a threshold of 0.95. Took:147sec
    Found 258703 pINDs using a threshold of 0.9. Took:140sec
     */
}


