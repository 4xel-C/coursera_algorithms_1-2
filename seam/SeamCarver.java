import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

public class SeamCarver {
    
    private Picture picture;
    private int height;
    private int width;
    private EdgeWeightedDigraph pixelsGraph; // Representation of the picture inside a digraph ? Try other representation of graph with weighed vertex? Try to put the weight on the edges using the vertex values.
    
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
    }
    
    /**
     * Current picture.
     * @return
     */
    public Picture picture() {
        return null;
    }
    
    /**
     * Width of the current picture.
     * @return the width.
     */
    public int width() {
        return -1;
    }
    
    
    /**
     * Return the height of the current picture.
     * @return height.
     */
    public int height() {
        return -1;
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
     * Sequence of indices for horizontal seam.
     */
    public int[] findHorizontalSeam() {
        return null;
    }
    
    
    /**
     * Sequence of indices for vertical seam.
     * @return
     */
    public int[] findVerticalSeam() {
        return null;
    }
    
    
    /**
     * Remove the horizontal seam from current picture.
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        
    }
    
    
    /**
     * Remove vertical seam from the current picture.
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        
    }
    
    
    /**
     * Unit testing.
     * @param args
     */
    public static void main(String[] args) {
        
    }

}
