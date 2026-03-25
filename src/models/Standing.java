package src.models;
import java.util.ArrayList;

public class Standing {
    private ArrayList<Team> teamStandings; // This is an IN-ORDER list of the teams based on standings rules

    // GETTERS
    public ArrayList<Team> getTeamStandings() {
        return this.teamStandings;
    }

    // FUNCTIONS
    public void sortStandings() {
        for (int i = 0; i < teamStandings.size() - 1; i++) {
            for (int j = 0; j < teamStandings.size() - 1 - i; j++) {            
                Team a = teamStandings.get(j);
                Team b = teamStandings.get(j + 1);

                boolean swap = false;

                if (a.getTeamRecordWins() < b.getTeamRecordWins()) {
                    swap = true;
                } 
                else if (a.getTeamRecordWins() == b.getTeamRecordWins()) {
                    if (a.getTeamRecordPointsRatio() < b.getTeamRecordPointsRatio()) {
                        swap = true;
                    } 
                    else if (a.getTeamRecordPointsRatio() == b.getTeamRecordPointsRatio()) {
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
}
