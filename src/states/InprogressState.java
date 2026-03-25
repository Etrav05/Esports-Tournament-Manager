package src.states;

import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.models.Team;

public class InprogressState implements TournamentStateHandler {
    @Override
    public void validateTransition(TournamentState newState) {
        if (newState == TournamentState.UPDATE || newState == TournamentState.SETUP) {
            return; // allowed
        }

        if (newState == TournamentState.INPROGRESS) {
            throw new IllegalStateException("Cannot transition into same state: " + newState);
        }

        throw new IllegalStateException("Cannot transition from INPROGRESS to " + newState);
    }
    
    @Override
    public void addTeam(Tournament tournament, Team team) {
        throw new IllegalStateException("Cannot add teams in INPROGRESS, must be in SETUP");
    }

    @Override
    public void setFormat(Tournament tournament, TournamentFormat format) {
        throw new IllegalStateException("Cannot add teams in INPROGRESS, must be in SETUP");
    }
    
    @Override
    public void generateBracket(Tournament tournament) {
        throw new IllegalStateException("Cannot generate bracket during INPROGRESS");
    }
}
