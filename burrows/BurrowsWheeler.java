import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BurrowsWheeler {
    
    /**
     * Apply burrows-Wheeler transform,
     * reading from standard input and writing to standard output.
     */
    public static void transform() {
        
        String s; // The string to transform.
        char[] t; // array t containing the last letter of the sorted circular suffices. (transformed message).
        int first = 0; // index of the first circular suffix.
        
        // read the string
        if (!BinaryStdIn.isEmpty()) {
            s = BinaryStdIn.readString();
        } else
            return;
        
        // Close the reader.
        BinaryStdIn.close();
        
        CircularSuffixArray suffix = new CircularSuffixArray(s);
        t = new char[suffix.length()];
        
        // Iterate through the suffix index to find the last letter of the sorted suffix:
        // s.charAt((index[i] - 1) % suffix.length()); because index[i] give the number of shifts to have the first letter, having -1 will give the the last.
        for (int i = 0; i < suffix.length(); i++) {
            t[i] = s.charAt((suffix.index(i) - 1 + suffix.length()) % suffix.length()); // Adding suffix.length() to have a positive modulo.
            if (suffix.index(i) == 0) first = i;
        }
        
        // write the first integer (index where to find the original string).
        BinaryStdOut.write(first);
        
        // Write the characters after the burrows-Wheeler transform.
        for (int i = 0; i < t.length; i++) {
            BinaryStdOut.write(t[i]);
        }
        
        // Close the writer.
        BinaryStdOut.close();
    }
    
    /**
     * Apply Burrows-Wheeler inverse transform,
     * reading from standard input and writing to standard output.
     */
    public static void inverseTransform() {
        
        // Index of the first suffix.
        int[] next;
        char[] t;
        char[] sufficesFirstLetters; // Compute the first letters of the suffices in sorted order.
        String s = "";
        
        // Create a map of all characters in the t array and the index of their first occurence.
        Map<Character, Queue<Integer>> map = new HashMap<>();
        
        int orderedIndex = 0;
        int n = 0; // Length of the string.
        
        // Read the first integer;
        if (!BinaryStdIn.isEmpty()) orderedIndex = BinaryStdIn.readInt();
        
        // Get the full string.
        if (!BinaryStdIn.isEmpty()) {
            s = BinaryStdIn.readString();
            n = s.length();
        }
        
        BinaryStdIn.close();
        
        // Initializing the variable with the correct length:
        next = new int[n];
        t = new char[n];
        sufficesFirstLetters = new char[n];
        
        // Computing the t array.
        for (int i = 0; i < n; i++) {
            t[i] = s.charAt(i);
        }
        
        // Compute the first letters.
        sufficesFirstLetters = Arrays.copyOf(t, n);
        
        Arrays.sort(sufficesFirstLetters);
        
        // Computing the next array.
        for (int i = 0; i < n; i++) {
            map.computeIfAbsent(t[i], k -> new Queue<Integer>()).enqueue(i);
        }
        for (int i = 0; i < n; i++) {
            next[i] = map.get(sufficesFirstLetters[i]).dequeue();
        }
        
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(sufficesFirstLetters[orderedIndex]);
            orderedIndex = next[orderedIndex];
        }
        
        BinaryStdOut.close();
    }
    
    public static void main(String[] args) {
    }

}
