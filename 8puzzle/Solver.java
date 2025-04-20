import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private int moves;

    private SearchNode goalNode;

    // Innerclass representing each state of the game
    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        SearchNode previousNode;

        int moves;
        int manhattan;
        int priority;

        public SearchNode(Board board, int move, SearchNode previousNode) {
            this.previousNode = previousNode;
            this.board = board;
            this.moves = move;
            this.manhattan = board.manhattan();

            // priority using Manhattan function
            this.priority = moves + manhattan;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    public Solver(Board initial) {

        // Initializing the Min priority queues for the the A* algorithm to select
        // heuristicly the correct node
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();

        if (initial == null)
            throw new IllegalArgumentException("Board to solve cannot be null");

        Board twin = initial.twin();

        // initializing the search node with the initial and the twin state.
        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(twin, 0, null));

        while (true) {

            // Get the most prioritized node of each pq
            SearchNode node = pq.delMin();
            SearchNode twinNode = pqTwin.delMin();

            // If solution is found: update the values
            if (node.board.isGoal()) {
                this.goalNode = node;
                this.moves = node.moves;
                return;

                // if solution if found in twinNode -> no solution
            } else if (twinNode.board.isGoal()) {
                this.moves = -1;
                return;

            } else {

                // Iterate throughout all neighbors and add items to the pq
                for (Board board : node.board.neighbors()) {

                    // Avoid adding the same board to the pq
                    if (node.previousNode != null && board.equals(node.previousNode.board))
                        continue;

                    // Insert each new node updating the values depending of the current search
                    // node.
                    pq.insert(new SearchNode(board, node.moves + 1, node));
                }
            }

            // Iterate throughout all neighbors of the twinNode and add items to the
            // respective pq
            for (Board board : twinNode.board.neighbors()) {

                // Avoid adding the same board to the pq
                if (board == twinNode.board)
                    continue;

                // Insert each new node updating the values depending of the current search
                // node.
                pqTwin.insert(new SearchNode(board, twinNode.moves + 1, twinNode));
            }
        }
    }

    public boolean isSolvable() {
        return moves >= 0;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        // Initialize a stack storing all solutions from the last node up to the first
        // node
        Stack<Board> solutions = new Stack<>();

        SearchNode node = goalNode;

        while (node != null) {
            solutions.push(node.board);
            node = node.previousNode;
        }
        return solutions;
    }

}
