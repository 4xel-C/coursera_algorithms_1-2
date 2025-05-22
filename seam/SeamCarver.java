import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

public class SeamCarver {
    
    private Picture picture;
    private int height;
    private int width;
    private double[][] energyGrid;
    private double[][] verticalDist;   // Compute distance for vertical seam
    private double[][] horizontalDist;
    
    /**
     * Constructor.
     * @param picture to create the SeamCarver on.
     */
    public SeamCarver(Picture picture) {
        
        if (picture == null) throw new IllegalArgumentException("Null object passed into the constructor.");
        
        // deep copy the picture for immutability.
        this.picture = new Picture(picture);
        this.height = picture.height();
        this.width = picture.width();
        this.energyGrid = new double[height][width];
        
        // initialize distance matrices
        verticalDist = new double[height][width];
        horizontalDist = new double[height][width];
        
        // compute the energy of each pixel.
        for (int x = 0; x < width; x++) {
            verticalDist[0][x] = 0; // declare the distance of the first line to 0.
            for (int y = 0; y < height; y++) {
                horizontalDist[y][0] = 0; // declare the distance of the first column to 0.
                energyGrid[y][x] = energy(x, y);
            } // end for
        } // end for
    }
    
    /**
     * Current picture.
     * @return
     */
    public Picture picture() {
        return picture;
    }
    
    /**
     * Width of the current picture.
     * @return the width.
     */
    public int width() {
        return width;
    }
    
    
    /**
     * Return the height of the current picture.
     * @return height.
     */
    public int height() {
        return height;
    }
    
    
    /**
     * Energy of pixel at column x and row y.
     * @param x column of the pixel.
     * @param y row of the pixel.
     * @return The energy value of the pixel.
     */
    public double energy(int x, int y) {
        
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException("Pixel coordinates beyond the size of the picture!");
        
        // detect if the pixel is on the left or right border of the picture.
        if (x == 0 || x == width - 1) return 1000;
        
        // detect if the pixel is on the top or bottom border of the picture.
        if (y == 0 || y ==  height -1) return 1000;
        
        // Get the ARGB value of the pixel on left, right, top and bottom.
        int ARGBLeft = picture.getARGB(x - 1, y);
        int ARGBRight = picture.getARGB(x + 1, y);
        int ARGBTop = picture.getARGB(x, y - 1);
        int ARGBBottom = picture.getARGB(x, y + 1);
        
        // Calculate central differences for each color using binary manipulation to extract Red, Green and Blue from the ARGB value.
        int Rx = ((ARGBRight >> 16) & 0xFF) - ((ARGBLeft >> 16) & 0xFF);
        int Gx = ((ARGBRight >> 8) & 0xFF) - ((ARGBLeft >> 8) & 0xFF);
        int Bx = (ARGBRight & 0xFF) - (ARGBLeft & 0xFF);
        int dx = (Rx * Rx) + (Gx * Gx) + (Bx * Bx);
        
        int Ry = ((ARGBBottom >> 16) & 0xFF) - ((ARGBTop >> 16) & 0xFF);
        int Gy = ((ARGBBottom >> 8) & 0xFF) - ((ARGBTop >> 8) & 0xFF);
        int By = (ARGBBottom & 0xFF) - (ARGBTop & 0xFF);
        int dy = (Ry * Ry) + (Gy * Gy) + (By * By);
        
        
        return Math.sqrt(dx + dy);
    }
    
    
    /**
     * Sequence of indices for vertical seam.
     * @return
     */
    public int[] findVerticalSeam() {
        
        // declare the seamPath
        int[] seamPath = new int[height];
        
        // Start by relaxing all pixels.
        relaxVertical();
        
        // detect the lowest energy cost on the last line and get his x value.
        double minCost = verticalDist[height - 1][0];
        int minX = 0;
        
        for (int x = 1; x < width; x++) {
            if (verticalDist[height - 1][x] < minCost) {
                minCost = verticalDist[height - 1][x];
                minX = x;
            }
        }
        
        // update the seamPath.
        seamPath[height - 1] = minX;
        
        // traceback the path line by line until we reach the top of the picture;
        for (int y = height - 2; y >= 0; y--) {
            
            // Check central cost
            double bestCost = verticalDist[y][minX];  
            int bestX = minX;
            
            // Check left cost
            if (minX > 0 && verticalDist[y][minX - 1] < bestCost) {
                bestX = minX - 1;
                bestCost = verticalDist[y][minX - 1];
            }
            
            // Check right cost
            if (minX < width - 1 && verticalDist[y][minX + 1] < bestCost) {
                bestX = minX + 1;
                bestCost = verticalDist[y][minX + 1];
            }
            // Reupdate the minX variable.
            seamPath[y] = bestX;
            minX = bestX;
        }
        
        return seamPath;
    }
    
    
    /**
     * Sequence of indices for horizontal seam.
     */
    public int[] findHorizontalSeam() {
        return null;
    }
    
    
    
    /**
     * Remove vertical seam from the current picture.
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        
    }
    
    
    
    /**
     * Remove the horizontal seam from current picture.
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        
    }
 
    
    /**
     * Method to relax the energy vertically (update vertical dist).
     */
    private void relaxVertical() {
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                
                // get the minimum cost from previous line
                double minPrev = verticalDist[y - 1][x];
                if (x > 0) minPrev = Math.min(minPrev, verticalDist[y - 1][x - 1]);
                if (x < width - 1) minPrev = Math.min(minPrev, verticalDist[y - 1][x + 1]);
                verticalDist[y][x] = energyGrid[y][x] + minPrev;
            } 
        }
    }
    
   
    
    /**
     * Unit testing.
     * @param args
     */
    public static void main(String[] args) {
        
    }

}
