package src.domainclasses;
import java.util.ArrayList;

public class Standing {
    private ArrayList<Team> teamStandings; // This is an IN-ORDER list of the teams based on standings rules

    // GETTERS
    public ArrayList<Team> getTeamStandings() {
        return this.teamStandings;
    }

    // FUNCTIONS

    // TODO: i think the logics wrong on the loop here (its a bubble sort)
    public void sortStandings() {
        for (int i = 0; i < teamStandings.size() - 1; i++) {
            for (int j = 0; j < teamStandings.size() - 1 - i; j++) {            
                Team a = teamStandings.get(i);
                Team b = teamStandings.get(j + 1);

                boolean swap = false;

                if (a.getTeamRecordPointsFor() < b.getTeamRecordPointsFor()) {
                    swap = true;
                } 
                else if (a.getTeamRecordPointsFor() == b.getTeamRecordPointsFor()) {
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
