import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;

public class Board {

    private int[][] tiles;
    private final int n; // dimension of the tiles

    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles[0].length];

        // dimension of the board
        this.n = tiles.length;

        // deep copy the board
        this.tiles = copyTiles(tiles);
    }
    
    /**
     * Helper method to copy a board's tiles
     * 
     * @param tiles
     * @return the copy of the board
     */
    private static int[][] copyTiles(int[][] tiles) {

        int n = tiles.length;

        int[][] copy = new int[n][n];

        // deep copy the board
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    public String toString() {

        // first line containing the board size
        StringBuilder string = new StringBuilder();
                
        string.append(String.valueOf(n));

        // print each line of the board
        for (int i = 0; i < n; i++) {

            // add the linebreak
            string.append("\n");

            for (int j = 0; j < n; j++) {
                string.append(String.format("%2d ", tiles[i][j]));
            }
        }
        return string.toString();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {

        int outOfPlace = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (tiles[i][j] != 0 && tiles[i][j] != ((i * n) + (j + 1)))
                    outOfPlace++;
            }
        }
        return outOfPlace;
    }

    public int manhattan() {

        int manhattan = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // if tile is misplaced, increment manhattan distances
                if (tiles[i][j] != 0 && tiles[i][j] != ((i * n) + (j + 1))) {

                    int num = tiles[i][j];

                    // correct row, col position of the num
                    int rownum = (num - 1) / n;
                    int colnum = (num - 1) % n;

                    manhattan += Math.abs(rownum - i) + Math.abs(colnum - j);
                }
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        if (this.hamming() == 0)
            return true;
        return false;
    }

    public boolean equals(Object y) {
        
        if (y == null) return false;
        
        if (this == y)
            return true;
        if (!(this.getClass() == y.getClass()))
            return false; // pattern matching with that beeing a Board instance accessing the methods &
                          // attributes.

        Board that = (Board) y;

        if (this.n != that.n)
            return false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {

        // ArrayList to store the neighbors
        ArrayList<Board> neighbors = new ArrayList<>();

        // Copy the original tiles board
        int[][] neighbor = new int[n][n];

        // Find the position of the zero
        int row0 = -1;
        int col0 = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                }
            }
        }

        if (col0 - 1 >= 0) {
            neighbor = Board.copyTiles(this.tiles);
            neighbor[row0][col0 - 1] = 0;
            neighbor[row0][col0] = tiles[row0][col0 - 1];
            neighbors.add(new Board(neighbor));
        }

        if (col0 + 1 < n) {
            neighbor = Board.copyTiles(this.tiles);
            neighbor[row0][col0 + 1] = 0;
            neighbor[row0][col0] = tiles[row0][col0 + 1];
            neighbors.add(new Board(neighbor));
        }

        if (row0 - 1 >= 0) {
            neighbor = Board.copyTiles(this.tiles);
            neighbor[row0 - 1][col0] = 0;
            neighbor[row0][col0] = tiles[row0 - 1][col0];
            neighbors.add(new Board(neighbor));
        }

        if (row0 + 1 < n) {
            neighbor = Board.copyTiles(this.tiles);
            neighbor[row0 + 1][col0] = 0;
            neighbor[row0][col0] = tiles[row0 + 1][col0];
            neighbors.add(new Board(neighbor));
        }

        return neighbors;
    }

    public Board twin() {
        
        int[][] copy = Board.copyTiles(tiles);
        
       // Look for 2 adjacents non zero elements 
       
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n-1; j++) {
                if (copy[i][j] != 0 && copy[i][j+1] != 0) {
                    copy[i][j] = copy[i][j + 1];
                    copy[i][j + 1] = tiles[i][j];
                    return new Board(copy);
                }
            }
        }
        return new Board(copy);
    }

    /**
     * Unit testing
     * 
     * @param args
     */
    public static void main(String[] args) {

        int[][] tiles = { { 3, 4, 2 }, { 1, 5, 6 }, { 8, 7, 0 } };

        Board board = new Board(tiles);

        Board twin = board.twin();

        // testig string representation
        System.out.println(board);

        // testing hamming
        System.out.println();
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println();
        System.out.println();
        System.out.println(twin);
    }
}
