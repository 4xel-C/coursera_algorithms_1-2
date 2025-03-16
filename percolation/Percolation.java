import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF UF; // Union Find Data Structure to keep track of components
    private WeightedQuickUnionUF UFNoBottom; // Union Find with no virtual bottom to avoid backwash while checking full
    private boolean[][] grid; // Boolean grid representation of the percolation grid. false = closed site.
    private int gridSize; // Remember the grid size (used to find the index in the UF)
    private int openSiteNumbers; // Keep track of the number of open sites
    private int virtualTopIndex; // Virtual top index (last but one element of UF)
    private int virtualBottomIndex; // Virtual bottom index (last element of UF)

    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("n should be a positive number");
        }

        // initialize the grid to all false
        this.grid = new boolean[n][n];
        this.gridSize = n;

        // Initialize the Union Find to keep track of all elements of the grid + virtual
        // top/bottom
        UF = new WeightedQuickUnionUF(n * n + 2);
        UFNoBottom = new WeightedQuickUnionUF(n * n + 1);
        virtualTopIndex = n * n;
        virtualBottomIndex = (n * n) + 1;

        openSiteNumbers = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        if (row <= 0 || col <= 0 || row > gridSize || col > gridSize) {
            throw new IllegalArgumentException("Invalid row and col range");
        }

        row = row - 1; // row given between 1 - n
        col = col - 1; // col given between 1 - n
        if (!grid[row][col]) {

            // Mark the site as open
            grid[row][col] = true;
            this.openSiteNumbers++;

            // Update the Union Find to join with the adjacent open site
            int ufIndex = col + (row * gridSize);

            // If row 0, connect with the virtual top
            if (row == 0) {
                UF.union(virtualTopIndex, ufIndex);
                UFNoBottom.union(virtualTopIndex, ufIndex);
            } 
            
            // If site belong to the last row, connect with the virtualBottom
            if (row == gridSize - 1) {
                UF.union(virtualBottomIndex, ufIndex);

            }

            // Check top and bottom cells and connect if open
            for (int i = row - 1; i <= row + 1; i += 2) {
                if (i >= 0 && i < gridSize && isOpen(i + 1, col + 1)) {
                    int adjCellIndex = col + (i * gridSize);
                    UF.union(ufIndex, adjCellIndex);
                    UFNoBottom.union(ufIndex, adjCellIndex);
                }
            }

            // Check left and right cells and connect if open
            for (int j = col - 1; j <= col + 1; j += 2) {
                if (j >= 0 && j < gridSize && isOpen(row + 1, j + 1)) {
                    int adjCellIndex = j + (row * gridSize);
                    UF.union(ufIndex, adjCellIndex);
                    UFNoBottom.union(ufIndex, adjCellIndex);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridSize || col > gridSize) {
            throw new IllegalArgumentException("Invalid row and col range");
        }
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {

        if (row <= 0 || col <= 0 || row > gridSize || col > gridSize) {
            throw new IllegalArgumentException("Invalid row and col range");
        }

        row = row - 1;
        col = col - 1;
        int ufIndex = col + (row * gridSize);
        return UFNoBottom.find(ufIndex) == UFNoBottom.find(virtualTopIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSiteNumbers;
    }

    // does the system percolate?
    public boolean percolates() {
        return UF.find(virtualTopIndex) == UF.find(virtualBottomIndex);
    }

    // test client (optional)
    public static void main(String[] args) {

        // Create a 5 * 5 grid
        int n = 5;
        Percolation perc = new Percolation(n);

        System.out.println("Grid size " + n + "x" + n);
        System.out.println("Does it percolates? " + perc.percolates());

        // Open some sites
        System.out.println("\nOpening some site for percolation:");

        int[][] sitesToOpen = { { 1, 3 }, { 2, 3 }, { 3, 3 }, { 4, 3 }, { 5, 3 } // Open a vertical path on the the 3rd
                                                                                 // column
        };

        for (int[] site : sitesToOpen) {
            int row = site[0];
            int col = site[1];
            perc.open(row, col);
            System.out.println("Open site (" + row + ", " + col + ")");
            System.out.println("  - Open? " + perc.isOpen(row, col));
            System.out.println("  - Full? " + perc.isFull(row, col));
            System.out.println("  - Percolation? " + perc.percolates());
        }

        // Print statistics
        System.out.println("\nFinal numbers");
        System.out.println("Open sites: " + perc.numberOfOpenSites());
        System.out.println("% of open sites " + (100.0 * perc.numberOfOpenSites() / (n * n)) + "%");

        // Check Backwash
        if (n >= 5) {
            // Open a site on the last row not connected to the percolation path and check
            // it is not full
            perc.open(n, 1);
            System.out.println("\nOpening site (" + n + ", 1)");
            System.out.println("  - Open? " + perc.isOpen(n, 1));
            System.out.println("  - Full? " + perc.isFull(n, 1) + " (Should be false - back wash test)");
        }
    }

}
