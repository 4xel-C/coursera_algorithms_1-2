import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int trials; // Store the number of trials wanted
    private double[] results; // Will store the fractions of opened site over all sites.

    // Perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("The gridsize or the trials number should be positive!");
        }

        this.trials = trials;
        this.results = new double[trials];

        // Process the tests
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {

                // Generate a random Row and Col to open
                int ranRow = StdRandom.uniformInt(1, n + 1);
                int ranCol = StdRandom.uniformInt(1, n + 1);

                // If a site is already open continue (choose another coordinates)
                if (perc.isOpen(ranRow, ranCol)) {
                    continue;
                }

                perc.open(ranRow, ranCol);

            }
            results[i] = (double) perc.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(trials));
    }

    // Test client
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: 'java PercolationStats size trials");
            return;
        }

        int size;
        int trials;

        try {
            size = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);

        } catch (NumberFormatException e) {
            System.out.println("Wrong number format");
            return;
        }

        PercolationStats stats = new PercolationStats(size, trials);

        System.out.println(String.format("%-30s", "mean") + " = " + stats.mean());
        System.out.println(String.format("%-30s", "stddev") + " = " + stats.stddev());
        System.out.println(String.format("%-30s", "95% confidence interval") + " = [" + stats.confidenceLo() + ", "
                + stats.confidenceHi() + "]");
    }

}
