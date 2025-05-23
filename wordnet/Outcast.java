import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet net;

    public Outcast(WordNet wordnet) {
        net = wordnet;
    }

    public String outcast(String[] nouns) {

        String outcast = null;
        int maxDistance = 0;

        for (String noun : nouns) {
            int distance = 0;

            for (String otherNoun : nouns) {
                distance += net.distance(noun, otherNoun);
            }

            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = noun;
            }
        }

        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
