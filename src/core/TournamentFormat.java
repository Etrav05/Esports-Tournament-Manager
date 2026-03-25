package src.core;

import java.util.ArrayList;
import java.util.Scanner;
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

        @Override
        public void playRound(Tournament tournament, Scanner scanner) {
            ArrayList<Team> winners = new ArrayList<>();

            for (int i = 0; i < tournament.getMatches().size(); i++) {
                Match m = tournament.getMatches().get(i);

                System.out.println("\nMatch " + (i + 1) + ": "
                        + m.getTeam1().getName() + " vs "
                        + m.getTeam2().getName());

                System.out.print("Enter score for " + m.getTeam1().getName() + ": ");
                int s1 = scanner.nextInt();

                System.out.print("Enter score for " + m.getTeam2().getName() + ": ");
                int s2 = scanner.nextInt();

                m.enterResults(s1, s2);

                Team winner = (s1 > s2) ? m.getTeam1() : m.getTeam2();
                winners.add(winner);
            }

            buildNextRound(tournament, winners);
        }

        @Override
        public void buildNextRound(Tournament tournament, ArrayList<Team> winners) {
            tournament.getMatchHistory().addAll(tournament.getMatches());
            tournament.getMatches().clear();

            if (winners.size() == 1) {
                System.out.println("Champion: " + winners.get(0).getName());
                return;
            }

            for (int i = 0; i < winners.size() - 1; i += 2) {
                tournament.getMatches().add(new Match(winners.get(i), winners.get(i + 1)));
            }

            System.out.println("Next round created with " + tournament.getMatches().size() + " matches.");
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

        @Override
        public void playRound(Tournament tournament, Scanner scanner) {
            ArrayList<Team> winners = new ArrayList<>();
            ArrayList<Team> losers = new ArrayList<>();

            for (int i = 0; i < tournament.getMatches().size(); i++) {
                Match m = tournament.getMatches().get(i);

                System.out.println("\nMatch " + (i + 1) + ": "
                        + m.getTeam1().getName() + " vs "
                        + m.getTeam2().getName());

                System.out.print("Enter score for " + m.getTeam1().getName() + ": ");
                int s1 = scanner.nextInt();

                System.out.print("Enter score for " + m.getTeam2().getName() + ": ");
                int s2 = scanner.nextInt();

                m.enterResults(s1, s2);

                if (s1 > s2) {
                    winners.add(m.getTeam1());
                    losers.add(m.getTeam2());
                } else {
                    winners.add(m.getTeam2());
                    losers.add(m.getTeam1());
                }
            }

            buildNextRound(tournament, winners, losers);
        }

        public void buildNextRound(Tournament tournament, ArrayList<Team> winners, ArrayList<Team> losers) {
            tournament.getMatchHistory().addAll(tournament.getMatches());
            tournament.getMatches().clear();

            System.out.println("\n--- WINNERS BRACKET ---");
            if (winners.size() == 1) {
                System.out.println("Tournament Champion: " + winners.get(0).getName());
                return;
            }

            // Winners matches
            for (int i = 0; i < winners.size() - 1; i += 2) {
                tournament.getMatches().add(new Match(winners.get(i), winners.get(i + 1)));
            }

            // Losers matches
            System.out.println("\n--- LOSERS BRACKET ---");
            for (int i = 0; i < losers.size() - 1; i += 2) {
                tournament.getMatches().add(new Match(losers.get(i), losers.get(i + 1)));
            }

            System.out.println("Next round created with " + tournament.getMatches().size() + " matches.");
        }

        @Override
        public void buildNextRound(Tournament tournament, ArrayList<Team> winners) {
            // Not used for double elim, we can just use the overloaded version with the list of losers for the losers bracket
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

        @Override
        public void playRound(Tournament tournament, Scanner scanner) {
            for (int i = 0; i < tournament.getMatches().size(); i++) {
                Match m = tournament.getMatches().get(i);

                System.out.println("\nMatch " + (i + 1) + ": "
                        + m.getTeam1().getName() + " vs "
                        + m.getTeam2().getName());

                System.out.print("Enter score for " + m.getTeam1().getName() + ": ");
                int s1 = scanner.nextInt();

                System.out.print("Enter score for " + m.getTeam2().getName() + ": ");
                int s2 = scanner.nextInt();

                m.enterResults(s1, s2);
            }

            System.out.println("\nAll matches complete. Check standings for results.");
        }

        @Override
        public void buildNextRound(Tournament tournament, ArrayList<Team> winners) {
            // Round robin generates all matches upfront, no next round needed again
        }
    };

    public abstract int calculateRounds(int teams);
    public abstract ArrayList<Match> generateBracket(ArrayList<Team> teams);
    public abstract void playRound(Tournament t, Scanner scanner);
    public abstract void buildNextRound(Tournament tournament, ArrayList<Team> winners);
}
