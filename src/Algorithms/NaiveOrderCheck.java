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
                                //System.out.println("match: " + matches + " " + testValue + " " + val);
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
                                    //System.out.println("test smaller: " + matches + " " + testValue + " " + val);
                                    leftoverMisses--;
                                    testValue = testIterator.next();
                                }

                                if (testValue.equals(val)) {
                                    matches++;
                                    // check if we have reached enough matches
                                    // if so, break since all remaining checks would not change the outcome anymore
                                    if (matches >= minRequiredMatches) {
                                        pINDs++;
                                        break;
                                    }

                                    //System.out.println("match: " + matches + " " + testValue + " " + val);
                                    if (testIterator.hasNext()) {
                                        testValue = testIterator.next();
                                    } else {
                                        // if there is no value remaining there can not be any more matches
                                        break;
                                    }
                                }

                                if (testValue.compareTo(val) < 0) {
                                    // we have run out of test values, no more matches are possible
                                    break;
                                }
                            } else { // testValue > val
                                // if the current testValue is bigger than the column value, we want to test the next
                                // column value without increasing the testValue
                                //System.out.println("test bigger: " + matches + " " + testValue + " " + val);
                                leftoverMisses--;
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
        System.out.println("Found " + pINDs + " pINDs using a threshold of " + threshold +
                ". Took: " + elapsedTime / 1000 + "sec");
    }

    public static void main(String[] args) {
        Loader loader = new Loader(",");
        Dataset dataset = loader.loadDataset("data/T2D Complete gold standard", 20);

        dataset.printStatistics();

        for (double t :new double[]{1.0, 0.95, 0.9, 0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55, 0.5}) {
            findUnaryPartialInclusionDependencies(dataset, t);
        }

       /*
        minRows = 20 - T2D
        Found 37332 pINDs using a threshold of 1.0. Took: 11sec
        Found 40017 pINDs using a threshold of 0.95. Took: 9sec
        Found 43611 pINDs using a threshold of 0.9. Took: 9sec
        Found 46109 pINDs using a threshold of 0.85. Took: 8sec
        Found 48851 pINDs using a threshold of 0.8. Took: 8sec
        Found 52434 pINDs using a threshold of 0.75. Took: 8sec
        Found 54720 pINDs using a threshold of 0.7. Took: 8sec
        Found 58764 pINDs using a threshold of 0.65. Took: 8sec
        Found 61793 pINDs using a threshold of 0.6. Took: 8sec
        Found 64884 pINDs using a threshold of 0.55. Took: 9sec
        Found 87207 pINDs using a threshold of 0.5. Took: 9sec
        */
    }
}
