import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private int numberOfSegments = 0;

    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {

        if (points == null)
            throw new IllegalArgumentException("null argument!");

        int n = points.length;

        // list of the segments
        ArrayList<LineSegment> tempSegments = new ArrayList<>();

        // create a deepcopy of the array that will be arranged according to the
        // reference point selected in points.
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("null element!");

            for (int j = 0; j < i; j++)
                if (points[i] == copy[j])
                    throw new IllegalArgumentException("No duplicates allowed in the array of points!");

            copy[i] = points[i];
        }

        for (int i = 0; i < n; i++) {

            // Arrange the array by the slopeOrder considering the reference point in
            // points[i]
            Arrays.sort(copy, points[i].slopeOrder());

            // We only want segment of at least 4 colinear points, so if less than 2 points
            // left: stop the iteration.
            // Use the increment to jump after each clusters of points having the same
            // slope.
            // Start at 1 because the index 0 will contains the reference point (-infinite)
            int increment = 1;
            for (int j = 1; j < n - 2; j = j + increment) {

                // reinitialize the increment to 1
                increment = 1;

                // Keep track of all the points detected having the same slope from the
                // reference point
                ArrayList<Point> colinearPoints = new ArrayList<>();

                // add the first comparison points
                colinearPoints.add(copy[0]);
                colinearPoints.add(copy[j]);

                // increment j until the one of the next point does not have the same slope,
                // adding all the points having the same

                while (j + increment < n && copy[0].slopeTo(copy[j]) == copy[0].slopeTo(copy[j + increment])) {
                    colinearPoints.add(copy[j + increment]);
                    increment++;
                }

                // if at least 4 points:
                if (colinearPoints.size() >= 4) {

                    // sort the points having the same slope and create the segment using the first
                    // and the last points (extremum of the segment)
                    // (we will have the same segment for the same groups of points, whatever the
                    // order they have been detected.
                    Collections.sort(colinearPoints);

                    // Create the segment only if the reference point points[i] is the smallest
                    // point of the segment. (to avoid duplicates)
                    if (copy[0].compareTo(colinearPoints.get(0)) == 0) {
                        tempSegments.add(
                                new LineSegment(colinearPoints.get(0), colinearPoints.get(colinearPoints.size() - 1)));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.numberOfSegments);
    }
}
