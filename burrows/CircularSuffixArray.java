import java.util.Arrays;

public class CircularSuffixArray {

    private final int n;
    private final Integer[] index;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Input string is null.");
        this.n = s.length();
        this.index = new Integer[n];

        // Initialize suffix indices
        for (int i = 0; i < n; i++) {
            index[i] = i;
        }

        // Sort using custom comparator for circular suffixes; lambda function.
        Arrays.sort(index, (a, b) -> {
            for (int i = 0; i < n; i++) {
                char c1 = s.charAt((a + i) % n);
                char c2 = s.charAt((b + i) % n);
                if (c1 != c2) return Character.compare(c1, c2);
            }
            return 0;
        });
    }
    
    public int length() {
        return this.n;
    }
    
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException("Index out of range!");
        return index[i];
    }
    
    public static void main(String[] args) {
        CircularSuffixArray test = new CircularSuffixArray("Abracadabra!");
        System.out.println(test.length());
        System.out.println(test.index(11));
    }

    
}
