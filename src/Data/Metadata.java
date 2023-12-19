package Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Metadata {

    private int total;
    private int distinct;
    private int[] types;
    private final String[] typeMap;

    Set<String> distinctValues;
    Map<String, Map<String, Integer>> values;

    public Metadata(int distinctSize) {
        typeMap = new String[]{"integers", "longs", "doubles", "dates", "strings"};
        types = new int[typeMap.length];
        distinctValues = new HashSet<>(distinctSize);
        values = new HashMap<>();
        for (String s : typeMap) {
            values.put(s, new HashMap<>());
        }
    }

    public void insertValue(String s) {
        int typeIndex = getMetadataTag(s);
        String type = typeMap[typeIndex];

        types[typeIndex] = types[typeIndex] + 1;
        total++;

        // value has been seen more than two times
        if (values.get(type).containsKey(s)) {
            values.get(type).put(s, values.get(type).get(s) + 1);
            return;
        }

        // value is seen for the second time
        if (distinctValues.remove(s)) {
            distinct--;
            values.get(type).put(s, 2);
            return;
        }

        // value is seen for the first time
        distinctValues.add(s);
        distinct++;
    }

    private int getMetadataTag(String value) {

        try {
            Integer.parseInt(value);
            return 1;
        } catch (NumberFormatException ignored) {
        }

        try {
            Long.parseLong(value);
            return 2;
        } catch (NumberFormatException ignored) {
        }

        try {
            Double.parseDouble(value);
            return 3;
        } catch (NumberFormatException ignored) {
        }

        try {
            Instant.parse(value);
            return 3;
        } catch (NumberFormatException ignored) {
        }

        return 0;
    }
}
