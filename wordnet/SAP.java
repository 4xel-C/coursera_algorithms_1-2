import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    Digraph graph;

    
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Null values passed to the constructor!");

        // create a deep copy.
        this.graph = new Digraph(G);
    }

    
    public int length(int v, int w) {

        // Check vertices
        validateVertex(v);
        validateVertex(w);

        // keep track of the distance from v for the common vertexes.
        int shortestDistance = -1;

        // Get the shortest path to the root.
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(graph, w);

        // Check common vertices from v and w and compute the distance.
        for (int x = 0; x < graph.V(); x++) {

            // If we find a common ancestor
            if (bfsv.hasPathTo(x) && bfsw.hasPathTo(x)) {

                int dist = bfsv.distTo(x) + bfsw.distTo(x);

                if (dist < shortestDistance || shortestDistance == -1) {
                    shortestDistance = dist;
                }
            } // end if
        } // end for

        return shortestDistance;
    }

    
    public int ancestor(int v, int w) {

        validateVertex(v);
        validateVertex(w);

        // keep track of the distance from v for the common vertexes.
        int commonAncestor = -1;
        int shortestDistance = -1;

        // Get the shortest path to the root.
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(graph, w);

        // Check common vertexes from v and w and compute the distance.
        for (int x = 0; x < graph.V(); x++) {

            // If we find a common ancestor
            if (bfsv.hasPathTo(x) && bfsw.hasPathTo(x)) {
                int dist = bfsv.distTo(x) + bfsw.distTo(x);

                if (dist < shortestDistance || shortestDistance == -1) {
                    commonAncestor = x;
                    shortestDistance = dist;
                }
            } // end if
        } // end for
        return commonAncestor;
    }

    
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        
        // Check vertices sets.
        validateVertices(v);
        validateVertices(w);
        
        // Declare and initialize variables
        int shortestDistance = -1;
        
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(graph, w);

     // Check common vertices from v and w and compute the distance.
        for (int x = 0; x < graph.V(); x++) {

            // If we find a common ancestor
            if (bfsv.hasPathTo(x) && bfsw.hasPathTo(x)) {

                int dist = bfsv.distTo(x) + bfsw.distTo(x);

                if (dist < shortestDistance || shortestDistance == -1) {
                    shortestDistance = dist;
                }
            } // end if
        } // end for

        return shortestDistance;
    }

    
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        
        // Check vertices sets.
        validateVertices(v);
        validateVertices(w);
        
        // Declare and initialize variables
        int shortestDistance = -1;
        int commonAncestor = -1;
        
        // Use a single BreadthFirstSearch on all vertices.
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(graph, w);

     // Check common vertices from v and w and compute the distance.
        for (int x = 0; x < graph.V(); x++) {

            // If we find a common ancestor
            if (bfsv.hasPathTo(x) && bfsw.hasPathTo(x)) {

                int dist = bfsv.distTo(x) + bfsw.distTo(x);

                if (dist < shortestDistance || shortestDistance == -1) {
                    commonAncestor = x;
                    shortestDistance = dist;
                }
            } // end if
        } // end for

        return commonAncestor;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= graph.V())
            throw new IllegalArgumentException("Vertices out of range");
    }

    private void validateVertices(Iterable<Integer> v) {
        for (Integer vertex : v) {
            if (vertex == null || vertex < 0 || vertex >= graph.V())
                throw new IllegalArgumentException("One of the vertex is null or out of range");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
