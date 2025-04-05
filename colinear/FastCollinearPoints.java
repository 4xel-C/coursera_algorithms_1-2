import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private int numberOfSegments = 0;

    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        // TODO: To be implemented

        int n = points.length;

        // list of the segments
        ArrayList<LineSegment> tempSegments = new ArrayList<>();

        for (int i = 0; i < n; i++) {

            // Sort the array according to the concerned point (The point will be minimum of
            // the array (because his slope with himself is -infinite, then sort by
            // coordinates for same slope)
            Arrays.sort(points, points[i].slopeOrder().thenComparing(Point::compareTo));

            // We only want segment of at least 4 colinear points.
            for (int j = 1; j < n - 2; j++) {

                // Count the number of colinear point with points[0] detected
                int colinearCounter = 1;

                while (j + colinearCounter < n
                        && points[0].slopeTo(points[j]) == points[0].slopeTo(points[j + colinearCounter])) {
                    colinearCounter++;
                }

                // if at least 4 points:
                if (colinearCounter >= 3) {
                    LineSegment segment = new LineSegment(points[0], points[j + colinearCounter - 1]);
                    tempSegments.add(segment);
                    numberOfSegments++;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.numberOfSegments);
    }
}
