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
        // for(Tournament i : savedTournaments)
            // System.out.print(i.toString());
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
                    // updateTournament();
                    break;
                case 3:
                    // displaySavedTournaments(savedTournaments);
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
        System.out.println("Enter 1 for Round robin,");
        System.out.println("Enter 2 for Single elimination,");
        System.out.println("Enter 3 for Double elimination,");
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

     private static void editTournament(ArrayList<Tournament> savedTournaments, Scanner userIn) {
        boolean editing = true;

        if (savedTournaments.isEmpty()) {
            System.out.println("No tournaments available to edit.");
            return;
        }

        displaySavedTournaments(savedTournaments);
        System.out.print("\nSelect tournament number to edit: ");
        int index = userIn.nextInt() - 1;
        userIn.nextLine(); // clear newline

        if (index < 0 || index >= savedTournaments.size()) {
            System.out.println("Invalid tournament selection.");
            return;
        }
        Tournament selectedTournament = savedTournaments.get(index);

        while (editing) {
            System.out.println("\n--- EDIT TOURNAMENT ---");
            System.out.println("Currently editing: " + selectedTournament.getName());
            System.out.println("1. Change name");
            System.out.println("2. Change max teams");
            System.out.println("3. View details");
            System.out.println("4. Fill in rounds");
            System.out.println("5. Back");
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
                    System.out.print("Enter new max teams: ");
                    int newMax = userIn.nextInt();
                    userIn.nextLine();
                    selectedTournament.setMaxTeams(newMax);
                    System.out.println("Max teams updated.");
                    break;

                case 3:
                    System.out.println("Name: " + selectedTournament.getName());
                    System.out.println("Format: " + selectedTournament.getFormat());
                    System.out.println("Max Teams: " + selectedTournament.getMaxTeams());
                    System.out.println("Rounds: " + selectedTournament.getRounds());
                    System.out.println("State: " + selectedTournament.getTournamentState());
                    break;

                case 4:
                    fillRounds(selectedTournament, userIn);
                    break;

                case 5:
                    editing = false;
                    break;

                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

}