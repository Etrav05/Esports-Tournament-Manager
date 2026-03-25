package src.core;
import java.util.ArrayList;
import src.models.Team;

public class Tournament {
    private String name;
    private TournamentFormat format;
    private ArrayList<Team> teams;
    private TournamentState tournamentState;
    private int maxTeams;

    public Tournament() {
        this.name = "Default";
        this.format = TournamentFormat.SINGLE_ELIM;
        this.teams = new ArrayList<>();
        this.tournamentState = TournamentState.IDLE;
        this.maxTeams = 16;
    }

    // FUNCTIONS
    public int calculateRounds() {
        return this.format.calculateRounds(this.maxTeams);
    }

    // GETTERS
    public String getName() {
        return this.name;
    }

    public TournamentFormat getFormat() {
        return this.format;
    }

    public ArrayList<Team> getTeams() {
        return this.teams;
    }

    public TournamentState getTournamentState() {
        return this.tournamentState;
    }

    public int getMaxTeams() {
        return this.maxTeams;
    }

    public int getRounds() {
        return calculateRounds();
    }

     // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(TournamentFormat format) {
        this.format = format;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public void setTournamentState(TournamentState tournamentState) {
        this.tournamentState = tournamentState;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }
    
}
