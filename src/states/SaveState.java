package src.states;

import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.models.Team;

public class SaveState implements TournamentStateHandler {
    @Override
    public void validateTransition(TournamentState newState) {
        if (newState == TournamentState.SETUP || newState == TournamentState.UPDATE) {
            return; // allowed
        }

        if (newState == TournamentState.SAVE) {
            throw new IllegalStateException("Cannot transition into same state: " + newState);
        }

        throw new IllegalStateException("Cannot transition from SAVE to " + newState);
    }
    
    @Override
    public void addTeam(Tournament tournament, Team team) {
        throw new IllegalStateException("Cannot add teams in SAVE, must be in SETUP");
    }

    @Override
    public void setFormat(Tournament tournament, TournamentFormat format) {
        throw new IllegalStateException("Cannot add teams in SAVE, must be in SETUP");
    }
    
    @Override
    public void generateBracket(Tournament tournament) {
        throw new IllegalStateException("Cannot generate bracket during SAVE");
    }
}
