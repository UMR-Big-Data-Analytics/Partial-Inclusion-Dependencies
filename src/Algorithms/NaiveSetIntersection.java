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
                int minRequiredMatches = (int) Math.ceil((double) currentSet.size() * threshold);

                int testTableIndex = 0;
                int testColumnIndex;
                for (List<HashSet<String>> table: l) {
                    testColumnIndex = 0;
                    for (Set<String> testColumn: table) {
                        if (testTableIndex == tableIndex && testColumnIndex == columnIndex) {
                            testColumnIndex++;
                            continue;
                        }
                        Set<String> copy = new HashSet<>(currentSet);
                        copy.retainAll(testColumn);
                        int intersect = copy.size();
                        if (intersect >= minRequiredMatches) {
                            found++;
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
                ". Took: " + elapsedTime/1000 + "sec");
    }

    public static void main(String[] args) {
        Loader loader = new Loader(",");
        Dataset dataset = loader.loadDataset("data/T2D Complete gold standard", 20);

        dataset.printStatistics();

        for (double t :new double[]{0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55, 0.5}) {
            findUnaryPartialInclusionDependencies(dataset, t);
        }
    }

    /*
    minRows = 20 - T2D
    Found 37332 pINDs using a threshold of 1.0. Took: 56sec
    Found 40017 pINDs using a threshold of 0.95. Took: 60sec
    Found 43611 pINDs using a threshold of 0.9. Took: 58sec
    Found 46109 pINDs using a threshold of 0.85. Took: 60sec
    Found 48851 pINDs using a threshold of 0.8. Took: 56sec
    Found 52434 pINDs using a threshold of 0.75. Took: 56sec
    Found 54720 pINDs using a threshold of 0.7. Took: 56sec
    Found 58764 pINDs using a threshold of 0.65. Took: 56sec
    Found 61793 pINDs using a threshold of 0.6. Took: 56sec
    Found 64884 pINDs using a threshold of 0.55. Took: 56sec
    Found 87207 pINDs using a threshold of 0.5. Took: 56sec
     */
}


