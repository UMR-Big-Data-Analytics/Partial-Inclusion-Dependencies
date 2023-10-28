package Utils;

public class pIND {
    private final int[] leftTableIndex;
    private final int[] leftColumnIndex;
    private final int[] rightTableIndex;
    private final int[] rightColumnIndex;
    private final int size;
    private final float score;

    public pIND(int[] leftTableIndex, int[] leftColumnIndex, int[] rightTableIndex, int[] rightColumnIndex, float score) {
        this.leftTableIndex = leftTableIndex;
        this.leftColumnIndex = leftColumnIndex;
        this.rightTableIndex = rightTableIndex;
        this.rightColumnIndex = rightColumnIndex;
        this.size = leftTableIndex.length;
        this.score = score;
    }

    /**
     * Method to convert pIND to a printable and readable format.
     * Example: A pIND was found where 1.1 and 1.2 form a subset of 2.1 and 2.2 with a score of 0.9. The method would
     * create the String: "1.1,1.2:2.1,2.2$0.9".
     * @return A printable String containing all information
     */
    public String print() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(leftTableIndex[i]).append('.').append(leftColumnIndex[i]);
            if (i != size-1) res.append(',');
        }
        res.append(':');
        for (int i = 0; i < size; i++) {
            res.append(rightTableIndex[i]).append('.').append(rightColumnIndex[i]);
            if (i != size-1) res.append(',');
        }
        res.append('$').append(score);
        return res.toString();
    }
}

