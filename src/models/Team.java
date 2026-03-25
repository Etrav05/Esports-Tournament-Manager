package src.models;
import java.util.ArrayList;

public class Team {
    private String name;
    private ArrayList<Player> players;
    private Stats stats;

    // GETTERS
    public String getName() {
        return this.name;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public Stats getStats() {
        return this.stats;
    }

    // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    // FUNCTIONS
    public int getTeamStatsPointsFor() {
        return this.stats.getPointsFor();
    }

    public double getTeamStatsPointsRatio() {
        return this.stats.getPointsRatio();
    }

    public int getTeamStatsWins() {
        return this.stats.getWins();
    }

    public double getTeamStatsLosses() {
        return this.stats.getLosses();
    }
}
