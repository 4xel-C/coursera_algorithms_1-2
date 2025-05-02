import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    
    SET<Point2D> points;
    
    // Constructor method: declare a new empty set of points.
    public PointSET() {
        this.points = new SET<>();
    }
    
    public boolean isEmpty() {
        return points.isEmpty();
    }
    
    public int size() {
        return points.size();
    }
    
    public void insert(Point2D point) {
        points.add(point);
    }
    
    public boolean contains(Point2D point) {
        return points.contains(point);
    }
    
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> pointsInRange = new SET<>();
        
        for (Point2D point : points) {
            if (rect.contains(point)) pointsInRange.add(point);
        }
        
        return pointsInRange;
    }
    
    public Point2D nearest(Point2D p) {
        double minDistance = Double.POSITIVE_INFINITY; // Declare the minDistance being +infinite for comparison.
        Point2D closestPoint = null;
        
        for (Point2D point : points) {
            if (point == p) continue;
            
            if (p.distanceTo(point) > minDistance) {
                closestPoint = point;
            }
        }
        
        return closestPoint;
    }

    
    
}
