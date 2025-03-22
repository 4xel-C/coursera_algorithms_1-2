import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: 'java Permutation number'");
            return;
        }

        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Usage: 'java Permutation number'");
        }

        int k = Integer.parseInt(args[0]);

        // Initialize a randomQueue to generate the permutation
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            queue.enqueue(word);
        }

        for (int i = 0; i < k; i++) {
            System.out.println(queue.dequeue());
        }

    }
}
