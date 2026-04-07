package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.commands.CommandHistory;
import src.commands.EnterResultCommand;
import src.models.Match;
import src.models.Stats;
import src.models.Team;
import src.models.modelstates.MatchStatus;

import static org.junit.jupiter.api.Assertions.*;

// Mohamed Al-Husaini - Unit tests for CommandHistory (undo/redo pattern)

public class CommandHistoryTest {

    private CommandHistory history;
    private Match match;
    private Team team1;
    private Team team2;

    @BeforeEach
    void setUp() {
        history = new CommandHistory();

        team1 = new Team();
        team1.setName("Lions");
        team1.setStats(new Stats());

        team2 = new Team();
        team2.setName("Tigers");
        team2.setStats(new Stats());

        match = new Match(team1, team2);
    }

    // -------------------------------------------------------
    // hasHistory tests
    // -------------------------------------------------------

    @Test
    void testHasHistory_initiallyFalse() {
        assertFalse(history.hasHistory());
    }

    @Test
    void testHasHistory_afterExecute_isTrue() {
        history.executeCommand(new EnterResultCommand(match, 3, 1));
        assertTrue(history.hasHistory());
    }

    @Test
    void testHasHistory_afterExecuteThenUndo_isFalse() {
        history.executeCommand(new EnterResultCommand(match, 3, 1));
        history.undoLast();
        assertFalse(history.hasHistory());
    }

    // -------------------------------------------------------
    // executeCommand tests
    // -------------------------------------------------------

    @Test
    void testExecuteCommand_matchScoresAreSet() {
        history.executeCommand(new EnterResultCommand(match, 3, 1));
        assertEquals(3, match.getScoreTeam1());
        assertEquals(1, match.getScoreTeam2());
    }

    @Test
    void testExecuteCommand_matchStatusIsComplete() {
        history.executeCommand(new EnterResultCommand(match, 3, 1));
        assertEquals(MatchStatus.COMPLETE, match.getStatus());
    }

    // -------------------------------------------------------
    // undoLast tests
    // -------------------------------------------------------

    @Test
    void testUndoLast_emptyHistory_throwsException() {
        assertThrows(IllegalStateException.class, () -> history.undoLast());
    }

    @Test
    void testUndoLast_restoresMatchStatus() {
        history.executeCommand(new EnterResultCommand(match, 3, 1));
        history.undoLast();
        assertEquals(MatchStatus.PENDING, match.getStatus(),
            "After undo, match status should return to PENDING");
    }

    @Test
    void testUndoLast_removesCommandFromHistory() {
        history.executeCommand(new EnterResultCommand(match, 3, 1));
        history.undoLast();
        assertFalse(history.hasHistory());
    }

    // -------------------------------------------------------
    // Multiple commands test
    // -------------------------------------------------------

    @Test
    void testMultipleCommands_undoIsLIFO() {
        Team t3 = new Team();
        t3.setName("Bears");
        t3.setStats(new Stats());

        Team t4 = new Team();
        t4.setName("Wolves");
        t4.setStats(new Stats());

        Match match2 = new Match(t3, t4);

        history.executeCommand(new EnterResultCommand(match, 3, 1));   // first
        history.executeCommand(new EnterResultCommand(match2, 2, 5));  // second

        // Undo second command first (LIFO)
        history.undoLast();
        assertEquals(MatchStatus.PENDING, match2.getStatus(),
            "Second match should be undone first");
        assertEquals(MatchStatus.COMPLETE, match.getStatus(),
            "First match should still be complete");

        // Undo first command
        history.undoLast();
        assertEquals(MatchStatus.PENDING, match.getStatus(),
            "First match should now be undone");
        assertFalse(history.hasHistory());
    }
}
