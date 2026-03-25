package src.factories;

import src.core.Tournament;
import src.core.TournamentFormat;

public class TournamentFactory {
    
    void maxTeamValidation(int maxTeams) {
        if (maxTeams < 32) {
            throw new IllegalArgumentException(
                "Max teams must be less than 32. Entered: " + maxTeams
            );
        }
        
        if (maxTeams < 2 || (maxTeams & (maxTeams - 1)) != 0) {
            throw new IllegalArgumentException(
                "Max teams requires a power of 2 (e.g. 8, 16, 32). Entered: " + maxTeams
            );
        }
    }
    
    public Tournament createSingleElimination(String name, int maxTeams) {
        maxTeamValidation(maxTeams);
        
        Tournament tournament = new Tournament();
        
        tournament.setName(name);
        tournament.setFormat(TournamentFormat.SINGLE_ELIM);
        tournament.setMaxTeams(maxTeams);

        return tournament;
    }

    public Tournament createDoubleElimination(String name, int maxTeams) {
        maxTeamValidation(maxTeams);
        
        Tournament tournament = new Tournament();
        
        tournament.setName(name);
        tournament.setFormat(TournamentFormat.DOUBLE_ELIM);
        tournament.setMaxTeams(maxTeams);

        return tournament;
    }

    public Tournament createRoundRobin(String name, int maxTeams) {
        maxTeamValidation(maxTeams);
        
        Tournament tournament = new Tournament();
        
        tournament.setName(name);
        tournament.setFormat(TournamentFormat.ROUND_ROBIN);
        tournament.setMaxTeams(maxTeams);

        return tournament;
    }

    // GENERAL CONSTRUCTOR
    public Tournament createTournament(String name, TournamentFormat format, int maxTeams) {
        Tournament tournament = new Tournament();
        
        tournament.setName(name);
        tournament.setFormat(format);
        tournament.setMaxTeams(maxTeams);
        
        return tournament;
    }
}