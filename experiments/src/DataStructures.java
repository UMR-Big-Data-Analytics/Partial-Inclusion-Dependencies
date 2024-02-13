import java.util.*;
import java.util.stream.Collectors;

public class DataStructures {

    public static void main(String[] args) {
        List<Integer> values = new ArrayList<>();
        List<Integer> shuffled = new ArrayList<>();
        for (int i = 0; i < 10_000_000; i++) {
            values.add(i);
            shuffled.add(i);
        }

        for (int i = 0; i < 5; i++) {
            Collections.shuffle(shuffled);

            long startTime = System.currentTimeMillis();
            TreeSet<Integer> treeSet = new TreeSet<>();
            for (Integer v : values) {
                treeSet.add(v);
            }
            System.out.println("TreeSet ordered insertion took: " + (System.currentTimeMillis() - startTime) + "ms");

            startTime = System.currentTimeMillis();
            treeSet = new TreeSet<>();
            for (Integer v : shuffled) {
                treeSet.add(v);
            }
            System.out.println("TreeSet shuffled insertion took: " + (System.currentTimeMillis() - startTime) + "ms");

            startTime = System.currentTimeMillis();
            HashSet<Integer> hashSet = new HashSet<>();
            for (Integer v : values) {
                hashSet.add(v);
            }
            hashSet.stream().sorted().collect(Collectors.toList());
            System.out.println("HashSet ordered insertion took: " + (System.currentTimeMillis() - startTime) + "ms");

            startTime = System.currentTimeMillis();
            hashSet = new HashSet<>();
            for (Integer v : shuffled) {
                hashSet.add(v);
            }
            hashSet.stream().sorted().collect(Collectors.toList());
            System.out.println("HashSet shuffled insertion took: " + (System.currentTimeMillis() - startTime) + "ms");
        }
    }
}
