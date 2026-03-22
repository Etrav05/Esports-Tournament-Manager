package src.domainclasses;
import java.util.ArrayList;

public class Tournament {
    private String name;
    private String format;
    private ArrayList<Team> teams;
    private TournamentState tournamentState;
    private int maxPlayers;
    private int rounds;

    // GETTERS
    public String getName() {
        return this.name;
    }

    public String getFormat() {
        return this.format;
    }

    public ArrayList<Team> getTeams() {
        return this.teams;
    }

    public TournamentState getTournamentState() {
        return this.tournamentState;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getRounds() {
        return this.rounds;
    }

     // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public void setTournamentState(TournamentState tournamentState) {
        this.tournamentState = tournamentState;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
}
