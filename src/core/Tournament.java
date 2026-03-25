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
