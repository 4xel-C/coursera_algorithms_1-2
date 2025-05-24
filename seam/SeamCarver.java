import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

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
            for (int y = 0; y < height; y++) {
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
        Color RGBLeft = picture.get(x - 1, y);
        Color RGBRight = picture.get(x + 1, y);
        Color RGBTop = picture.get(x, y - 1);
        Color RGBBottom = picture.get(x, y + 1);
        
        // Calculate central differences for each color : Red, Green and Blue.
        int Rx = RGBRight.getRed() - RGBLeft.getRed();
        int Gx = RGBRight.getGreen() - RGBLeft.getGreen();
        int Bx = RGBRight.getBlue() - RGBLeft.getBlue();
        int dx = (Rx * Rx) + (Gx * Gx) + (Bx * Bx);
        
        int Ry = RGBBottom.getRed() - RGBTop.getRed();
        int Gy = RGBBottom.getGreen() - RGBTop.getGreen();
        int By = RGBBottom.getBlue() - RGBTop.getBlue();
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
        
        // declare the seamPath
        int[] seamPath = new int[width];
        
        // Start by relaxing all pixels.
        relaxHorizontal();
        
        
        // detect the lowest energy cost on the previous column and get his y value.
        double minCost = horizontalDist[0][width - 1];

        int minY = 0;
        
        // Scan the last column to search the lowest energy pixel distance.
        for (int y = 1; y < height; y++) {
            if (horizontalDist[y][width - 1] < minCost) {
                minCost = horizontalDist[y][width - 1];
                minY = y;
            }
        }
        
        // update the seamPath.
        seamPath[width - 1] = minY;
        
        // traceback the path column by column until we reach the left of the picture;
        for (int x = width - 2; x >= 0; x--) {
            
            // Check central cost
            double bestCost = horizontalDist[minY][x];  
            int bestY = minY;
            
            // Check left cost
            if (minY > 0 && horizontalDist[minY - 1][x] < bestCost) {
                bestY = minY - 1;
                bestCost = horizontalDist[minY - 1][x];
            }
            
            // Check right cost
            if (minY < height - 1 && horizontalDist[minY + 1][x] < bestCost) {
                bestY = minY + 1;
                bestCost = horizontalDist[minY + 1][x];
            }
            // Reupdate the minX variable.
            seamPath[x] = bestY;
            minY = bestY;
        }
        
        return seamPath;
    }
    
    
    
    /**
     * Remove vertical seam from the current picture and update all the matrices.
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        
        if (seam == null) throw new IllegalArgumentException("Invalid seam");
        if (width == 1) throw new IllegalArgumentException("Minimum width reached");
        if (seam.length != height) throw new IllegalArgumentException("Bad seam size!");
        
        for (int i = 0; i < seam.length; i++) {
            
            if (seam[i] < 0 || seam[i] >= width) throw new IllegalArgumentException("Seam format error!");
            if (i < seam.length - 1 && (Math.abs(seam[i] - seam[i+1]) > 1)) throw new IllegalArgumentException("Seam not contiguous!");
        }
        
        // Update the width.
        this.width -= 1; 
        
        // Create a new picture.
        Picture resizedPicture = new Picture(width, height);
        
        // Deletion increment.
        int offset = 0;
        
        // Iterate through each pixel of the picture.
        for (int y = 0; y < height; y++) {
            offset = 0;
            
            for (int x = 0; x < width; x++) {
                
                // increment the offset counter to ignore the seam for the picture's pixel and energy report.
                if (x == seam[y]) offset++;
                
                Color color = picture.get(x + offset, y);
                resizedPicture.set(x, y, color);
            }
        }
  
        // Rebuild energy and distances matrices.
        double[][] energyGrid = new double[height][width];
        this.verticalDist = new double[height][width];
        this.horizontalDist = new double[height][width];
        this.picture = resizedPicture;
        
        // Recompute energy.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energyGrid[y][x] = energy(x, y);
            }
        }
    }
    
    
    
    /**
     * Remove the horizontal seam from current picture.
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        
        if (seam == null) throw new IllegalArgumentException("Invalid seam");
        if (height == 1) throw new IllegalArgumentException("Minimum height reached");
        if (seam.length != width) throw new IllegalArgumentException("Bad seam size!");
        
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height) throw new IllegalArgumentException("Seam format error!");
            if (i < seam.length - 1 && (Math.abs(seam[i] - seam[i+1]) > 1)) throw new IllegalArgumentException("Seam not contiguous!");
        }
        
        // Update the height.
        this.height-= 1; 
        
        // Create a new picture.
        Picture resizedPicture = new Picture(width, height);
        
        // Deletion increment.
        int offset = 0;
        
        // Iterate through each pixel of the picture.
        for (int x = 0; x < width; x++) {
            offset = 0;
            
            for (int y = 0; y < height; y++) {
                
                // increment the offset counter to ignore the seam for the picture's pixel and energy report.
                if (y == seam[x]) offset++;
                
                Color color = picture.get(x, y + offset);
                resizedPicture.set(x, y, color);
            }
        }
  
        // Rebuild energy and distances matrices.
        double[][] energyGrid = new double[height][width];
        this.verticalDist = new double[height][width];
        this.horizontalDist = new double[height][width];
        this.picture = resizedPicture;
        
        // Recompute energy.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energyGrid[y][x] = energy(x, y);
            }
        }
    }
 
    
    /**
     * Method to relax the energy vertically (update vertical dist).
     */
    private void relaxVertical() {
        
        // Initialize the first line with the energy.
        for (int x = 0; x < width; x++) {
            verticalDist[0][x] = energyGrid[0][x];
        }
        
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
     * Method to relax the energy horizontally (update vertical dist).
     */
    private void relaxHorizontal() {
        
        // Initialize the first column with the energy.
        for (int y = 0; y < height; y++) {
            horizontalDist[y][0] = energyGrid[y][0];
        }
        
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                
                // get the minimum cost from previous column
                double minPrev = horizontalDist[y][x - 1];
                if (y > 0) minPrev = Math.min(minPrev, horizontalDist[y - 1][x - 1]);
                if (y < height - 1) minPrev = Math.min(minPrev, horizontalDist[y + 1][x - 1]);
                horizontalDist[y][x] = energyGrid[y][x] + minPrev;
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
