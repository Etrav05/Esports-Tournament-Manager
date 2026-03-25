package src.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import src.core.Tournament;
import src.core.TournamentFormat;
import src.factories.TournamentFactory;
import src.models.Match;
import src.models.Team;

// Matthew Romano - March 25th, 2026 - File reader and writer
// Implementation of the file reading and writing functions

public class FileManager {

    public static Tournament loadTournamentFromFile(String filePath, TournamentFactory factory) throws IOException {
        String tournamentName = "";
        TournamentFormat format = null;
        int maxTeams = 0;
        ArrayList<String> teamNames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean readingTeams = false; // needed when reading team names

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.equals("Teams=")) {
                    readingTeams = true;
                    continue;
                }

                if (readingTeams) {
                    teamNames.add(line);
                    continue;
                }

                // Check each field
                if (line.startsWith("TournamentName=")) {
                    tournamentName = line.substring("TournamentName=".length()).trim();
                } 
                else if (line.startsWith("Format=")) {
                    String formatText = line.substring("Format=".length()).trim();
                    format = TournamentFormat.valueOf(formatText);
                } 
                else if (line.startsWith("MaxTeams=")) {
                    String maxTeamsText = line.substring("MaxTeams=".length()).trim();
                    maxTeams = Integer.parseInt(maxTeamsText);
                }
            }
        }

        if (tournamentName.isEmpty()) {
            throw new IllegalArgumentException("Tournament name missing.");
        }

        if (format == null) {
            throw new IllegalArgumentException("Tournament format missing.");
        }

        if (maxTeams <= 0) {
            throw new IllegalArgumentException("Max teams must be greater than 0.");
        }

        Tournament tournament;

        // Create the tournament based on parsed type
        switch (format) {
            case ROUND_ROBIN:
                tournament = factory.createRoundRobin(tournamentName, maxTeams);
                break;
            case SINGLE_ELIM:
                tournament = factory.createSingleElimination(tournamentName, maxTeams);
                break;
            case DOUBLE_ELIM:
                tournament = factory.createDoubleElimination(tournamentName, maxTeams);
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }

        if (teamNames.size() > maxTeams) {
            throw new IllegalArgumentException("Too many teams in file. Max teams is " + maxTeams);
        }

        for (String teamName : teamNames) {
            tournament.addTeam(teamName);
        }

        tournament.generateBracket();
        return tournament;
    }

    public static void saveResultsToFile(Tournament tournament, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Tournament Name: " + tournament.getName());
            writer.newLine();

            writer.write("Format: " + tournament.getFormat());
            writer.newLine();

            writer.write("MaxTeams: " + tournament.getMaxTeams());
            writer.newLine();
            writer.newLine();

            writer.write("Teams: ");
            writer.newLine();
            for (Team team : tournament.getTeams()) {
                writer.write(" - " + team.getName());
                writer.newLine();
            }

            writer.newLine();
            writer.write("Results: ");
            writer.newLine();

            if (tournament.getMatchHistory().isEmpty()) {
                writer.write("No matches played yet.");
                writer.newLine();
            } else {
                for (int i = 0; i < tournament.getMatchHistory().size(); i++) {
                    Match match = tournament.getMatchHistory().get(i);

                    writer.write(
                        "Match " + (i + 1)
                        + ": " + match.getTeam1().getName()
                        + " vs " + match.getTeam2().getName()
                        + " | Status: " + match.getStatus()
                        + " | Score: " + match.getScoreTeam1() + "-" + match.getScoreTeam2()
                        + " | Result: " + match.getResult()
                    );
                    writer.newLine();
                }
            }
        }
    }
}
