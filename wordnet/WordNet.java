import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {

    private Digraph net;
    private HashMap<Integer, String> synmap;
    private HashMap<String, Set<Integer>> wordToIndex; // One word may have multiples index
    private SAP sap;

    // constructor takes the name of the two input files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("constructor arguments cannot be null !");

        In synsetIO = new In(synsets);
        In hyperIO = new In(hypernyms);

        // hashmap construction for the synset file.
        this.synmap = new HashMap<>();
        this.wordToIndex = new HashMap<>();

        while (synsetIO.hasNextLine()) {
            
            // line[0] will contain the index and line[1] the complete synset.
            String[] line = synsetIO.readLine().split(",");
            int index = Integer.parseInt(line[0]);

            // Separate all words with whitespace.
            String[] words = line[1].split(" ");

            // Put the complete synset in the hasmap.
            synmap.put(index, line[1]);
            
            // put each individual words into the wordToIndex map.
            for (String word : words) {
                if (!wordToIndex.containsKey(word)) {
                    wordToIndex.put(word, new HashSet<Integer>());
                }
                wordToIndex.get(word).add(index); // Add the index to the word.
            } // end for
            
        } // end while

        // Consruction of the graph.
        net = new Digraph(synmap.size());

        // Create the edges.
        while (hyperIO.hasNextLine()) {
            String[] line = hyperIO.readLine().split(",");
            if (line.length < 2)
                continue;

            // Get the tail vertex
            int tail = Integer.parseInt(line[0]);

            // For all possible vertex head, create the edge
            for (int i = 1; i < line.length; i++) {
                int head = Integer.parseInt(line[i]);
                net.addEdge(tail, head);
            }
        }

        // Checking the cycle and the single root
        CycleRootDetection check = new CycleRootDetection(net);
        if (check.hasCycle() || !check.hasSingleRoot())
            throw new IllegalArgumentException("The graph is not a rooted DAG");

        // Compute the SAP (Shortest Ancestor Path)
        sap = new SAP(net);

    }

    // Class to detect cycle and root
    private class CycleRootDetection {
        private boolean[] visited;
        private boolean[] onStack;
        private java.util.HashSet<Integer> terminalVertices; // Detect all vertex having a outdegree of 0 (no outward
        // pointing edge);
        private boolean hasCycle;

        public CycleRootDetection(Digraph graph) {
            int vertices = graph.V();
            visited = new boolean[vertices];
            onStack = new boolean[vertices];
            terminalVertices = new HashSet<Integer>();
            hasCycle = false;

            for (int i = 0; i < vertices; i++) {
                if (!visited[i]) {
                    dfs(graph, i);
                } // end if
            } // end for
        }

        private void dfs(Digraph G, int v) {
            visited[v] = true;
            onStack[v] = true;

            // Check if a terminal vertex
            if (G.outdegree(v) == 0)
                terminalVertices.add(v);

            for (int w : G.adj(v)) {

                if (!visited[w]) {
                    dfs(G, w);

                } else if (onStack[w]) {
                    // we found a cycle
                    hasCycle = true;
                } // endif

            } // endfor

            onStack[v] = false;
        }

        public boolean hasCycle() {
            return hasCycle;
        }

        public boolean hasSingleRoot() {
            return terminalVertices.size() == 1;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordToIndex.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordToIndex.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException(nounA + "/" + nounB + ": One of the noun is not a wordnet!");
        
        // get the set of index of each synsets
        Set<Integer> indexA = new HashSet<>();
        Set<Integer> indexB = new HashSet<>();
        
        // push the index into the HashSets
        indexA.addAll(wordToIndex.get(nounA));
        indexB.addAll(wordToIndex.get(nounB));

        return sap.length(indexA, indexB);
    }

    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path.
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("One of the noun is not a wordnet!");

        // get the index of each words
        // get the set of index of each synsets
        Set<Integer> indexA = new HashSet<>();
        Set<Integer> indexB = new HashSet<>();
        
        // push the index into the HashSets
        indexA.addAll(wordToIndex.get(nounA));
        indexB.addAll(wordToIndex.get(nounB));

        int ancestorIndex = sap.ancestor(indexA, indexB);

        return synmap.get(ancestorIndex);
    }

    // unit testing
    public static void main(String[] args) {
    }
}
