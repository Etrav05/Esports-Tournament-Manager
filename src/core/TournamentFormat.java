package src.core;

import java.util.ArrayList;
import java.util.Scanner;

import src.commands.EnterResultCommand;
import src.models.Match;
import src.models.Team;
import src.models.modelstates.MatchStatus;

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
                Match match = tournament.getMatches().get(i);

                System.out.println("\nMatch " + (i + 1) + ": "
                        + match.getTeam1().getName() + " vs "
                        + match.getTeam2().getName());

                System.out.print("Enter score for " + match.getTeam1().getName() + ": ");
                int s1 = scanner.nextInt();

                System.out.print("Enter score for " + match.getTeam2().getName() + ": ");
                int s2 = scanner.nextInt();

                tournament.getCommandHistory().executeCommand(new EnterResultCommand(match, s1, s2));

                Team winner = (s1 > s2) ? match.getTeam1() : match.getTeam2();
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
                tournament.transitionTo(TournamentState.COMPLETE);
                return;
            }

            for (int i = 0; i < winners.size() - 1; i += 2) {
                tournament.getMatches().add(new Match(winners.get(i), winners.get(i + 1)));
            }

            System.out.println("Next round created with " + tournament.getMatches().size() + " matches.");
        }

        @Override
        public void playNextMatch(Tournament tournament, Match match, Scanner scanner) {
            System.out.println("\nMatch: " + match.getTeam1().getName() + " vs " + match.getTeam2().getName());

            System.out.print("Enter score for " + match.getTeam1().getName() + ": ");
            int s1 = scanner.nextInt();

            System.out.print("Enter score for " + match.getTeam2().getName() + ": ");
            int s2 = scanner.nextInt();
            scanner.nextLine();

            tournament.getCommandHistory().executeCommand(new EnterResultCommand(match, s1, s2));

            boolean roundComplete = true;
            for (Match m : tournament.getMatches()) {
                if (m.getStatus() != MatchStatus.COMPLETE) {
                    roundComplete = false;
                    break;
                }
            }

            if (roundComplete) {
                ArrayList<Team> winners = new ArrayList<>();
                for (Match m : tournament.getMatches()) {
                    winners.add(m.getScoreTeam1() > m.getScoreTeam2() ? m.getTeam1() : m.getTeam2());
                }
                buildNextRound(tournament, winners);
            }
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
                Match match = tournament.getMatches().get(i);

                System.out.println("\nMatch " + (i + 1) + ": "
                        + match.getTeam1().getName() + " vs "
                        + match.getTeam2().getName());

                System.out.print("Enter score for " + match.getTeam1().getName() + ": ");
                int s1 = scanner.nextInt();

                System.out.print("Enter score for " + match.getTeam2().getName() + ": ");
                int s2 = scanner.nextInt();

                tournament.getCommandHistory().executeCommand(new EnterResultCommand(match, s1, s2));

                if (s1 > s2) {
                    winners.add(match.getTeam1());
                    losers.add(match.getTeam2());
                } else {
                    winners.add(match.getTeam2());
                    losers.add(match.getTeam1());
                }
            }

            buildNextRound(tournament, winners, losers);
        }

        @Override
        public void playNextMatch(Tournament tournament, Match match, Scanner scanner) {
            System.out.println("\nMatch: " + match.getTeam1().getName() + " vs " + match.getTeam2().getName());

            System.out.print("Enter score for " + match.getTeam1().getName() + ": ");
            int s1 = scanner.nextInt();

            System.out.print("Enter score for " + match.getTeam2().getName() + ": ");
            int s2 = scanner.nextInt();
            scanner.nextLine();

            tournament.getCommandHistory().executeCommand(new EnterResultCommand(match, s1, s2));

            boolean roundComplete = true;
            for (Match m : tournament.getMatches()) {
                if (m.getStatus() != MatchStatus.COMPLETE) {
                    roundComplete = false;
                    break;
                }
            }

            if (roundComplete) {
                ArrayList<Team> winners = new ArrayList<>();
                ArrayList<Team> losers = new ArrayList<>();
                for (Match m : tournament.getMatches()) {
                    if (m.getScoreTeam1() > m.getScoreTeam2()) {
                        winners.add(m.getTeam1());
                        losers.add(m.getTeam2());
                    } else {
                        winners.add(m.getTeam2());
                        losers.add(m.getTeam1());
                    }
                }
                buildNextRound(tournament, winners, losers);
            }
        }

        public void buildNextRound(Tournament tournament, ArrayList<Team> winners, ArrayList<Team> losers) {
            tournament.getMatchHistory().addAll(tournament.getMatches());
            tournament.getMatches().clear();

            System.out.println("\n--- WINNERS BRACKET ---");
            if (winners.size() == 1) {
                System.out.println("Tournament Champion: " + winners.get(0).getName());
                tournament.transitionTo(TournamentState.COMPLETE);
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
            if (tournament.getMatches().isEmpty()) {
                tournament.transitionTo(TournamentState.COMPLETE);

                Team team = tournament.getWinner();

                System.out.println("Tournament Champion: " + team.getName());
            }
            
            for (int i = 0; i < tournament.getMatches().size(); i++) {
                Match match = tournament.getMatches().get(i);

                System.out.println("\nMatch " + (i + 1) + ": "
                        + match.getTeam1().getName() + " vs "
                        + match.getTeam2().getName());

                System.out.print("Enter score for " + match.getTeam1().getName() + ": ");
                int s1 = scanner.nextInt();

                System.out.print("Enter score for " + match.getTeam2().getName() + ": ");
                int s2 = scanner.nextInt();

                tournament.getCommandHistory().executeCommand(new EnterResultCommand(match, s1, s2));
            }

            System.out.println("\nAll matches complete. Check standings for results.");
        }

        @Override
        public void playNextMatch(Tournament tournament, Match match, Scanner scanner) {
            System.out.println("\nMatch: " + match.getTeam1().getName() + " vs " + match.getTeam2().getName());

            System.out.print("Enter score for " + match.getTeam1().getName() + ": ");
            int s1 = scanner.nextInt();

            System.out.print("Enter score for " + match.getTeam2().getName() + ": ");
            int s2 = scanner.nextInt();
            scanner.nextLine();

            tournament.getCommandHistory().executeCommand(new EnterResultCommand(match, s1, s2));

            boolean allComplete = true;
            for (Match m : tournament.getMatches()) {
                if (m.getStatus() != MatchStatus.COMPLETE) {
                    allComplete = false;
                    break;
                }
            }

            if (allComplete) {
                System.out.println("\nAll matches complete. Check standings for results.");
                tournament.transitionTo(TournamentState.COMPLETE);
            }
        }

        @Override
        public void buildNextRound(Tournament tournament, ArrayList<Team> winners) {
            // Round robin generates all matches upfront, no next round needed again
        }
    };

    public abstract int calculateRounds(int teams);
    public abstract ArrayList<Match> generateBracket(ArrayList<Team> teams);
    public abstract void playRound(Tournament t, Scanner scanner);
    public abstract void playNextMatch(Tournament t, Match m, Scanner scanner);
    public abstract void buildNextRound(Tournament tournament, ArrayList<Team> winners);
}
