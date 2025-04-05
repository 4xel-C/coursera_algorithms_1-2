import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private int numberOfSegments = 0;
    private LineSegment[] segments;
    
    
    public BruteCollinearPoints(Point[] points) {
        
        // Number of points
        int n = points.length;
        
        // list of the segments
        ArrayList<LineSegment> tempSegments = new ArrayList<>();
        

        for (int p = 0; p < n; p++) {
            for (int q = p + 1; q < n; q++) {
                for (int r = q + 1; r < n; r++) {
                    if (points[p].slopeTo(points[q]) != points[p].slopeTo(points[r])) continue;
                    
                    for (int s = r + 1; s < n; s++) {
                        
                        // Check if they have the same slope
                        if (points[p].slopeTo(points[r]) != points[p].slopeTo(points[s])) continue;
                        
                        // Add the segment to the segment list
                        LineSegment segment = new LineSegment(points[p], points[s]);
                        tempSegments.add(segment);
                        numberOfSegments++;
                    }
                } 
            }
        }
        
        // Update the LineSegment array with the correct size;
        segments = new LineSegment[tempSegments.size()];
        
        for (int i = 0; i < tempSegments.size(); i++) {
            segments[i] = tempSegments.get(i);
        }   
    }
    
    public int numberOfSegments() {
        return numberOfSegments;
    }
    
    public LineSegment[] segments() {
        return segments;
    }
    
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.numberOfSegments);
    }
    
}
