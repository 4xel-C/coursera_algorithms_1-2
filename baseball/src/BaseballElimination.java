public class BaseballElimination {
    
    /**
     * Create a baseball division from given txt filename.
     * @param filename text file containing the informations of the teams.
     */
    public BaseballElimination(String filename) {
        return;
    }
    
    
    /**
     * Number of teams.
     * @return the number of teams.
     */
    public int numberOfTeams() {
        return -1;
    }
    
    
    /**
     * all teams
     * @return An iterable containing all teams.
     */
    public Iterable<String> teams() {
        return null;
    }
    
    
    /**
     * number of wins for a given team.
     * @param team to display the wins.
     * @return the number of wins.
     */
    public int wins(String team) {
        return -1;
    }
    
    
    /**
     * number of losses for a given team.
     * @param team to display the losses.
     * @return the number of losses.
     */
    public int losses(String team) {
        return -1;
    }
    
    
    /**
     * number of remaining games for given team.
     * @param team to display the remaining games.
     * @return
     */
    public int remaining(String team) {
        return -1;
    }
    
    
    /**
     * number of remaining games between team1 and team2.
     * @param team1
     * @param team2
     * @return
     */
    public int against(String team1, String team2) {
        return -1;
    }
    
    
    /**
     * is given team eliminated?
     * @param team
     * @return
     */
    public boolean isEliminated(String team) {
        return false;
    }
    
    
    /**
     * subset R of teams that eliminates given team; null if not eliminated.
     * @param team
     * @return
     */
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

}
