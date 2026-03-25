package src;
import java.lang.*;
import java.util.ArrayList;
import java.util.Scanner;
import src.core.Tournament;
import src.factories.TournamentFactory;

// Matthew Romano - March 25th, 2026 - Main implementation
// SINGLE_ELIM, DOUBLE_ELIM, ROUND_ROBIN

// TODO: Cretae an override for the toString method in Tournament
public class main {
    private static void displaySavedTournaments(ArrayList<Tournament> savedTournaments) {
        for(Tournament i : savedTournaments)
            i.basicDisplayTournament();
    }

    public static void main(String[] args) {
        ArrayList<Tournament> savedTournaments = new ArrayList<>();
        TournamentFactory tournamentFactory = new TournamentFactory();
        Scanner userIn = new Scanner(System.in);
        String tournamentName = "";
        int teamAmount = 0;
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
        userIn.nextLine(); // clear newline

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
            System.out.println("Enter 2 to View bracket");
            System.out.println("Enter 3 to Fill in rounds");
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
                    System.out.println("Name: " + selectedTournament.getName());
                    System.out.println("Format: " + selectedTournament.getFormat());
                    System.out.println("Max Teams: " + selectedTournament.getMaxTeams());
                    System.out.println("Rounds: " + selectedTournament.getRounds());
                    System.out.println("State: " + selectedTournament.getTournamentState());
                    break;

                case 3:
                    fillRounds(selectedTournament, userIn);
                    break;

                case 4:
                    editing = false;
                    break;

                default:
                    System.out.println("Invalid selection.");
            }
        }
    }
    
    // Fills out and saves the match results of each round
    private static void fillRounds(Tournament tournamentToEdit, Scanner userIn) {
        int rounds = tournamentToEdit.getRounds();

        System.out.println("\n--- FILL TOURNAMENT ROUNDS ---");
        System.out.println("Tournament: " + tournamentToEdit.getName());
        System.out.println("Total rounds: " + rounds);

        int teamsRemaining = tournamentToEdit.getMaxTeams();

        for (int round = 1; round <= rounds; round++) {
            int matches = teamsRemaining / 2;

            System.out.println("\nROUND " + round);
            for (int match = 1; match <= matches; match++) {
                System.out.print("Enter winner for Match " + match + ": ");
                String winner = userIn.nextLine();

                // For now just print it.
                // Later you should store this in a Round/Match class.
                System.out.println("Winner recorded: " + winner);
            }

            teamsRemaining /= 2;
        }

        System.out.println("All rounds entered.");
    }

}