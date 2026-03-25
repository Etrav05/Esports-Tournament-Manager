package src.core;

import java.util.ArrayList;
import src.models.Match;
import src.models.Team;

public enum TournamentFormat {
    SINGLE_ELIM {
        @Override
        public int calculateRounds(int teams) {
            return (int) (Math.log(teams) / Math.log(2));
        }

        @Override
          public ArrayList<Match> generateBracket(ArrayList<Team> teams) {
            ArrayList<Match> matches = new ArrayList<>();

            for (int i = 0; i < teams.size() - 1; i += 2) {
                matches.add(new Match(teams.get(i), teams.get(i + 1)));
            }

            return matches;
        }
    },

    DOUBLE_ELIM {
        @Override
        public int calculateRounds(int teams) {
            return (int) (Math.log(teams) / Math.log(2)) * 2;
        }

        @Override
        public ArrayList<Match> generateBracket(ArrayList<Team> teams) {
            ArrayList<Match> matches = new ArrayList<>();

            for (int i = 0; i < teams.size() - 1; i += 2) {
                matches.add(new Match(teams.get(i), teams.get(i + 1)));
            }

            return matches;
        }

    },
    
    ROUND_ROBIN {
        @Override
        public int calculateRounds(int teams) {
            return teams - 1;
        }

        @Override
        public ArrayList<Match> generateBracket(ArrayList<Team> teams) {
            ArrayList<Match> matches = new ArrayList<>();

            for (int i = 0; i < teams.size(); i++) {
                for (int j = i + 1; j < teams.size(); j++) {
                    matches.add(new Match(teams.get(i), teams.get(j)));
                }
            }

            return matches;
        }
    };

    public abstract int calculateRounds(int teams);
    public abstract ArrayList<Match> generateBracket(ArrayList<Team> teams);
}
