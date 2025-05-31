import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private final int n; // number of team in the file.
    private final String[] idToTeam; // mapping of teams id to their name.
    private final HashMap<String, Integer> teamToId; // mapping teams name with their id.
    private final int[][] g; // number of planned match between team i-j.
    private final int[] w; // number of win.
    private final int[] losses; // number of losses.
    private final int[] r; // number of remaining game.
    private final boolean[] eliminated; // keep track of the eliminated team.
    private final Bag<Integer>[] eliminationCertificates; // Keep track of the set of teams that eliminate a given team.

    /**
     * Create a baseball division from given txt filename.
     * 
     * @param filename text file containing the informations of the teams.
     */
    public BaseballElimination(String filename) {
        In file = new In(filename);

        // get the number of team.
        this.n = file.readInt();

        // Initialize all variables.
        this.idToTeam = new String[n];
        this.teamToId = new HashMap<>();
        this.g = new int[n][n];
        this.w = new int[n];
        this.losses = new int[n];
        this.r = new int[n];
        this.eliminated = new boolean[n];
        this.eliminationCertificates = (Bag<Integer>[]) new Bag[n];

        // extract all the other informations.
        for (int i = 0; i < n; i++) {

            // Extract the name of the team.
            String team = file.readString();
            idToTeam[i] = team;
            teamToId.put(team, i);
            eliminationCertificates[i] = new Bag<>();

            // Extract all the other informations: in order: wins/loss/left
            this.w[i] = file.readInt(); // wins
            this.losses[i] = file.readInt(); // losses
            this.r[i] = file.readInt(); // remaining matches.

            // Extract the information concerning the remaining matches.
            for (int j = 0; j < n; j++) {
                g[i][j] = file.readInt();
            } // end for.

        } // end for.

        // Complete the elimination matrix
        for (int i = 0; i < n; i++) {
            checkNonTrivialElimination(i);
            checkTrivialElimination(i); // update the trivial detection.
        }
    }

    /**
     * Number of teams.
     * 
     * @return the number of teams.
     */
    public int numberOfTeams() {
        return n;
    }

    /**
     * all teams
     * 
     * @return An iterable containing all teams.
     */
    public Iterable<String> teams() {
        return teamToId.keySet();
    }

    /**
     * number of wins for a given team.
     * 
     * @param team to display the wins.
     * @return the number of wins.
     */
    public int wins(String team) {

        if (!teamToId.containsKey(team))
            throw new IllegalArgumentException("The team passed in the method 'wins' does not exist");

        int id = teamToId.get(team);
        return w[id];
    }

    /**
     * number of losses for a given team.
     * 
     * @param team to display the losses.
     * @return the number of losses.
     */
    public int losses(String team) {
        if (!teamToId.containsKey(team))
            throw new IllegalArgumentException("The team passed in the method 'wins' does not exist");

        int id = teamToId.get(team);
        return losses[id];
    }

    /**
     * number of remaining games for given team.
     * 
     * @param team to display the remaining games.
     * @return
     */
    public int remaining(String team) {
        if (!teamToId.containsKey(team))
            throw new IllegalArgumentException("The team passed in the method 'wins' does not exist");

        int id = teamToId.get(team);
        return r[id];
    }

    /**
     * number of remaining games between team1 and team2.
     * 
     * @param team1
     * @param team2
     * @return
     */
    public int against(String team1, String team2) {
        if (!teamToId.containsKey(team1) || !teamToId.containsKey(team2))
            throw new IllegalArgumentException("The team passed in the method 'wins' does not exist");

        int id1 = teamToId.get(team1);
        int id2 = teamToId.get(team2);
        return g[id1][id2];
    }

    /**
     * is given team eliminated?
     * 
     * @param team
     * @return
     */
    public boolean isEliminated(String team) {
        if (!teamToId.containsKey(team))
            throw new IllegalArgumentException("The team passed in the method 'wins' does not exist");

        int id = teamToId.get(team);
        return eliminated[id];
    }

    /**
     * subset R of teams that eliminates given team; null if not eliminated.
     * 
     * @param team
     * @return
     */
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamToId.containsKey(team))
            throw new IllegalArgumentException("The team passed in the method 'wins' does not exist");

        int id = teamToId.get(team);

        Bag<String> result = new Bag<>();

        for (int teamId : eliminationCertificates[id]) {
            result.add(idToTeam[teamId]);
        } // end if

        if (result.isEmpty()) return null;
        return result;
    }
    
    /**
     * Method to check trivial elimination.
     * @param id of the team to check.
     */
    private void checkTrivialElimination(int id) {
        
        for (int i = 0; i < n; i++) {
            if (w[id] + r[id] < w[i]) {
                eliminated[id] = true;
                eliminationCertificates[id].add(i);
            }
        }
    }
    
    /**
     * Helper method to check if a team is non-trivially eliminated. Also compute
     * the Ford Fulkerson algorithm to update the set of team that eliminate another
     * team.
     * 
     * @param id The id of the team to check.
     * @return
     */
    private void checkNonTrivialElimination(int id) {

        // Create the flowNetwork
        // number of vertices: n (We will create vertices for all teams) +
        // binomialCoefficient(n, 2) (Other games) + 2 (sources vertices).

        int gameVertices = binomialCoef(n, 2); // Calculate the number of match remaining between teams not including
        // the team to check elimination.

        int v = n + gameVertices + 2; // Number of vertices.
        FlowNetwork network = new FlowNetwork(v);

        /*
         * Connection of vertices (vertex 0): Source vertex connected to all vertices of
         * otherGames (match vertices -> 1 to binomialCoefficient(n, 2)) -> Vertex 1
         * being the first match reported in the g matrix, we ignore all match where the
         * considered team is involved. from vertex binomialCoefficient(n, 2) up to n-2
         * -> all the teams in order minus the checked team. n-1: sink vertex. The
         * analyzed team is represented as a non connected vertex.
         */

        int totalGames = 0;
        int indexCounter = 0; // Counter to keep track of the vertices to create edges on from the source.
        int teamStartIndex = 1 + gameVertices;

        // Create the edges from the sources to the match, from the match to the
        // corresponding teams
        for (int i = 0; i < n; i++) {

            // Create the edges between teams and sink with weight being equal to the
            // maximum of victory possible without eliminating the team.
            double maxCapacity = w[id] + r[id] - w[i];
            if (maxCapacity < 0)
                maxCapacity = 0;
            if (i != id)
                network.addEdge(new FlowEdge(teamStartIndex + i, v - 1, maxCapacity));

            for (int j = i + 1; j < n; j++) {
                if (i == id || j == id) {
                    indexCounter++;
                    continue; // do not consider the analyzed team.
                }

                int games = g[i][j]; // number of games between team i and j.

                totalGames += games;
                network.addEdge(new FlowEdge(0, indexCounter + 1, games));
                network.addEdge(new FlowEdge(indexCounter + 1, teamStartIndex + i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(indexCounter + 1, teamStartIndex + j, Double.POSITIVE_INFINITY));
                indexCounter++;
            } // end for.
        } // end for.

        // compute FordFulkerson algorithm
        FordFulkerson ff = new FordFulkerson(network, 0, v - 1);
        double maxFlow = ff.value();
        if (maxFlow == totalGames) {
            eliminated[id] = false;
            return;
        }

        // If the team is detected as eliminated, compute the minflow to dected which
        // team is being eliminated.
        eliminated[id] = true;

        // Check if team vertices are reachable or not (belong to the minCut and not
        // having full capacity.
        for (int i = teamStartIndex; i < v - 1; i++) {
            if (ff.inCut(i)) {
                eliminationCertificates[id].add(i - teamStartIndex); // Get the real id of the team and not the id of
                // the vertices.
            }
        } // end for

        return;
    }

    /**
     * Helper method to help calculating binomial coefficient.
     * 
     * @param n
     * @param k
     * @return Binomial coefficient.
     */
    private static int binomialCoef(int n, int k) {
        int res = 1;

        if (k > n - k) {
            k = n - k;
        }

        for (int i = 0; i < k; i++) {
            res *= n - i;
            res /= (i + 1);
        }

        return res;
    }

    // testing function
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
