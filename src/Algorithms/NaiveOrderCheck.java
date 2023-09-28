package Algorithms;

import Data.Dataset;
import Data.Loader;
import Utils.Utils;

import java.util.List;
import java.util.ListIterator;

public class NaiveOrderCheck {

    public static void findUnaryPartialInclusionDependencies(Dataset dataset, double threshold) {

        long startTime = System.currentTimeMillis();

        List<List<List<String>>> data = dataset.sortAndRemoveDuplicates();

        long setUpTime = System.currentTimeMillis() - startTime;

        int pINDs = 0;
        int tableIndex = 0;
        int columnIndex;
        for (List<List<String>> currentTable : data) {
            columnIndex = 0;
            for (List<String> currentColumn : currentTable) {

                int minRequiredMatches = (int) Math.ceil((double) currentColumn.size() * threshold);

                int testTableIndex = 0;
                int testColumnIndex;
                for (List<List<String>> testTable : data) {
                    testColumnIndex = 0;
                    for (List<String> testColumn : testTable) {

                        if (tableIndex == testTableIndex && columnIndex == testColumnIndex) {
                            // disregard identity comparison
                            testColumnIndex++;
                            continue;
                        }
                        if (minRequiredMatches > testColumn.size()) {
                            // since all values are unique, there needs to be at least as many as the minimum required
                            testColumnIndex++;
                            continue;
                        }
                        ListIterator<String> testIterator = testColumn.listIterator();

                        if (!testIterator.hasNext()) {
                            // if the test column is empty it can not be a super set
                            // this case should never happen in a proper dataset
                            testColumnIndex++;
                            continue;
                        }

                        // order based overlap check
                        int leftoverMisses = testColumn.size() - minRequiredMatches;

                        // keep track of the number of matching values
                        int matches = 0;
                        int misses = 0;
                        String testValue = testIterator.next();
                        for (String val : currentColumn) {

                            int comp = testValue.compareTo(val);

                            if (comp == 0) { // equal
                                // if both values are equal we will set the test value to the next element
                                // and continue with the next column value
                                matches++;
                                // check if we have reached enough matches
                                // if so, break since all remaining checks would not change the outcome anymore
                                if (matches >= minRequiredMatches) {
                                    pINDs++;
                                    break;
                                }
                                if (testIterator.hasNext()) {
                                    testValue = testIterator.next();
                                } else {
                                    // if there is no value remaining there can not be any more matches
                                    break;
                                }

                            } else if (comp < 0) { // testValue < val
                                // if the testValue is 'smaller' than the current column value we need to skip values
                                // until we reach a value that is at least as big as the current value
                                while (testValue.compareTo(val) < 0 && testIterator.hasNext()) {
                                    leftoverMisses--;
                                    if (leftoverMisses < 0) break;
                                    testValue = testIterator.next();
                                }
                                if (leftoverMisses < 0) break;

                                if (testValue.equals(val)) {
                                    matches++;
                                    // check if we have reached enough matches
                                    // if so, break since all remaining checks would not change the outcome anymore
                                    if (matches >= minRequiredMatches) {
                                        pINDs++;
                                        break;
                                    }
                                    if (testIterator.hasNext()) {
                                        testValue = testIterator.next();
                                    } else {
                                        // if there is no value remaining there can not be any more matches
                                        break;
                                    }
                                } else {
                                    misses++;
                                    if (currentColumn.size() - misses < minRequiredMatches - matches) break;
                                }

                                if (testValue.compareTo(val) < 0) {
                                    // we have run out of test values, no more matches are possible
                                    break;
                                }
                            } else { // testValue > val
                                // if the current testValue is bigger than the column value, we want to test the next
                                // column value without increasing the testValue
                                misses++;
                                if (currentColumn.size() - misses < minRequiredMatches - matches) break;
                            }


//                            if (leftoverMisses < 0) {
//                                break;
//                            }
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
        System.out.println(pINDs + " pINDs,\tt=" + threshold +
                ",\tTook: " + Utils.printMillTime(elapsedTime) + " | " + Utils.printMillTime(setUpTime));
    }

    public static void main(String[] args) {
        Loader loader = new Loader('\t');
        Dataset dataset = loader.loadDataset("data/Ensembl", 1);

        dataset.printStatistics();

        for (double t :new double[]{1.00, 0.95, 0.9, 0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55, 0.5}) {
            findUnaryPartialInclusionDependencies(dataset, t);
        }

       /*
        T2D - minRows: 20
        Dataset contains 694 tables totaling 3351 columns and 195434 rows.
        61814 pINDs,	t=1.0,	Took: 00h 00m 03s 0071ms | 00h 00m 00s 0054ms
        65090 pINDs,	t=0.95,	Took: 00h 00m 03s 0405ms | 00h 00m 00s 0032ms
        69913 pINDs,	t=0.9,	Took: 00h 00m 03s 0904ms | 00h 00m 00s 0030ms
        73589 pINDs,	t=0.85,	Took: 00h 00m 03s 0408ms | 00h 00m 00s 0023ms
        77572 pINDs,	t=0.8,	Took: 00h 00m 03s 0743ms | 00h 00m 00s 0028ms
        82273 pINDs,	t=0.75,	Took: 00h 00m 04s 0428ms | 00h 00m 00s 0030ms
        85366 pINDs,	t=0.7,	Took: 00h 00m 04s 0521ms | 00h 00m 00s 0022ms
        90820 pINDs,	t=0.65,	Took: 00h 00m 05s 0420ms | 00h 00m 00s 0030ms
        94922 pINDs,	t=0.6,	Took: 00h 00m 05s 0882ms | 00h 00m 00s 0028ms
        99161 pINDs,	t=0.55,	Took: 00h 00m 06s 0081ms | 00h 00m 00s 0031ms
        145388 pINDs,	t=0.5,	Took: 00h 00m 07s 0146ms | 00h 00m 00s 0030ms

        T2D - minRows: 150
        5862 pINDs,	t=1.0,	Took: 00h 00m 01s 0040ms | 00h 00m 00s 0047ms
        7054 pINDs,	t=0.95,	Took: 00h 00m 00s 0980ms | 00h 00m 00s 0016ms
        8636 pINDs,	t=0.9,	Took: 00h 00m 00s 0724ms | 00h 00m 00s 0028ms
        9512 pINDs,	t=0.85,	Took: 00h 00m 00s 0865ms | 00h 00m 00s 0022ms
        10541 pINDs,	t=0.8,	Took: 00h 00m 00s 0956ms | 00h 00m 00s 0016ms
        11683 pINDs,	t=0.75,	Took: 00h 00m 01s 0135ms | 00h 00m 00s 0016ms
        12609 pINDs,	t=0.7,	Took: 00h 00m 01s 0288ms | 00h 00m 00s 0022ms
        14082 pINDs,	t=0.65,	Took: 00h 00m 01s 0528ms | 00h 00m 00s 0032ms
        15048 pINDs,	t=0.6,	Took: 00h 00m 01s 0849ms | 00h 00m 00s 0020ms
        16095 pINDs,	t=0.55,	Took: 00h 00m 02s 0000ms | 00h 00m 00s 0023ms
        22889 pINDs,	t=0.5,	Took: 00h 00m 02s 0340ms | 00h 00m 00s 0020ms

        Catalog Data Gov - minRows: 150
        939 pINDs,	t=1.0,	Took: 00h 00m 32s 0323ms | 00h 00m 09s 0698ms
        1003 pINDs,	t=0.95,	Took: 00h 00m 45s 0487ms | 00h 00m 10s 0771ms
        1088 pINDs,	t=0.9,	Took: 00h 00m 36s 0897ms | 00h 00m 09s 0572ms
        1172 pINDs,	t=0.85,	Took: 00h 00m 38s 0832ms | 00h 00m 09s 0406ms
        1326 pINDs,	t=0.8,	Took: 00h 00m 41s 0981ms | 00h 00m 09s 0532ms
        1458 pINDs,	t=0.75,	Took: 00h 00m 43s 0478ms | 00h 00m 10s 0093ms
        1535 pINDs,	t=0.7,	Took: 00h 00m 45s 0982ms | 00h 00m 11s 0455ms
        1781 pINDs,	t=0.65,	Took: 00h 00m 45s 0780ms | 00h 00m 10s 0027ms
        1894 pINDs,	t=0.6,	Took: 00h 00m 46s 0908ms | 00h 00m 09s 0996ms
        1986 pINDs,	t=0.55,	Took: 00h 00m 48s 0138ms | 00h 00m 10s 0072ms
        2335 pINDs,	t=0.5,	Took: 00h 00m 49s 0159ms | 00h 00m 10s 0105ms
        */
    }
}
