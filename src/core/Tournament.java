package src.core;
import java.util.ArrayList;
import src.models.Team;
import src.states.CompleteState;
import src.states.IdleState;
import src.states.InprogressState;
import src.states.SaveState;
import src.states.SetupState;
import src.states.TournamentStateHandler;
import src.states.UpdateState;

public class Tournament {
    private String name;
    private TournamentFormat format;
    private ArrayList<Team> teams;
    private TournamentState tournamentState;
    private int maxTeams;
    private TournamentStateHandler stateHandler;

    public Tournament() {
        this.name = "Default";
        this.format = TournamentFormat.SINGLE_ELIM;
        this.teams = new ArrayList<>();
        this.tournamentState = TournamentState.IDLE;
        this.maxTeams = 16;
        this.stateHandler = new IdleState();
    }

    // FUNCTIONS
    public int calculateRounds() {
        return this.format.calculateRounds(this.maxTeams);
    }

    public void transitionTo(TournamentState newState) {
        this.stateHandler.validateTransition(newState);
        this.tournamentState = newState;
        this.stateHandler = resolveState(newState);
    }

    private TournamentStateHandler resolveState(TournamentState state) {
        switch (state) {
            case IDLE -> {        
                return new IdleState();
            }

            case SETUP -> {       
                return new SetupState();
            }

            case INPROGRESS -> {  
                return new InprogressState();
            }

            case UPDATE -> {      
                return new UpdateState();
            }

            case COMPLETE -> {    
                return new CompleteState();
            }

            case SAVE -> {        
                return new SaveState();
            }

            default -> throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    public void basicDisplayTournament() {
        System.out.println("Tournament name: " + this.name);
        System.out.println("Format: " + this.format);
        System.out.println("State: " + this.tournamentState);
        System.out.println("Max Teams: " + this.maxTeams);
        System.out.println("Rounds: " + this.calculateRounds());
        System.out.println("Teams enrolled: " + this.teams.size() + "/" + this.maxTeams);

        if (this.teams.isEmpty()) {
            System.out.println("No teams registered yet");
        } 

        else {
            System.out.println("Teams: ");
            
            for (Team team : this.teams) {
                System.out.println("  - " + team.getName());
            }
        }
    }

    public void displayBracket() {
        int rounds = this.calculateRounds();
        int totalTeams = this.teams.size();

        System.out.println("/------------------------\\");
        System.out.println("[    " + this.name + "    ]");
        System.out.println("\\------------------------/");

        if (totalTeams < 2) {
            System.out.println("  Not enough teams to display bracket.");
            return;
        }

        int matchesInRound = totalTeams / 2;
        int teamIndex = 0;

        for (int round = 1; round <= rounds; round++) {
            System.out.println("  ── Round " + round + " ──────────────────────");

            if (round == 1) {
                for (int match = 0; match < matchesInRound; match++) {
                    String team1 = teamIndex < totalTeams ? teams.get(teamIndex++).getName() : "TBD";
                    String team2 = teamIndex < totalTeams ? teams.get(teamIndex++).getName() : "TBD";

                    System.out.printf("  │  %-10s  vs  %-10s %n", team1, team2);
                }
            } else {
                for (int match = 0; match < matchesInRound; match++) {
                    System.out.printf("  │  %-10s  vs  %-10s %n", "Winner Match" + (match * 2 + 1), "Winner Match" + (match * 2 + 2));
                }
            }

            matchesInRound = matchesInRound / 2;
            System.out.println();
        }

        System.out.println("  ── Champion ───────────────────────");
        System.out.println("  │   None yet   ");
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
