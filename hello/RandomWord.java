import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {

    public static void main(String[] args) {
        String champion = null;
        int counter = 0;

        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            counter++;

            if (StdRandom.bernoulli(1.0 / counter)) {
                champion = word;
            }
        }

        if (champion != null) {
            System.out.println(champion);
        }
    }

}
