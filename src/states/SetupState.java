package src.states;

import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.models.Team;

public class SetupState implements TournamentStateHandler {
    @Override
    public void validateTransition(TournamentState newState) {
        if (newState == TournamentState.INPROGRESS) {
            return; // allowed
        }

        if (newState == TournamentState.SETUP) {
            throw new IllegalStateException("Cannot transition into same state: " + newState);
        }

        throw new IllegalStateException("Cannot transition from SETUP to " + newState);
    }
    
    @Override
    public void addTeam(Tournament tournament, Team team) {
        tournament.getTeams().add(team); // allowed adding teams SETUP
    }

    @Override
    public void setFormat(Tournament tournament, TournamentFormat format) {
        tournament.setFormat(format); // allowed setting format in SETUP
    }
    
    @Override
    public void generateBracket(Tournament tournament) {
        throw new IllegalStateException("Cannot generate bracket during SETUP");
    }
}
