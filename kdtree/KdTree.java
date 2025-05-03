import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    Node root;

    /**
     * Inner class representing a node of the kd-tree.
     * 
     */
    public class Node {
        double x;
        double y;
        Point2D point;
        int level; // Dimension to sort the next node: 0 -> x or 1 -> y.

        Node parent;
        Node left;
        Node right;

        public Node(Point2D point, Node parent) {
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
        if (root == null)
            return true;
        return false;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null)
            return 0;
        return 1 + size(node.left) + size(node.right);
    }

    public void insert(Point2D p) {
        root = insert(p, root, null);
    }

    private Node insert(Point2D p, Node node, Node parent) {
        if (node == null)
            return new Node(p, parent);

        // If we try to insert a node already in the tree.
        if (p.equals(node.point))
            return node;

        // if the tree is not null.
        // Check level.
        if (node.level == 0) {

            // compare on x basis, tie by y.
            if (p.x() < node.x) {
                node.left = insert(p, node.left, node);
            } else if (p.x() > node.x) {
                node.right = insert(p, node.right, node);
            } else {
                if (p.y() < node.y) {
                    node.left = insert(p, node.left, node);
                } else if (p.y() > node.y) {
                    node.right = insert(p, node.right, node);
                }
            }

        } else if (node.level == 1) {
            // compare on y basis, tie by x.
            if (p.y() < node.y) {
                node.left = insert(p, node.left, node);
            } else if (p.y() > node.y) {
                node.right = insert(p, node.right, node);
            } else {
                if (p.x() < node.x) {
                    node.left = insert(p, node.left, node);
                } else if (p.x() > node.x) {
                    node.right = insert(p, node.right, node);
                }
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
        return contains(p, node.left) || contains(p, node.right);
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

        // call the function passing the root and the pointer to the result list to
        // update the result.
        range(rect, root, result);

        return result;
    }

    private void range(RectHV rect, Node node, ArrayList<Point2D> list) {

        if (node == null)
            return;

        // if the point is in the rectangle, add it to the list.
        if (rect.contains(node.point))
            list.add(node.point);

        // Whether the rectangle contains or not the point, check both child to see if
        // there is other potential points intersecting with the rectangle.
        // Check the level of the node and compare x or y method.
        if (node.level == 0) {
            if (rect.xmin() < node.x)
                range(rect, node.left, list); // check left node if on the left partition.
            if (rect.xmax() > node.x)
                range(rect, node.right, list); // check right node if on the right partition.

        } else if (node.level == 1) {
            if (rect.ymin() < node.y)
                range(rect, node.left, list); // check left node if rect on the lower partition.
            if (rect.ymax() > node.y)
                range(rect, node.right, list); // check right node if rect on the upper partition.
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
        if (pointer[0] == null || p.distanceTo(node.point) < p.distanceTo(pointer[0]))
            pointer[0] = node.point; // initialize with the first point.

        // Subdivise the aera
        RectHV leftNodeArea = null, rightNodeArea = null;

        if (node.level == 0) { // Vertical split
            leftNodeArea = new RectHV(area.xmin(), area.ymin(), node.x, area.ymax());
            rightNodeArea = new RectHV(node.x, area.ymin(), area.xmax(), area.ymax());

        } else if (node.level == 1) { // Horizontal split
            leftNodeArea = new RectHV(area.xmin(), area.ymin(), area.xmax(), node.y);
            rightNodeArea = new RectHV(area.xmin(), node.y, area.xmax(), area.ymax());
        }

        // Explore both left and right starting by the same side of the query point
  
        if ((node.level == 0 && p.x() < node.x) || (node.level == 1 && p.y() < node.y)) {
            nearest(p, node.left, pointer, leftNodeArea);

            // Pruning the right zone if the rectangle if further away than the query point.
            if (rightNodeArea.distanceTo(p) < p.distanceTo(pointer[0])) {
                nearest(p, node.right, pointer, rightNodeArea);
            }

        } else if ((node.level == 0 && p.x() > node.x) || (node.level == 1 && p.y() > node.y)) {

            // Explore the side containing
            nearest(p, node.right, pointer, rightNodeArea);

            // Pruning the left zone if the rectangle if further away than the query point.
            if (leftNodeArea.distanceTo(p) < p.distanceTo(pointer[0])) {
                nearest(p, node.left, pointer, leftNodeArea);
            }
        }
    }

    // Testing the class and the methods.
    public static void main(String[] args) {

        KdTree tree = new KdTree();
        System.out.println("Empty? " + tree.isEmpty());
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        System.out.println("Empty? " + tree.isEmpty());
        System.out.println("Size" + tree.size());
        System.out.println("Contains 0.1 / 0.2? " + tree.contains(new Point2D(0.1, 0.2)));

        Point2D nearest = tree.nearest(new Point2D(0.3, 0.3));

        System.out.println("nearest x: " + nearest.x());
        System.out.println("nearrest y :" + nearest.y());

        RectHV rect = new RectHV(0.3, 0.3, 0.8, 0.9);
        ArrayList<Point2D> points = new ArrayList<>();
        points = (ArrayList<Point2D>) tree.range(rect);

        for (Point2D point : points) {
            System.out.println(point.x());
        }

        tree.draw();
    }

}
