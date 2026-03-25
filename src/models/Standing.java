package src.models;
import java.util.ArrayList;

public class Standing {
    private ArrayList<Team> teamStandings; // This is an IN-ORDER list of the teams based on standings rules

    public Standing(ArrayList<Team> teams) {
        this.teamStandings = new ArrayList<>(teams);
    }

    // FUNCTIONS
    public void updateStandings() {
        sortStandings();
    }

    public void display() {
        System.out.println("\n--- STANDINGS ---");
        System.out.println(String.format("%-5s %-15s %-6s %-6s %-10s", "Rank", "Team", "Wins", "Loss", "Ratio"));
        System.out.println("  ──────────────────────────────────────");

        for (int i = 0; i < teamStandings.size(); i++) {
            Team team = teamStandings.get(i);
            System.out.println(String.format("%-5s %-15s %-6s %-6s %-10s",
                (i + 1) + ".",
                team.getName(),
                team.getStats().getWins(),
                team.getStats().getLosses(),
                String.format("%.2f", team.getStats().getPointsRatio())
            ));
        }
    }

    public void sortStandings() { // Bubble sort, could maybe use a better sort but we only work with groups of 32 max so it isnt that important
        for (int i = 0; i < teamStandings.size() - 1; i++) {
            for (int j = 0; j < teamStandings.size() - 1 - i; j++) {            
                Team a = teamStandings.get(j);
                Team b = teamStandings.get(j + 1);

                boolean swap = false;

                if (a.getTeamStatsWins() < b.getTeamStatsWins()) {
                    swap = true;
                } 
                else if (a.getTeamStatsWins() == b.getTeamStatsWins()) {
                    if (a.getTeamStatsPointsRatio() < b.getTeamStatsPointsRatio()) {
                        swap = true;
                    } 
                    else if (a.getTeamStatsPointsRatio() == b.getTeamStatsPointsRatio()) {
                        swap = Math.random() < 0.5;
                    }
                }

                if (swap) {
                    teamStandings.set(j, b);
                    teamStandings.set(j + 1, a);
                }
            }
        }
    }

    // GETTERS
    public ArrayList<Team> getTeamStandings() {
        return this.teamStandings;
    }

    
}
