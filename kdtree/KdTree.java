import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

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
            if (parent != null)  {
                this.level = (parent.level + 1) % 2;
                this.parent = parent;
            }
        }
    }
    
    public boolean isEmpty() {
        if (root == null) return true;
        return false;        
    }
    
    public int size() {
        return size(root);
    }
    
    private int size(Node node) {
        if (node == null) return 0;
        return 1 + size(node.left) + size(node.right);
    }

    public void insert(Point2D p) {
        root = insert(p, root, null);
    }
    
    private Node insert(Point2D p, Node node, Node parent) {
        if (node == null) return new Node(p, parent);
        
        // If we try to insert a node already in the tree.
        if (p.equals(node.point)) return node;
        
        // if the tree is not null.
        // Check level.
        if (node.level == 0) {
            
            // compare on x basis, tie by y.
            if (p.x() < node.x) {
                node.left = insert(p, node.left, node);
            } else if (p.x() > node.x)  {
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
            } else if (p.y() > node.y)  {
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
        if (node == null) return false;
        if (node.point.equals(p)) return true;
        return contains(p, node.left) || contains(p, node.right);
    }
    
    public void draw() {
        draw(root);
    }
    
    private void draw(Node node)  {
        if (node == null) return;
        // inorder draw
        draw(node.left);
        node.point.draw();
        draw(node.right);
    }
    
    

}
