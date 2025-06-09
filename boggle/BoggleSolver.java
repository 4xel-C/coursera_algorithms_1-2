import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

public class BoggleSolver {

    private final TrieSET dictionarySet;

    public BoggleSolver(String[] dictionary) {
        this.dictionarySet = new TrieSET();

        // add all the words into a trie for search.
        for (String word : dictionary) {
            this.dictionarySet.add(word);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {

        // Initialize the set that will contains the result.
        SET<String> validWords = new SET<>();

        // Test all letters of the board and progress through neighbors to find all
        // possible words.
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                collect(board, i, j, validWords, "", new boolean[board.rows()][board.cols()]);
            }
        }

        return validWords;
    }

    /**
     * Helper method to recursively find words in the boggleboard starting from cell
     * row, col.
     * 
     * @param board      Board game to analyze.
     * @param row        The i coordinate.
     * @param col        The j coordinate.
     * @param validWords A bag to add all valid words in the boggle.
     * @param prefix     The current word construction.
     */
    private void collect(BoggleBoard board, int row, int col, SET<String> validWords, String prefix,
            boolean[][] visited) {

        String newPrefix;

        // get the letter of the current cell.
        char newChar = board.getLetter(row, col);
        if (newChar == 'Q') {
            newPrefix = prefix + "QU";
        } else {
            newPrefix = prefix + newChar;
        }

        // base case: if the current 'newPrefix' is not the prefix of any words in the
        // dictionary, return null.
        if (!dictionarySet.keysWithPrefix(newPrefix).iterator().hasNext()) {
            return;
        }

        // Check if the dictionary (trie) contains the new prefix and the word is > 3.
        if (newPrefix.length() > 2 && dictionarySet.contains(newPrefix))
            validWords.add(newPrefix);

        // mark the cell as visited.
        visited[row][col] = true;

        // If we can still find a word, explore the neighbors.
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {

                // ignore the cell if out of range of the board or already visited.
                if (i < 0 || j < 0 || i >= board.rows() || j >= board.cols() || visited[i][j]) {
                    continue;
                }
                collect(board, i, j, validWords, newPrefix, visited);
            }
        }

        // backtrack after we explore all the possible path to not block other path
        // eovlution.
        visited[row][col] = false;
    }

    public int scoreOf(String word) {
        int len = word.length();

        if (len < 5)
            return 1;
        if (len == 5)
            return 2;
        if (len == 6)
            return 3;
        if (len == 7)
            return 5;
        else
            return 11;
    }

    /**
     * Unit testing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
