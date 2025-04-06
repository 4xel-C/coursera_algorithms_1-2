import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private int numberOfSegments = 0;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {

        if (points == null)
            throw new IllegalArgumentException("null argument!");

        // Number of points
        int n = points.length;

        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("null element!");
            for (int j = 0; j < i; j++)
                if (points[i] == copy[j])
                    throw new IllegalArgumentException("No duplicates allowed in the array of points!");
            copy[i] = points[i];
        }

        // list of the segments
        ArrayList<LineSegment> tempSegments = new ArrayList<>();

        Arrays.sort(copy);

        for (int p = 0; p < n; p++) {
            for (int q = p + 1; q < n; q++) {
                for (int r = q + 1; r < n; r++) {
                    for (int s = r + 1; s < n; s++) {

                        // Check if they have the same slope
                        if (copy[p].slopeTo(copy[q]) == copy[p].slopeTo(copy[r])
                                && copy[p].slopeTo(copy[r]) == copy[p].slopeTo(copy[s])) {

                            // Add the segment to the segment list
                            LineSegment segment = new LineSegment(copy[p], copy[s]);
                            tempSegments.add(segment);
                            numberOfSegments++;
                        }
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
        return segments.clone();
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
