package src.states;

import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.models.Team;

public interface TournamentStateHandler {
    void validateTransition(TournamentState newState);
    
    void addTeam(Tournament tournament, Team team);
    
    void setFormat(Tournament tournament, TournamentFormat format);
    
    void generateBracket(Tournament tournament);
}
