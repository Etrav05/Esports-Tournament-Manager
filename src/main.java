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
            System.out.println("--- TOURNAMENT MANAGER ---");
            System.out.println("Enter 1 to create a new tournament.");
            System.out.println("Enter 2 to edit an existing tournament.");
            System.out.println("Enter 3 to view all tournaments.");
            System.out.println("Enter 4 to load a tournament from file.");
            System.out.println("Enter 5 to save tournament results to file.");
            System.out.println("Enter 6 to exit.");
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
                        System.out.println("Enter file path to load: ");
                        String loadPath = userIn.nextLine();

                        Tournament loadedTournament = FileManager.loadTournamentFromFile(loadPath, tournamentFactory);
                        savedTournaments.add(loadedTournament);
                        System.out.println("Tournament loaded successfully.");

                    } catch (Exception e) {
                        System.out.println("Tournament failed to load.");
                    }
                }

                case 5 -> {
                    if (savedTournaments.isEmpty()) 
                        System.out.println("No tournament aviable to write.");
                    else {
                        displaySavedTournaments(savedTournaments);
                        System.out.println("Enter tournament name to save: ");
                        String tournamentName = userIn.nextLine();

                        Tournament selectedTournament = null;
                        for (Tournament t : savedTournaments) {
                            if (t.getName().equals(tournamentName)) 
                                selectedTournament = t;
                            break;
                        }
                        
                        if (selectedTournament == null) 
                            System.out.println("Tournament not found.");
                        else {
                            try {
                                System.out.println("Enter output file path: ");
                                String writePath = userIn.nextLine();

                                FileManager.saveResultsToFile(selectedTournament, writePath);
                                System.out.println("Tournament saved successfully.");
                            } catch (Exception e) {
                                System.out.println("Unable to write to file: " + e.getMessage());
                            }
                        }

                    }
                }
                
                case 6 -> {
                    running = false;
                    System.out.println("Shutting down...");
                }
                
                default -> System.out.println("Invalid Selection");
            }
        }

        userIn.close();
    }

    private static Tournament setupTournament(TournamentFactory tournamentFactory, Scanner userIn) {
        System.out.println("\n--- TOURNAMENT CREATION ---");
        System.out.println("Enter 1 for Round robin");
        System.out.println("Enter 2 for Single elimination");
        System.out.println("Enter 3 for Double elimination");
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
            System.out.println("\n--- EDIT TOURNAMENT ---");
            System.out.println("Currently editing: " + selectedTournament.getName());
            System.out.println("Enter 1 to change the name");
            System.out.println("Enter 2 to enter match results this round");
            System.out.println("Enter 3 to display all matches in the current round");
            System.out.println("Enter 4 to display results");
            System.out.println("Enter 5 to view standings");
            System.out.println("Enter 6 to go Back");
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
                            selectedTournament.playRound(userIn);
                        }
                    } catch (Exception e) {
                        System.out.println("Could not play round: " + e.getMessage());
                        userIn.nextLine(); // recover scanner if bad input happens
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

                case 6 -> editing = false;

                default -> System.out.println("Invalid selection.");
            }
        }
    }
}