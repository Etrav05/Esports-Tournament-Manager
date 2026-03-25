package src;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;
import src.core.Tournament;
import src.factories.TournamentFactory;

// Matthew Romano - March 25th, 2026 - Main implementation
// SINGLE_ELIM, DOUBLE_ELIM, ROUND_ROBIN

public class main {
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
            System.out.println("Enter 4 to exit.");
            System.out.print("Selection: ");
            int selection = userIn.nextInt(); 
            userIn.nextLine(); // clear newline 

            switch (selection) {
                case 1:
                    Tournament newTournament = setupTournament(tournamentFactory, userIn);
                    if (newTournament != null) {
                        savedTournaments.add(newTournament);
                        System.out.println("Tournament created successfully.");
                    }
                    try {
                        newTournament.matchSetup();
                    } catch (Exception e) {
                        System.out.println("Could not generate matches: " + e.getMessage());
                    }
                    break;
                case 2:
                    updateTournament(savedTournaments, userIn);
                    break;
                case 3:
                    displaySavedTournaments(savedTournaments);
                    break;
                case 4:
                    running = false;
                    System.out.println("Shutting down...");
                    break;
                default:
                    System.out.println("Invalid Selection");
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
        int teamAmount = userIn.nextInt();
        userIn.nextLine(); // clear newline

        switch (selection) {
            case 1:
                return tournamentFactory.createRoundRobin(tournamentName, teamAmount);
            case 2:
                return tournamentFactory.createSingleElimination(tournamentName, teamAmount);
            case 3:
                return tournamentFactory.createDoubleElimination(tournamentName, teamAmount);
            default:
                System.out.println("Invalid tournament type.");
                return null;
        }

    }

     private static void updateTournament(ArrayList<Tournament> savedTournaments, Scanner userIn) {
        boolean editing = true;

        if (savedTournaments.isEmpty()) {
            System.out.println("No tournaments available to edit.");
            return;
        }

        displaySavedTournaments(savedTournaments);
        System.out.print("\nSelect tournament name to edit: ");
        String tournamentName = userIn.nextLine();

        Tournament selectedTournament = new Tournament();
        for(Tournament i : savedTournaments) {
            if (i.getName().equals(tournamentName))
                selectedTournament = i;
        }

        if (selectedTournament.getName().equals("Default")) {
            System.out.print("\nUnable to find tournament with name: " + tournamentName);
            return;
        }

        while (editing) {
            System.out.println("\n--- EDIT TOURNAMENT ---");
            System.out.println("Currently editing: " + selectedTournament.getName());
            System.out.println("Enter 1 to change the name");
            System.out.println("Enter 2 to enter match results this round");
            System.out.println("Enter 3 to display all matches in the current round");
            System.out.println("Enter 4 to go Back");
            System.out.print("Selection: ");

            int choice = userIn.nextInt();
            userIn.nextLine(); // clear newline

            switch (choice) {
                case 1:
                    System.out.print("Enter new tournament name: ");
                    String newName = userIn.nextLine();
                    selectedTournament.setName(newName);
                    System.out.println("Tournament name updated.");
                    break;
                case 2:
                     try {
                        if (selectedTournament.getMatches().isEmpty()) {
                            System.out.println("No matches found. Generate matches first.");
                        } else {
                            selectedTournament.playRound(userIn);
                            userIn.nextLine(); // helps avoid scanner weirdness after round input
                        }
                    } catch (Exception e) {
                        System.out.println("Could not play round: " + e.getMessage());
                        userIn.nextLine(); // recover scanner if bad input happens
                    }
                    break;
                case 3:
                     if (selectedTournament.getMatches().isEmpty()) {
                        System.out.println("No matches created yet.");
                    } else {
                        selectedTournament.displayMatches();
                    }
                    break;
                case 4:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }


}