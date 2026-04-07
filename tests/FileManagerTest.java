package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import src.core.Tournament;
import src.core.TournamentFormat;
import src.factories.TournamentFactory;
import src.io.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Mohamed Al-Husaini - File loading and saving tests for FileManager

public class FileManagerTest {

    private TournamentFactory factory;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        factory = new TournamentFactory();
    }

    // Helper: write content to a temp file and return the path
    private String writeTempFile(String content) throws IOException {
        File file = tempDir.resolve("tournament_test.txt").toFile();
        Files.writeString(file.toPath(), content);
        return file.getAbsolutePath();
    }

    // -------------------------------------------------------
    // LOAD TESTS
    // -------------------------------------------------------

    @Test
    void testLoadValidSingleElim() throws IOException {
        String path = writeTempFile(
            "TournamentName=Champions Cup\n" +
            "Format=SINGLE_ELIM\n" +
            "MaxTeams=4\n" +
            "Teams=\n" +
            "Lions\n" +
            "Tigers\n" +
            "Bears\n" +
            "Wolves\n"
        );

        Tournament t = FileManager.loadTournamentFromFile(path, factory);

        assertEquals("Champions Cup", t.getName());
        assertEquals(TournamentFormat.SINGLE_ELIM, t.getFormat());
        assertEquals(4, t.getMaxTeams());
        assertEquals(4, t.getTeams().size());
    }

    @Test
    void testLoadValidRoundRobin() throws IOException {
        String path = writeTempFile(
            "TournamentName=RR League\n" +
            "Format=ROUND_ROBIN\n" +
            "MaxTeams=4\n" +
            "Teams=\n" +
            "Alpha\n" +
            "Beta\n" +
            "Gamma\n" +
            "Delta\n"
        );

        Tournament t = FileManager.loadTournamentFromFile(path, factory);

        assertEquals("RR League", t.getName());
        assertEquals(TournamentFormat.ROUND_ROBIN, t.getFormat());
        assertEquals(4, t.getTeams().size());
    }

    @Test
    void testLoadValidDoubleElim() throws IOException {
        String path = writeTempFile(
            "TournamentName=Double Bracket\n" +
            "Format=DOUBLE_ELIM\n" +
            "MaxTeams=4\n" +
            "Teams=\n" +
            "Team1\n" +
            "Team2\n" +
            "Team3\n" +
            "Team4\n"
        );

        Tournament t = FileManager.loadTournamentFromFile(path, factory);

        assertEquals(TournamentFormat.DOUBLE_ELIM, t.getFormat());
    }

    @Test
    void testLoadCorrectTeamNames() throws IOException {
        String path = writeTempFile(
            "TournamentName=Name Test\n" +
            "Format=SINGLE_ELIM\n" +
            "MaxTeams=4\n" +
            "Teams=\n" +
            "Red Hawks\n" +
            "Blue Jays\n" +
            "Green Wolves\n" +
            "Gold Eagles\n"
        );

        Tournament t = FileManager.loadTournamentFromFile(path, factory);

        assertEquals("Red Hawks",   t.getTeams().get(0).getName());
        assertEquals("Blue Jays",   t.getTeams().get(1).getName());
        assertEquals("Green Wolves", t.getTeams().get(2).getName());
        assertEquals("Gold Eagles", t.getTeams().get(3).getName());
    }

    @Test
    void testLoadMissingTournamentName_throwsException() throws IOException {
        String path = writeTempFile(
            "Format=SINGLE_ELIM\n" +
            "MaxTeams=4\n" +
            "Teams=\n" +
            "Lions\n" +
            "Tigers\n" +
            "Bears\n" +
            "Wolves\n"
        );

        assertThrows(IllegalArgumentException.class,
            () -> FileManager.loadTournamentFromFile(path, factory));
    }

    @Test
    void testLoadMissingFormat_throwsException() throws IOException {
        String path = writeTempFile(
            "TournamentName=Test\n" +
            "MaxTeams=4\n" +
            "Teams=\n" +
            "Lions\n" +
            "Tigers\n" +
            "Bears\n" +
            "Wolves\n"
        );

        assertThrows(IllegalArgumentException.class,
            () -> FileManager.loadTournamentFromFile(path, factory));
    }

    @Test
    void testLoadInvalidFormatString_throwsException() throws IOException {
        String path = writeTempFile(
            "TournamentName=Test\n" +
            "Format=NOT_A_FORMAT\n" +
            "MaxTeams=4\n" +
            "Teams=\n" +
            "Lions\n" +
            "Tigers\n" +
            "Bears\n" +
            "Wolves\n"
        );

        assertThrows(Exception.class,
            () -> FileManager.loadTournamentFromFile(path, factory));
    }

    @Test
    void testLoadZeroMaxTeams_throwsException() throws IOException {
        String path = writeTempFile(
            "TournamentName=Test\n" +
            "Format=SINGLE_ELIM\n" +
            "MaxTeams=0\n" +
            "Teams=\n"
        );

        assertThrows(IllegalArgumentException.class,
            () -> FileManager.loadTournamentFromFile(path, factory));
    }

    @Test
    void testLoadTooManyTeams_throwsException() throws IOException {
        // MaxTeams=2 but 4 teams listed
        String path = writeTempFile(
            "TournamentName=Overflow Test\n" +
            "Format=SINGLE_ELIM\n" +
            "MaxTeams=2\n" +
            "Teams=\n" +
            "Alpha\n" +
            "Beta\n" +
            "Gamma\n" +
            "Delta\n"
        );

        assertThrows(IllegalArgumentException.class,
            () -> FileManager.loadTournamentFromFile(path, factory));
    }

    @Test
    void testLoadNonExistentFile_throwsIOException() {
        assertThrows(IOException.class,
            () -> FileManager.loadTournamentFromFile("/no/such/file.txt", factory));
    }

    @Test
    void testLoadFileWithBlankLines_parsesCorrectly() throws IOException {
        // Extra blank lines should be ignored by FileManager
        String path = writeTempFile(
            "\n" +
            "TournamentName=Blank Lines Test\n" +
            "\n" +
            "Format=SINGLE_ELIM\n" +
            "MaxTeams=4\n" +
            "\n" +
            "Teams=\n" +
            "A\n" +
            "B\n" +
            "C\n" +
            "D\n"
        );

        Tournament t = FileManager.loadTournamentFromFile(path, factory);
        assertEquals("Blank Lines Test", t.getName());
        assertEquals(4, t.getTeams().size());
    }

    // -------------------------------------------------------
    // SAVE TESTS
    // -------------------------------------------------------

    @Test
    void testSaveCreatesFile() throws IOException {
        Tournament t = factory.createSingleElimination("Save Test", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");

        File outFile = tempDir.resolve("output.txt").toFile();
        FileManager.saveResultsToFile(t, outFile.getAbsolutePath());

        assertTrue(outFile.exists(), "Output file should be created");
        assertTrue(outFile.length() > 0, "Output file should not be empty");
    }

    @Test
    void testSaveContainsTournamentName() throws IOException {
        Tournament t = factory.createSingleElimination("Grand Finals", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");

        File outFile = tempDir.resolve("output.txt").toFile();
        FileManager.saveResultsToFile(t, outFile.getAbsolutePath());

        String content = Files.readString(outFile.toPath());
        assertTrue(content.contains("Grand Finals"), "File should include tournament name");
    }

    @Test
    void testSaveContainsFormat() throws IOException {
        Tournament t = factory.createRoundRobin("Format Check", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");

        File outFile = tempDir.resolve("output.txt").toFile();
        FileManager.saveResultsToFile(t, outFile.getAbsolutePath());

        String content = Files.readString(outFile.toPath());
        assertTrue(content.contains("ROUND_ROBIN"), "File should include tournament format");
    }

    @Test
    void testSaveContainsTeamNames() throws IOException {
        Tournament t = factory.createSingleElimination("Teams Test", 4);
        t.addTeam("Lions");
        t.addTeam("Tigers");
        t.addTeam("Bears");
        t.addTeam("Wolves");

        File outFile = tempDir.resolve("output.txt").toFile();
        FileManager.saveResultsToFile(t, outFile.getAbsolutePath());

        String content = Files.readString(outFile.toPath());
        assertTrue(content.contains("Lions"),  "File should contain Lions");
        assertTrue(content.contains("Tigers"), "File should contain Tigers");
        assertTrue(content.contains("Bears"),  "File should contain Bears");
        assertTrue(content.contains("Wolves"), "File should contain Wolves");
    }

    @Test
    void testSaveEmptyMatchHistory_showsNoMatchesPlayed() throws IOException {
        Tournament t = factory.createRoundRobin("No Matches", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");

        File outFile = tempDir.resolve("output.txt").toFile();
        FileManager.saveResultsToFile(t, outFile.getAbsolutePath());

        String content = Files.readString(outFile.toPath());
        assertTrue(content.contains("No matches played yet."),
            "File should say no matches played when history is empty");
    }

    @Test
    void testSaveToInvalidPath_throwsIOException() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");

        assertThrows(IOException.class,
            () -> FileManager.saveResultsToFile(t, "/no/such/directory/output.txt"));
    }
}
