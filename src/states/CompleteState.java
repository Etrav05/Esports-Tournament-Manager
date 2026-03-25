package src.states;

import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.models.Team;

public class CompleteState implements TournamentStateHandler {
    @Override
    public void validateTransition(TournamentState newState) {
        if (newState == TournamentState.SAVE) {
            return; // allowed
        }

        if (newState == TournamentState.COMPLETE) {
            throw new IllegalStateException("Cannot transition into same state: " + newState);
        }

        throw new IllegalStateException("Cannot transition from COMPLETE to " + newState);
    }
    
    @Override
    public void addTeam(Tournament tournament, Team team) {
        throw new IllegalStateException("Cannot add teams in COMPLETE, must be in SETUP");
    }

    @Override
    public void setFormat(Tournament tournament, TournamentFormat format) {
        throw new IllegalStateException("Cannot set format in COMPLETE, must be in SETUP");
    }
    
    @Override
    public void generateBracket(Tournament tournament) {
        throw new IllegalStateException("Cannot generate bracket during COMPLETE");
    }
}
