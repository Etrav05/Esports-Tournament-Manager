package src;
import java.util.ArrayList;
import java.util.Scanner;

import src.core.Tournament;
import src.factories.TournamentFactory;
import src.io.FileManager;

// Matthew Romano - March 25th, 2026 - Main implementation
// SINGLE_ELIM, DOUBLE_ELIM, ROUND_ROBIN

// FILE STRUCTURE WHEN READING
// TournamentName=March Madness
// Format=SINGLE_ELIM
// MaxTeams=4
// Teams=
// Lions
// Tigers
// Bears
// Wolves

public class main {
    public static int teamAmount = 0;

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid input. Please enter a whole number.");
            }
        }
    }

    private static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("[ERROR] Input cannot be empty. Please try again.");
        }
    }

    private static void displaySavedTournaments(ArrayList<Tournament> savedTournaments) {
        for (Tournament i : savedTournaments)
            i.basicDisplayTournament();
    }

    public static void main(String[] args) {
        ArrayList<Tournament> savedTournaments = new ArrayList<>();
        TournamentFactory tournamentFactory = new TournamentFactory();
        Scanner userIn = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n+----------------------------------+");
            System.out.println("|      ESPORTS TOURNAMENT MANAGER  |");
            System.out.println("+----------------------------------+");
            System.out.println("|  1. Create new tournament        |");
            System.out.println("|  2. Edit existing tournament     |");
            System.out.println("|  3. View all tournaments         |");
            System.out.println("|  4. Load tournament from file    |");
            System.out.println("|  5. Save tournament to file      |");
            System.out.println("|  6. Exit                         |");
            System.out.println("+----------------------------------+");
            int selection = readInt(userIn, "Selection: ");

            switch (selection) {
                case 1 -> {
                    Tournament newTournament = setupTournament(tournamentFactory, userIn);
                    if (newTournament != null) {
                        for (int i = 0; i < teamAmount; i++) {
                            String teamName = readString(userIn, "Enter team name: ");
                            newTournament.addTeam(teamName);
                        }

                        try {
                            newTournament.generateBracket();
                            savedTournaments.add(newTournament);
                            System.out.println("Tournament created successfully.");
                        } catch (Exception e) {
                            System.out.println("[ERROR] Could not generate bracket: " + e.getMessage());
                        }
                    }
                }

                case 2 -> updateTournament(savedTournaments, userIn);

                case 3 -> {
                    if (savedTournaments.isEmpty())
                        System.out.println("[ERROR] No tournaments to display.");
                    else
                        displaySavedTournaments(savedTournaments);
                }

                case 4 -> {
                    try {
                        System.out.println("\n--- LOAD TOURNAMENT ---");
                        String loadPath = readString(userIn, "Enter file path to load: ");

                        Tournament loadedTournament = FileManager.loadTournamentFromFile(loadPath, tournamentFactory);
                        savedTournaments.add(loadedTournament);
                        System.out.println("[OK] Tournament loaded successfully: " + loadedTournament.getName());

                    } catch (Exception e) {
                        System.out.println("[ERROR] Tournament failed to load: " + e.getMessage());
                    }
                }

                case 5 -> {
                    if (savedTournaments.isEmpty())
                        System.out.println("[ERROR] No tournaments available to save.");
                    else {
                        System.out.println("\n--- SAVE TOURNAMENT ---");
                        displaySavedTournaments(savedTournaments);
                        String tournamentName = readString(userIn, "Enter tournament name to save: ");

                        Tournament selectedTournament = null;
                        for (Tournament t : savedTournaments) {
                            if (t.getName().equals(tournamentName)) {
                                selectedTournament = t;
                                break;
                            }
                        }

                        if (selectedTournament == null)
                            System.out.println("[ERROR] Tournament not found: " + tournamentName);
                        else {
                            try {
                                String writePath = readString(userIn, "Enter output file path: ");
                                FileManager.saveResultsToFile(selectedTournament, writePath);
                                System.out.println("[OK] Tournament saved successfully to: " + writePath);
                            } catch (Exception e) {
                                System.out.println("[ERROR] Unable to write to file: " + e.getMessage());
                            }
                        }
                    }
                }

                case 6 -> {
                    running = false;
                    System.out.println("\nShutting down. Goodbye!");
                }

                default -> System.out.println("[ERROR] Invalid selection. Please choose 1-6.");
            }
        }

        userIn.close();
    }

    private static Tournament setupTournament(TournamentFactory tournamentFactory, Scanner userIn) {
        System.out.println("\n+----------------------------------+");
        System.out.println("|       CREATE NEW TOURNAMENT      |");
        System.out.println("+----------------------------------+");
        System.out.println("|  1. Round Robin                  |");
        System.out.println("|  2. Single Elimination           |");
        System.out.println("|  3. Double Elimination           |");
        System.out.println("+----------------------------------+");
        int selection = readInt(userIn, "Selection: ");

        String tournamentName = readString(userIn, "Enter tournament name: ");

        teamAmount = readInt(userIn, "Enter number of teams: ");
        while (teamAmount < 2) {
            System.out.println("[ERROR] Must have at least 2 teams.");
            teamAmount = readInt(userIn, "Enter number of teams: ");
        }

        try {
            switch (selection) {
                case 1 -> { return tournamentFactory.createRoundRobin(tournamentName, teamAmount); }
                case 2 -> { return tournamentFactory.createSingleElimination(tournamentName, teamAmount); }
                case 3 -> { return tournamentFactory.createDoubleElimination(tournamentName, teamAmount); }
                default -> {
                    System.out.println("[ERROR] Invalid tournament type.");
                    return null;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] Could not create tournament: " + e.getMessage());
            return null;
        }
    }

    private static void updateTournament(ArrayList<Tournament> savedTournaments, Scanner userIn) {
        boolean editing = true;

        if (savedTournaments.isEmpty()) {
            System.out.println("\nNo tournaments available to edit.");
            return;
        }

        displaySavedTournaments(savedTournaments);
        String tournamentName = readString(userIn, "\nSelect tournament name to edit: ");

        Tournament selectedTournament = null;
        for (Tournament i : savedTournaments) {
            if (i.getName().equals(tournamentName))
                selectedTournament = i;
        }

        if (selectedTournament == null) {
            System.out.println("\n[ERROR] Unable to find tournament: " + tournamentName);
            return;
        }

        while (editing) {
            System.out.println("\n+----------------------------------+");
            System.out.println("|          EDIT TOURNAMENT         |");
            System.out.printf( "|  Editing: %-22s|\n", selectedTournament.getName());
            System.out.println("+----------------------------------+");
            System.out.println("|  1. Rename tournament            |");
            System.out.println("|  2. Enter match results          |");
            System.out.println("|  3. View current matches         |");
            System.out.println("|  4. View results                 |");
            System.out.println("|  5. View standings               |");
            System.out.println("|  6. Undo last result             |");
            System.out.println("|  7. Back                         |");
            System.out.println("+----------------------------------+");
            int choice = readInt(userIn, "Selection: ");

            switch (choice) {
                case 1 -> {
                    String newName = readString(userIn, "Enter new tournament name: ");
                    selectedTournament.setName(newName);
                    System.out.println("Tournament name updated.");
                }

                case 2 -> {
                    try {
                        if (selectedTournament.getMatches().isEmpty()) {
                            System.out.println("[ERROR] No matches found. Generate matches first.");
                        } else {
                            selectedTournament.playNextMatch(userIn);
                        }
                    } catch (Exception e) {
                        System.out.println("[ERROR] Could not play match: " + e.getMessage());
                    }
                }

                case 3 -> {
                    if (selectedTournament.getMatches().isEmpty())
                        System.out.println("[ERROR] No matches created yet.");
                    else
                        selectedTournament.displayMatches();
                }

                case 4 -> selectedTournament.displayResults();

                case 5 -> selectedTournament.displayStandings();

                case 6 -> {
                    try {
                        selectedTournament.getCommandHistory().undoLast();
                        selectedTournament.refreshStandings();
                        System.out.println("Last result undone successfully.");
                    } catch (Exception e) {
                        System.out.println("[ERROR] Nothing to undo: " + e.getMessage());
                    }
                }

                case 7 -> editing = false;

                default -> System.out.println("[ERROR] Invalid selection. Please choose 1-7.");
            }
        }
    }
}