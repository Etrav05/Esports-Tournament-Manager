package src.states;

import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.models.Team;

public class IdleState implements TournamentStateHandler {
    @Override
    public void validateTransition(TournamentState newState) {
        if (newState == TournamentState.SETUP || newState == TournamentState.INPROGRESS) {
            return; // allowed
        }

        if (newState == TournamentState.IDLE) {
            throw new IllegalStateException("Cannot transition into same state: " + newState);
        }

        throw new IllegalStateException("Cannot transition from IDLE to " + newState);
    }

    @Override
    public void addTeam(Tournament tournament, Team team) {
        throw new IllegalStateException("Cannot add teams in IDLE, must be in SETUP");
    }

    @Override
    public void setFormat(Tournament tournament, TournamentFormat format) {
        throw new IllegalStateException("Cannot set format in IDLE, must be in SETUP");
    }
    
    @Override
    public void generateBracket(Tournament tournament) {
        throw new IllegalStateException("Cannot generate bracket during IDLE");
    }
}
