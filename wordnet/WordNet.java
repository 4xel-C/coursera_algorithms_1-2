import edu.princeton.cs.algs4.Digraph;

public class WordNet {
    
    Digraph net;
    
    // constructor takes the name of the two input files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException("constructor arguments cannot be null !");
        
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns(){
        
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        
    }
    
    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path.
    public String sap(String nounA, String nounB) {
        
    }
    
    // unit testing
    public static void main(String[] args) {
        
    }
}
