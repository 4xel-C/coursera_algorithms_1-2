# SeamCarver

## Overview

SeamCarver is a Java class designed to perform seam carving on images. Seam carving is a content-aware image resizing technique that removes seams of low energy from an image to reduce its size without significant loss of important content.

This implementation uses the Princeton `edu.princeton.cs.algs4.Picture` class for image representation and manipulation.

## Features

- Compute energy of pixels in a picture.
- Find vertical and horizontal seams with minimal energy.
- Remove vertical and horizontal seams.
- Maintains immutability of the original picture by working on copies.
- Efficient dynamic programming approach to compute seams.

## Installation

1. **Prerequisites:**

   - Java Development Kit (JDK) 8 or above installed.
   - [algs4.jar](https://algs4.cs.princeton.edu/code/algs4.jar) library from Princeton’s Algorithms course, which contains the `Picture` class.

2. **Setup Steps:**

   - Download and place `algs4.jar` in your project directory.
   - Compile the `SeamCarver.java` file along with `algs4.jar`:

     ```bash
     javac -cp .:algs4.jar SeamCarver.java
     ```

     *(On Windows, replace `:` with `;` in the classpath.)*

3. **Running the program:**

   You can use the `ShowSeams.java` class to test the program and display the calculation on the image of your choice using the following command:
   ```bash
   java -cp ".;lib/algs4.jar" ShowSeams png/chameleon.png
   ```

## Key Methods

- `SeamCarver(Picture picture)`: Constructor that initializes the object with a copy of the provided picture and computes pixel energies.
- `double energy(int x, int y)`: Returns the energy of the pixel at column `x` and row `y`.
- `int[] findVerticalSeam()`: Finds and returns the vertical seam of lowest energy as an array of column indices.
- `int[] findHorizontalSeam()`: Finds and returns the horizontal seam of lowest energy as an array of row indices.
- `void removeVerticalSeam(int[] seam)`: Removes the specified vertical seam from the current picture.
- `void removeHorizontalSeam(int[] seam)`: Removes the specified horizontal seam from the current picture.

## Implementation Details

- Energy is calculated based on the gradient magnitude of RGB color channels, using the dual-gradient energy function.
- Seam paths are found by dynamic programming with relaxation of energy costs either vertically or horizontally.
- Input validation is done for seam correctness (contiguousness, length, bounds).
- Picture and energy matrices are updated after each seam removal.

## Usage Example

```java
Picture inputPic = new Picture("input.png");
SeamCarver sc = new SeamCarver(inputPic);

int[] verticalSeam = sc.findVerticalSeam();
sc.removeVerticalSeam(verticalSeam);

int[] horizontalSeam = sc.findHorizontalSeam();
sc.removeHorizontalSeam(horizontalSeam);

Picture outputPic = sc.picture();
outputPic.save("output.png");
```

## Notes

- The class assumes the input picture is not null and has dimensions greater than 1x1.
- Seam removal reduces the width or height by 1, respectively.
- Throws `IllegalArgumentException` if invalid seams are passed or minimum dimensions are reached.


