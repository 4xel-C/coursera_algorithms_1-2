# Percolation Simulation

## Description

This project simulates a percolation system using a Monte Carlo method to estimate the percolation threshold. It consists of two main Java classes:

- **Percolation**: Models an n x n grid where sites can be opened and checks whether the system percolates.

- **PercolationStats**: Runs multiple trials on a percolation system to compute statistical estimates of the percolation threshold.

### Percolation problem

Given a composite systems comprised of randomly distributed insulating and metallic materials: what fraction of the materials need to be metallic so that the composite system is an electrical conductor? Given a porous landscape with water on the surface (or oil below), under what conditions will the water be able to drain through to the bottom (or the oil to gush through to the surface)? Scientists have defined an abstract process known as percolation to model such situations.

<p align="center">
  <img src="https://coursera.cs.princeton.edu/algs4/assignments/percolation/percolates-yes.png" height="250px" />
  <img src="https://coursera.cs.princeton.edu/algs4/assignments/percolation/percolates-no.png" height="250px" />
</p>

In a famous scientific problem, researchers are interested in the following question: if sites are independently set to be open with probability p (and therefore blocked with probability 1 − p), what is the probability that the system percolates? When p equals 0, the system does not percolate; when p equals 1, the system percolates. The plots below show the site vacancy probability p versus the percolation probability for 20-by-20 random grid (left) and 100-by-100 random grid (right).

<p align="center">
  <img src="https://coursera.cs.princeton.edu/algs4/assignments/percolation/percolation-threshold20.png" height="250px" />
  <img src="https://coursera.cs.princeton.edu/algs4/assignments/percolation/percolation-threshold100.png" height="250px" />
</p>
      
When n is sufficiently large, there is a threshold value p* such that when p < p* a random n-by-n grid almost never percolates, and when p > p*, a random n-by-n grid almost always percolates. No mathematical solution for determining the percolation threshold p* has yet been derived. Your task is to write a computer program to estimate p*.

### Union Find Data structure

#### Principle

The Union Find used in the algorithm serves as a support to keep track of connected sites using optimized alogrithms (path compression and *wheighed quick union* to flatten the tree).
The union find represent each **site** of the percolation problem on a **1D array** where the *index corresponds to a site* and the value, the root node of the tree keeping track of **connected components** (all the sites connected all together).

*Exemple:*
```java
index: 0  1  2  3  4  5  6  7  8  9
 id = [0, 1, 1, 8, 8, 0, 0, 1, 8, 8]
```
Sites {1, 2, 7} are all connected all together with the root node beeing 1.

#### Methods and complexity
Union-find API provides several method to connect 2 nodes together (union) and to retrieve the root node of a specific node (find).
To check if 2 nodes belong to the same *connected component*, we can just call the *find* method to both and compare their roots.

Because of the **path compression** and **wheighed union** flattening as much as possible the resulting trees, all of these operations work on **O(N + Mlg*N)** (alpha(n), inversed Ackermann function) complexity, almost reaching a constant complexity.

#### Representation of the 2D array in a 1D array
To represent a 2D array of n * n elements, I'm using a simple formula where I place all the elements on their columns, shifting by the number of row the element should reach:
`index = col + row * n`

### Monte Carlo simulation
To estimate the percolation threshold, consider the following computational experiment:
- Initialize all sites to be blocked.
- Repeat the following until the system percolates:
- Choose a site uniformly at random among all blocked sites.
- Open the site.
- The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.

For example, if sites are opened in a 20-by-20 lattice according to the snapshots below, then our estimate of the percolation threshold is 204/400 = 0.51 because the system percolates when the 204th site is opened.

<p align="center">
    <img src="https://coursera.cs.princeton.edu/algs4/assignments/percolation/percolation-204.png"  alt="percolation-204" height="250px"/>
</p>

**Percolation** is a class implemented to manage all the methods concerning the percolation grid.

## Files

`Percolation.java`

This class implements the percolation system using the Weighted Quick Union-Find algorithm.

**Key Features:**

- Initializes an n x n grid where all sites are initially blocked.
- Supports opening sites and connecting them to adjacent open sites.
- Uses virtual top and bottom nodes to efficiently check if the system percolates.
- Avoids backwash using a second Union-Find structure without a virtual bottom.

**Methods:**

- `Percolation(int n)`: Initializes an n x n grid.
- `void open(int row, int col)`: Opens a site if it is not already open.
- `boolean isOpen(int row, int col)`: Checks if a site is open.
- `boolean isFull(int row, int col)`: Checks if a site is full (connected to the top row).
- `int numberOfOpenSites()`: Returns the number of open sites.
- `boolean percolates()`: Checks if the system percolates.

**Example Usage:**
```java
Percolation perc = new Percolation(5);
perc.open(1, 3);
System.out.println("Does it percolate? " + perc.percolates());
```

`PercolationStats.java`

This class performs multiple percolation experiments to compute statistics on the percolation threshold.

**Key Features:**

- Runs T independent trials on an n x n percolation system.
- Uses the Monte Carlo method to estimate the percolation threshold.
- Computes mean, standard deviation, and 95% confidence interval of percolation thresholds.

**Methods:**

- `PercolationStats(int n, int trials)`: Runs multiple percolation experiments.
- `double mean()`: Computes the mean percolation threshold.
- `double stddev()`: Computes the standard deviation.
- `double confidenceLo()`: Computes the lower bound of the 95% confidence interval.
- `double confidenceHi()`: Computes the upper bound of the 95% confidence interval.

**Example Usage:**

```java
PercolationStats stats = new PercolationStats(20, 30);
System.out.println("Mean threshold: " + stats.mean());
System.out.println("95% confidence interval: [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
```

## Compilation and Execution

To compile the code:

`javac -cp ".;./.lift/algs4.jar" Percolation.java PercolationStats.java`

To run the PercolationStats program:

`java -cp ".;./.lift/algs4.jar" PercolationStats 20 30`

Here, 20 is the grid size (n), and 30 is the number of trials using the *algs4.jar* package supplied by Princeton.

## Dependencies

`edu.princeton.cs.algs4.StdRandom`: For generating random numbers.
`edu.princeton.cs.algs4.StdStats`: For computing statistical measures.
`edu.princeton.cs.algs4.WeightedQuickUnionUF`: For Union-Find operations.

## Notes

- The percolation threshold is the ratio of open sites when the system first percolates.
- The simulation helps estimate the percolation threshold for different grid sizes.