package src.factories;

import src.core.Tournament;

public class TournamentFactory {
    public Tournament createTournament() {
        Tournament tournament = new Tournament();
        
        return tournament;
    }
}