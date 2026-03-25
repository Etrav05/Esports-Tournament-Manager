package src.core;
import java.util.ArrayList;
import java.util.Scanner;
import src.models.Match;
import src.models.Team;
import src.states.CompleteState;
import src.states.IdleState;
import src.states.InprogressState;
import src.states.SaveState;
import src.states.SetupState;
import src.states.TournamentStateHandler;
import src.states.UpdateState;

// Matthew Romano + Evan Travis - March 26th, 2026 - Tournament class implementation
// The actual tournament class that will save and handle matches

public class Tournament {
    private String name;
    private TournamentFormat format;
    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    private TournamentState tournamentState;
    private int maxTeams;
    private TournamentStateHandler stateHandler;

    public Tournament() {
        this.name = "Default";
        this.format = TournamentFormat.SINGLE_ELIM;
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.tournamentState = TournamentState.IDLE;
        this.maxTeams = 16;
        this.stateHandler = new IdleState();
    }

    // FUNCTIONS
    public int calculateRounds() {
        return this.format.calculateRounds(this.maxTeams);
    }

    public void generateBracket() {
        if (this.teams.size() < 2) {
            throw new IllegalStateException("Not enough teams to generate bracket");
        }

        if (this.teams.size() % 2 != 0 && this.format != TournamentFormat.ROUND_ROBIN) {
            throw new IllegalStateException("Teams must be even for non-round robin formats");
        }

        this.matches.clear();
        this.matches.addAll(this.format.generateBracket(this.teams));

        System.out.println("Bracket generated: " + this.matches.size() + " matches created");
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

    public void displayMatches() {
        System.out.println("\n--- CURRENT MATCHES ---");

        for (int i = 0; i < matches.size(); i++) {
            Match m = matches.get(i);

            System.out.println("Match " + (i + 1) + ": "
                    + m.getTeam1().getName() + " vs "
                    + m.getTeam2().getName()
                    + " | Status: " + m.getStatus());
        }
    }
  
    public void playRound(Scanner scanner) {
        ArrayList<Team> winners = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            Match m = matches.get(i);

            System.out.println("\nMatch " + (i + 1) + ": "
                    + m.getTeam1().getName() + " vs "
                    + m.getTeam2().getName());

            System.out.print("Enter score for " + m.getTeam1().getName() + ": ");
            int s1 = scanner.nextInt();

            System.out.print("Enter score for " + m.getTeam2().getName() + ": ");
            int s2 = scanner.nextInt();

            m.enterResults(s1, s2);

            // Determine winner
            Team winner = (s1 > s2) ? m.getTeam1() : m.getTeam2();
            winners.add(winner);
        }
      
        // Build next round
        buildNextRound(winners);
    }

    private void buildNextRound(ArrayList<Team> winners) {
        matches.clear();

        if (winners.size() == 1) {
            System.out.println( "Champion: " + winners.get(0).getName());
            return;
        }

        for (int i = 0; i < winners.size(); i += 2) {
            Match match = new Match(winners.get(i), winners.get(i + 1));
            matches.add(match);
        }

        System.out.println("Next round created with " + matches.size() + " matches.");
    }

    public void setTeamNameAtIndex(int index, String teamName) {
        teams.get(index).setName(teamName);
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

    public ArrayList<Match> getMatches() {
    return this.matches;
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
    
