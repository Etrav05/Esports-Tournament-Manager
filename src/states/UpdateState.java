package src.states;

import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.models.Team;

public class UpdateState implements TournamentStateHandler {
    @Override
    public void validateTransition(TournamentState newState) {
        if (newState == TournamentState.INPROGRESS || newState == TournamentState.SAVE || newState == TournamentState.COMPLETE) {
            return; // allowed
        }

        if (newState == TournamentState.UPDATE) {
            throw new IllegalStateException("Cannot transition into same state: " + newState);
        }

        throw new IllegalStateException("Cannot transition from UPDATE to " + newState);
    }
    
    @Override
    public void addTeam(Tournament tournament, Team team) {
        throw new IllegalStateException("Cannot add teams in UPDATE, must be in SETUP");
    }

    @Override
    public void setFormat(Tournament tournament, TournamentFormat format) {
        throw new IllegalStateException("Cannot add teams in UPDATE, must be in SETUP");
    }
    
    @Override
    public void generateBracket(Tournament tournament) {
        throw new IllegalStateException("Cannot generate bracket during UPDATE");
    }
}
