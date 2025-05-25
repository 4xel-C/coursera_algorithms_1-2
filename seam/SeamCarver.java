import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private Picture picture;
    private int height;
    private int width;
    private Color[][] colors; // Caching the colors of the picture.
    private double[][] energyMatrix;

    /**
     * Constructor.
     * 
     * @param picture to create the SeamCarver on.
     */
    public SeamCarver(Picture picture) {

        if (picture == null)
            throw new IllegalArgumentException("Null object passed into the constructor.");

        // deep copy the picture for immutability.
        this.picture = new Picture(picture);
        this.height = picture.height();
        this.width = picture.width();
        this.colors = new Color[height][width];
        this.energyMatrix = new double[height][width];

        // compute the color of each pixel.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colors[y][x] = picture.get(x, y); // caching the colors.
            } // end for
        } // end for

        // compute the energy matrix
        // compute the color of each pixel.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energyMatrix[y][x] = energy(x, y); 
            } // end for
        } // end for
    }

    /**
     * Return a new instance of picture for immutability.
     * 
     * @return
     */
    public Picture picture() {
        return new Picture(this.picture);
    }

    /**
     * Width of the current picture.
     * 
     * @return the width.
     */
    public int width() {
        return width;
    }

    /**
     * Return the height of the current picture.
     * 
     * @return height.
     */
    public int height() {
        return height;
    }

    /**
     * Energy of pixel at column x and row y.
     * 
     * @param x column of the pixel.
     * @param y row of the pixel.
     * @return The energy value of the pixel.
     */
    public double energy(int x, int y) {

        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Pixel coordinates beyond the size of the picture!");

        // detect if the pixel is on the left or right border of the picture.
        if (x == 0 || x == width - 1)
            return 1000;

        // detect if the pixel is on the top or bottom border of the picture.
        if (y == 0 || y == height - 1)
            return 1000;

        // Get the ARGB value of the pixel on left, right, top and bottom using the
        // grid.
        Color rgbLeft = colors[y][x - 1];
        Color rgbRight = colors[y][x + 1];
        Color rgbTop = colors[y - 1][x];
        Color rgbBottom = colors[y + 1][x];

        // Calculate central differences for each color : Red, Green and Blue.
        int rx = rgbRight.getRed() - rgbLeft.getRed();
        int gx = rgbRight.getGreen() - rgbLeft.getGreen();
        int bx = rgbRight.getBlue() - rgbLeft.getBlue();
        int dx = (rx * rx) + (gx * gx) + (bx * bx);

        int ry = rgbBottom.getRed() - rgbTop.getRed();
        int gy = rgbBottom.getGreen() - rgbTop.getGreen();
        int by = rgbBottom.getBlue() - rgbTop.getBlue();
        int dy = (ry * ry) + (gy * gy) + (by * by);

        return Math.sqrt(dx + dy);
    }

    /**
     * Sequence of indices for vertical seam.
     * 
     * @return
     */
    public int[] findVerticalSeam() {

        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // Initialize the first line
        for (int x = 0; x < width; x++) {
            distTo[0][x] = energyMatrix[0][x];
        }

        // iterate throughout each line to relax the distances.
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
                for (int dx = -1; dx <= 1; dx++) {
                    int prevX = x + dx;
                    if (prevX >= 0 && prevX < width) {
                        if (distTo[y - 1][prevX] + energyMatrix[y][x] < distTo[y][x]) {
                            distTo[y][x] = distTo[y -1][prevX] + energyMatrix[y][x];
                            edgeTo[y][x] = prevX;
                        }
                    }
                }
            }
        }

        // Find the seamPath by finding the minimum distance of the last line
        double minDist = Double.POSITIVE_INFINITY;
        int minX = 0;
        for (int x = 0; x < width; x++) {
            if (distTo[height - 1][x] < minDist) {
                minDist = distTo[height - 1][x];
                minX = x;
            }
        }

        // Retrieve the seam using the edgeTo matrix.
        int[] seam = new int[height];

        // Minimum distance of the last step.
        seam[height - 1] = minX;

        for (int y = height - 1; y > 0; y--) {
            seam[y - 1] = edgeTo[y][seam[y]];
        }
        return seam;
    }

    /**
     * Sequence of indices for horizontal seam.
     */
    public int[] findHorizontalSeam() {

        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // Initialize the first column
        for (int y = 0; y < height; y++) {
            distTo[y][0] = energyMatrix[y][0];
        }

        // iterate throughout each column to relax the distances.
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
                for (int dy = -1; dy <= 1; dy++) {
                    int prevY = y + dy;
                    if (prevY >= 0 && prevY < height) {
                        if (distTo[prevY][x - 1] + energyMatrix[y][x] < distTo[y][x]) {
                            distTo[y][x] = distTo[prevY][x - 1] + energyMatrix[y][x];
                            edgeTo[y][x] = prevY;
                        }
                    }
                }
            }
        }

        // Find the seamPath by finding the minimum distance of the last column
        double minDist = Double.POSITIVE_INFINITY;
        int minY = 0;
        for (int y = 0; y < height; y++) {
            if (distTo[y][width - 1] < minDist) {
                minDist = distTo[y][width - 1];
                minY = y;
            }
        }

        // Retrieve the seam using the edgeTo matrix.
        int[] seam = new int[width];

        // Minimum distance of the last step.
        seam[width - 1] = minY;

        for (int x = width - 1; x > 0; x--) {
            seam[x - 1] = edgeTo[seam[x]][x];
        }
        return seam;
    }

    /**
     * Remove vertical seam from the current picture and update all the matrices.
     * 
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {

        if (seam == null)
            throw new IllegalArgumentException("Invalid seam");
        if (width == 1)
            throw new IllegalArgumentException("Minimum width reached");
        if (seam.length != height)
            throw new IllegalArgumentException("Bad seam size!");

        for (int i = 0; i < seam.length; i++) {

            if (seam[i] < 0 || seam[i] >= width)
                throw new IllegalArgumentException("Seam format error!");
            if (i < seam.length - 1 && (Math.abs(seam[i] - seam[i + 1]) > 1))
                throw new IllegalArgumentException("Seam not contiguous!");
        }

        // for each line shift the colors of the pixel to the left in the colors matrix and the energy Matrix to overwrite the seam.
        for (int y = 0; y < height; y++) {
            int seamx = seam[y];
            for (int x = seamx; x < width-1; x++) {
                colors[y][x] = colors[y][x+1];
                energyMatrix[y][x] = energyMatrix[y][x+1];
            }
        }
        
        // reduce the width
        this.width--;
        
        // Update the energyMatrix for the pixel that was before the deleted seam, and the cell that was after the seam (now shifted to the seam X value).
        for (int y = 0; y < height; y++) {
            int seamx = seam[y];
            
            // update the energy value
            for (int dx = -1; dx < 1; dx++) {
                if ((seamx + dx) >= 0 && (seamx + dx < width)) {
                    energyMatrix[y][seamx + dx] = energy(seamx + dx, y);    
                }
            }
        }
        
        // update the picture
        this.picture = new Picture(width, height);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.picture.set(x,  y, colors[y][x]);
            }
        }
    }

    /**
     * Remove the horizontal seam from current picture.
     * 
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {

        if (seam == null)
            throw new IllegalArgumentException("Invalid seam");
        if (height == 1)
            throw new IllegalArgumentException("Minimum height reached");
        if (seam.length != width)
            throw new IllegalArgumentException("Bad seam size!");

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height)
                throw new IllegalArgumentException("Seam format error!");
            if (i < seam.length - 1 && (Math.abs(seam[i] - seam[i + 1]) > 1))
                throw new IllegalArgumentException("Seam not contiguous!");
        }

        // for each column shift the colors of the pixel and its energy toward the top in the colors matrix to overwrite the seam.
        for (int x = 0; x < width; x++) {
            int seamy = seam[x];
            for (int y = seamy; y < height-1; y++) {
                colors[y][x] = colors[y+1][x];
                energyMatrix[y][x] = energyMatrix[y+1][x];
            }
        }
        
        // reduce the height
        this.height--;
        
        // Update the energyMatrix for the pixel impacted by the deletion of the seam. (-1 and seamy value)
        for (int x = 0; x < width; x++) {
            int seamy = seam[x];
            for (int dy = -1; dy < 1; dy++) {
                if ((seamy + dy) >= 0 && (seamy + dy < height)) {
                    energyMatrix[seamy + dy][x] = energy(x, seamy + dy);    
                }
            }
        }
        
        // update the picture
        this.picture = new Picture(width, height);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.picture.set(x,  y, colors[y][x]);
            }
        }
    }

    public static void main(String[] args) {
        String file = "png/10x10.png";
        Picture picture = new Picture(file);

        SeamCarver carver = new SeamCarver(picture);

        // -------------------------------------------Print energy matrix
        for (int y = 0; y < carver.height; y++) {
            System.out.println();
            for (int i = 0; i < carver.width; i++) {

                System.out.print("----------");
            }
            System.out.println();
            for (int x = 0; x < carver.width; x++) {
                String line = " | " + String.format("%.2f", carver.energy(x, y));
                System.out.print(String.format("%-10s", line));
            }
        }
        System.out.println();
        // ------------------------------------------
        System.out.println();
        
        
        
        int[] verticalSeam = carver.findVerticalSeam();

        for (int i = 0; i < verticalSeam.length; i++) {
            System.out.println("y = " + i + "->" + verticalSeam[i]);
        }
        
        carver.removeVerticalSeam(verticalSeam);
        
        // -------------------------------------------Print energy matrix
        for (int y = 0; y < carver.height; y++) {
            System.out.println();
            for (int i = 0; i < carver.width; i++) {

                System.out.print("----------");
            }
            System.out.println();
            for (int x = 0; x < carver.width; x++) {
                String line = " | " + String.format("%.2f", carver.energy(x, y));
                System.out.print(String.format("%-10s", line));
            }
        }
        System.out.println();
        // ------------------------------------------
        System.out.println();

    }
}
