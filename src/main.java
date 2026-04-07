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
    
    private static void displaySavedTournaments(ArrayList<Tournament> savedTournaments) {
        for(Tournament i : savedTournaments)
            i.basicDisplayTournament();
    }

    public static void main(String[] args) {
        ArrayList<Tournament> savedTournaments = new ArrayList<>();
        TournamentFactory tournamentFactory = new TournamentFactory();
        Scanner userIn = new Scanner(System.in);
        boolean running = true;

        while(running) {
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
            System.out.print("Selection: ");
            int selection = userIn.nextInt(); 
            userIn.nextLine(); // clear newline 

            switch (selection) {
                case 1 -> {
                    Tournament newTournament = setupTournament(tournamentFactory, userIn);
                    if (newTournament != null) {
                        for (int i = 0; i < teamAmount; i++) {
                            System.out.print("Enter team name: ");
                            String teamName = userIn.nextLine(); // clear newline

                            newTournament.addTeam(teamName);
                        }        
                        
                        try {
                            newTournament.generateBracket();

                            savedTournaments.add(newTournament);
                            System.out.println("Tournament created successfully.");
                            
                        } catch (Exception e) {
                            System.out.println("Could not generate bracket: " + e.getMessage());
                        }      
                    }
                }

                case 2 -> updateTournament(savedTournaments, userIn);
                
                case 3 -> displaySavedTournaments(savedTournaments);

                case 4 -> {
                    try {
                        System.out.println("\n--- LOAD TOURNAMENT ---");
                        System.out.print("Enter file path to load: ");
                        String loadPath = userIn.nextLine();

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
                        System.out.print("Enter tournament name to save: ");
                        String tournamentName = userIn.nextLine();

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
                                System.out.print("Enter output file path: ");
                                String writePath = userIn.nextLine();

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
                
                default -> System.out.println("Invalid Selection");
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
        System.out.print("Selection: ");
        int selection = userIn.nextInt();
        userIn.nextLine(); // clear newline

        System.out.print("Enter tournament name: ");
        String tournamentName = userIn.nextLine();

        System.out.print("Enter number of teams: ");
        teamAmount = userIn.nextInt();
        userIn.nextLine(); // clear newline

        switch (selection) {
            case 1 -> {
                return tournamentFactory.createRoundRobin(tournamentName, teamAmount);
            }

            case 2 -> {
                return tournamentFactory.createSingleElimination(tournamentName, teamAmount);
            }

            case 3 -> {
                return tournamentFactory.createDoubleElimination(tournamentName, teamAmount);
            }

            default -> {
                System.out.println("Invalid tournament type.");
                return null;
            }
        }

    }

    private static void updateTournament(ArrayList<Tournament> savedTournaments, Scanner userIn) {
        boolean editing = true;

        if (savedTournaments.isEmpty()) {
            System.out.println("\nNo tournaments available to edit.");
            return;
        }

        displaySavedTournaments(savedTournaments);
        System.out.print("\nSelect tournament name to edit: ");
        String tournamentName = userIn.nextLine();


        Tournament selectedTournament = null;
        for(Tournament i : savedTournaments) {
            if (i.getName().equals(tournamentName))
                selectedTournament = i;
        }

        if (selectedTournament == null) {
            System.out.println("\nUnable to find tournament: " + tournamentName);
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
            System.out.print("Selection: ");

            int choice = userIn.nextInt();
            userIn.nextLine(); // clear newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new tournament name: ");
                    String newName = userIn.nextLine();
                    selectedTournament.setName(newName);
                    System.out.println("Tournament name updated.");
                }

                case 2 -> {
                    try {
                        if (selectedTournament.getMatches().isEmpty()) {
                            System.out.println("No matches found. Generate matches first.");
                        } else {
                            selectedTournament.playNextMatch(userIn); 
                        }
                    } catch (Exception e) {
                        System.out.println("Could not play match: " + e.getMessage());
                        userIn.nextLine();
                    }
                }

                case 3 -> {
                    if (selectedTournament.getMatches().isEmpty()) {
                        System.out.println("No matches created yet.");
                    } else {
                        selectedTournament.displayMatches();
                    }
                }

                case 4 -> selectedTournament.displayResults();

                case 5 -> selectedTournament.displayStandings();

                case 6 -> {
                    try {
                        selectedTournament.getCommandHistory().undoLast();
                        System.out.println("Last result undone successfully.");
                    } catch (Exception e) {
                        System.out.println("Nothing to undo: " + e.getMessage());
                    }
                }

                case 7 -> editing = false;

                default -> System.out.println("Invalid selection.");
            }
        }
    }
}