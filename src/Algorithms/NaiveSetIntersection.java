package Algorithms;

import Data.Dataset;
import Data.Loader;
import Utils.Utils;

import java.util.*;

public class NaiveSetIntersection {

    public static void findUnaryPartialInclusionDependencies(Dataset dataset, double threshold) {

        long startTime = System.currentTimeMillis();

        List<List<HashSet<String>>> l = dataset.tables.stream()
                .map(x -> x.values.stream()
                        .map(HashSet::new).toList()).toList();

        long setUpTime = System.currentTimeMillis() - startTime;

        int pINDs = 0;

        for (int tableIndex = 0; tableIndex < dataset.tables.size(); tableIndex++) {
            for (int columnIndex = 0; columnIndex < dataset.tables.get(tableIndex).numCols; columnIndex++) {
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
                            pINDs++;
                        }
                        testColumnIndex++;
                    }
                    testTableIndex++;
                }
            }
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(pINDs + " pINDs,\tt=" + threshold +
                ",\tTook: " + Utils.printMillTime(elapsedTime) + " | " + Utils.printMillTime(setUpTime));
    }

    public static void main(String[] args) {
        Loader loader = new Loader(',');
        Dataset dataset = loader.loadDataset("data/T2D", 20);

        dataset.printStatistics();

        for (double t :new double[]{1.0, 0.95, 0.9, 0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55, 0.5}) {
            findUnaryPartialInclusionDependencies(dataset, t);
        }
    }

    /*
    minRows = 20 - T2D
    Dataset contains 617 tables totaling 3017 columns and 194320 rows.
    41715 pINDs,	t=1.0,	Took: 00h 01m 31s 0132ms | 00h 00m 00s 0046ms
    44952 pINDs,	t=0.95,	Took: 00h 01m 31s 0677ms | 00h 00m 00s 0016ms
    49365 pINDs,	t=0.9,	Took: 00h 01m 31s 0379ms | 00h 00m 00s 0016ms
    52526 pINDs,	t=0.85,	Took: 00h 01m 33s 0134ms | 00h 00m 00s 0016ms
    56374 pINDs,	t=0.8,	Took: 00h 01m 31s 0617ms | 00h 00m 00s 0016ms
    60718 pINDs,	t=0.75,	Took: 00h 01m 31s 0443ms | 00h 00m 00s 0016ms
    63618 pINDs,	t=0.7,	Took: 00h 01m 31s 0792ms | 00h 00m 00s 0016ms
    68310 pINDs,	t=0.65,	Took: 00h 01m 34s 0568ms | 00h 00m 00s 0015ms
    72108 pINDs,	t=0.6,	Took: 00h 01m 33s 0857ms | 00h 00m 00s 0016ms
    76042 pINDs,	t=0.55,	Took: 00h 01m 34s 0032ms | 00h 00m 00s 0015ms
    104181 pINDs,	t=0.5,	Took: 00h 01m 34s 0335ms | 00h 00m 00s 0015ms
     */
}


