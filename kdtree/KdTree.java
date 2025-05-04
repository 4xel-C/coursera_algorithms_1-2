import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;
    private int size = 0;

    /**
     * Inner class representing a node of the kd-tree.
     * 
     */
    private class Node {
        double x;
        double y;
        Point2D point;
        int level; // Dimension to sort the next node: 0 -> x or 1 -> y.

        Node parent;
        Node left;
        Node right;

        Node(Point2D point, Node parent) {
            this.x = point.x();
            this.y = point.y();
            this.point = point;
            if (parent != null) {
                this.level = (parent.level + 1) % 2;
                this.parent = parent;
            }
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(p, root, null);
    }

    private Node insert(Point2D p, Node node, Node parent) {
        if (node == null) {
            size++;
            return new Node(p, parent);
        }

        // If we try to insert a node already in the tree.
        if (p.equals(node.point))
            return node;

        // if the tree is not null.
        // Check level.
        if (node.level == 0) {
            if (p.x() < node.x)
                node.left = insert(p, node.left, node);
            else if (p.x() > node.x)
                node.right = insert(p, node.right, node);
            else {
                if (p.y() < node.y)
                    node.left = insert(p, node.left, node);
                else
                    node.right = insert(p, node.right, node);
            }
        } else {
            if (p.y() < node.y)
                node.left = insert(p, node.left, node);
            else if (p.y() > node.y)
                node.right = insert(p, node.right, node);
            else {
                if (p.x() < node.x)
                    node.left = insert(p, node.left, node);
                else
                    node.right = insert(p, node.right, node);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        return contains(p, root);
    }

    private boolean contains(Point2D p, Node node) {
        if (node == null)
            return false;
        if (node.point.equals(p))
            return true;

        // search the left or right node depending of the level

        // Vertical line (x comparison)
        if (node.level == 0) {
            if (p.x() < node.x)
                return contains(p, node.left);
            else if (p.x() > node.x)
                return contains(p, node.right);
            else {
                if (p.y() < node.y)
                    return contains(p, node.left);
                else
                    return contains(p, node.right);
            }

        } else if (node.level == 1) {
            if (p.y() < node.y)
                return contains(p, node.left);
            else if (p.y() > node.y)
                return contains(p, node.right);
            else {
                if (p.x() < node.x)
                    return contains(p, node.left);
                else
                    return contains(p, node.right);
            }
        }
        return false;
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null)
            return;
        // pre-order draw.

        // Draw the point.
        StdDraw.setPenColor(StdDraw.BLACK);
        node.point.draw();

        // draw the line depending of the level.
        if (node.level == 0) {

            // vertical red line
            StdDraw.setPenColor(StdDraw.RED);
            if (node.parent == null) {
                StdDraw.line(node.x, 0, node.x, 1);
            } else if (node.y < node.parent.y) {
                StdDraw.line(node.x, 0, node.x, node.parent.y);
            } else if (node.y > node.parent.y) {
                StdDraw.line(node.x, node.parent.y, node.x, 1);
            }

        } else if (node.level == 1) {

            // horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            if (node.x < node.parent.x) {
                StdDraw.line(0, node.y, node.parent.x, node.y);
            } else if (node.x > node.parent.x) {
                StdDraw.line(node.parent.x, node.y, 1, node.y);
            }
        }
        draw(node.left);
        draw(node.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        
        // The array list will store all points in range of the given rectangle.
        ArrayList<Point2D> result = new ArrayList<>();
        
        // Create the subarea of the node
        RectHV area = new RectHV(0, 0, 1, 1);

        // call the function passing the root and the pointer to the result list to
        // update the result.
        range(rect, root, area, result);

        return result;
    }

    private void range(RectHV rect, Node node, RectHV area, ArrayList<Point2D> list) {

        if (node == null)
            return;

        // if the point is in the rectangle, add it to the list.
        if (rect.contains(node.point))
            list.add(node.point);
        
        
        // Generate the two sub areas
        RectHV leftArea = null;
        RectHV rightArea = null;
        
            // Horizontal split
        if (node.level == 0) {
            leftArea = new RectHV(area.xmin(), area.ymin(), node.x, area.ymax());
            rightArea = new RectHV(node.x, area.ymin(), area.xmax(), area.ymax());
            
            // Check if the query rectangle intersect with the sub areas
            
            // If intersection with leftArea, explore left node.
            if (rect.intersects(leftArea)) {
                range(rect, node.left, leftArea, list);
            }
            
            // If intersection with rigthArea, explore right node.
            if (rect.intersects(rightArea)) {
                range(rect, node.right, rightArea, list);
            }
        
            // Vertical split
        } else if (node.level == 1) {
            leftArea = new RectHV(area.xmin(), area.ymin(), area.xmax(), node.y);
            rightArea = new RectHV(area.xmin(), node.y, area.xmax(), area.ymax()); 
            
            // If intersection with leftArea, explore left node.
            if (rect.intersects(leftArea)) {
                range(rect, node.left, leftArea, list);
            }
            
            // If intersection with rigthArea, explore right node.
            if (rect.intersects(rightArea)) {
                range(rect, node.right, rightArea, list);
            }
        }
    }

    public Point2D nearest(Point2D p) {

        // Create an array containing only 1 point to pass the pointer to the recursive
        // function, updating in-place the closest point.
        // Pass the area to recursively subdivise it during the code
        Point2D[] closestPointPointer = { null };
        nearest(p, root, closestPointPointer, new RectHV(0, 0, 1, 1));
        return closestPointPointer[0];
    }

    private void nearest(Point2D p, Node node, Point2D[] pointer, RectHV area) {

        if (node == null)
            return;

        // Update the closest point.
        if (pointer[0] == null || p.distanceSquaredTo(node.point) < p.distanceSquaredTo(pointer[0]))
            pointer[0] = node.point; // initialize with the first point.

        // Subdivise the area
        RectHV leftNodeArea = null, rightNodeArea = null;

        if (node.level == 0) { // Vertical split
            leftNodeArea = new RectHV(area.xmin(), area.ymin(), node.x, area.ymax());
            rightNodeArea = new RectHV(node.x, area.ymin(), area.xmax(), area.ymax());

        } else if (node.level == 1) { // Horizontal split
            leftNodeArea = new RectHV(area.xmin(), area.ymin(), area.xmax(), node.y);
            rightNodeArea = new RectHV(area.xmin(), node.y, area.xmax(), area.ymax());
        }

        // Explore both left and right starting by the same side of the query point

        Node first, second;
        RectHV firstArea, secondArea;

        if ((node.level == 0 && p.x() < node.x) || (node.level == 1 && p.y() < node.y)) {
            first = node.left;
            second = node.right;
            firstArea = leftNodeArea;
            secondArea = rightNodeArea;
        } else {
            first = node.right;
            second = node.left;
            firstArea = rightNodeArea;
            secondArea = leftNodeArea;
        }

        nearest(p, first, pointer, firstArea);
        if (secondArea.distanceSquaredTo(p) < p.distanceSquaredTo(pointer[0])) {
            nearest(p, second, pointer, secondArea);
        }
    }
}
