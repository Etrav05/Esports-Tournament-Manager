import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.models.Match;
import src.models.Stats;
import src.models.Team;
import src.models.modelstates.MatchStatus;

import static org.junit.jupiter.api.Assertions.*;

// Mohamed Al-Husaini - Unit tests for Match class

public class MatchTest {

    private Team team1;
    private Team team2;

    @BeforeEach
    void setUp() {
        team1 = new Team();
        team1.setName("Lions");
        team1.setStats(new Stats());

        team2 = new Team();
        team2.setName("Tigers");
        team2.setStats(new Stats());
    }

    // -------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------

    @Test
    void testConstructor_validTeams_setsFields() {
        Match match = new Match(team1, team2);
        assertEquals(team1, match.getTeam1());
        assertEquals(team2, match.getTeam2());
        assertEquals(MatchStatus.PENDING, match.getStatus());
    }

    @Test
    void testConstructor_nullTeam1_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Match(null, team2));
    }

    @Test
    void testConstructor_nullTeam2_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Match(team1, null));
    }

    @Test
    void testConstructor_sameTeamTwice_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Match(team1, team1));
    }

    // -------------------------------------------------------
    // enterResults tests
    // -------------------------------------------------------

    @Test
    void testEnterResults_team1Wins_statusIsComplete() {
        Match match = new Match(team1, team2);
        match.enterResults(3, 1);
        assertEquals(MatchStatus.COMPLETE, match.getStatus());
    }

    @Test
    void testEnterResults_team1Wins_scoresStoredCorrectly() {
        Match match = new Match(team1, team2);
        match.enterResults(5, 2);
        assertEquals(5, match.getScoreTeam1());
        assertEquals(2, match.getScoreTeam2());
    }

    @Test
    void testEnterResults_team1Wins_resultStringContainsWinner() {
        Match match = new Match(team1, team2);
        match.enterResults(3, 1);
        assertTrue(match.getResult().contains("Lions"),
            "Result string should name the winning team");
        assertTrue(match.getResult().contains("def."),
            "Result string should include 'def.'");
    }

    @Test
    void testEnterResults_team2Wins_resultStringContainsWinner() {
        Match match = new Match(team1, team2);
        match.enterResults(1, 4);
        assertTrue(match.getResult().contains("Tigers"),
            "Result string should name the winning team");
    }

    @Test
    void testEnterResults_tieScore_throwsException() {
        Match match = new Match(team1, team2);
        assertThrows(IllegalArgumentException.class, () -> match.enterResults(3, 3));
    }

    // -------------------------------------------------------
    // Stats update tests
    // -------------------------------------------------------

    @Test
    void testEnterResults_team1Wins_updatesWinsAndLosses() {
        Match match = new Match(team1, team2);
        match.enterResults(5, 2);

        assertEquals(1, team1.getStats().getWins(),   "team1 should have 1 win");
        assertEquals(0, team1.getStats().getLosses(), "team1 should have 0 losses");
        assertEquals(0, team2.getStats().getWins(),   "team2 should have 0 wins");
        assertEquals(1, team2.getStats().getLosses(), "team2 should have 1 loss");
    }

    @Test
    void testEnterResults_team2Wins_updatesWinsAndLosses() {
        Match match = new Match(team1, team2);
        match.enterResults(1, 6);

        assertEquals(0, team1.getStats().getWins(),   "team1 should have 0 wins");
        assertEquals(1, team1.getStats().getLosses(), "team1 should have 1 loss");
        assertEquals(1, team2.getStats().getWins(),   "team2 should have 1 win");
        assertEquals(0, team2.getStats().getLosses(), "team2 should have 0 losses");
    }

    @Test
    void testEnterResults_updatesPointsFor() {
        Match match = new Match(team1, team2);
        match.enterResults(5, 2);

        assertEquals(5, team1.getStats().getPointsFor());
        assertEquals(2, team2.getStats().getPointsFor());
    }

    @Test
    void testEnterResults_updatesPointsAgainst() {
        Match match = new Match(team1, team2);
        match.enterResults(5, 2);

        assertEquals(2, team1.getStats().getPointsAgainst());
        assertEquals(5, team2.getStats().getPointsAgainst());
    }

    // -------------------------------------------------------
    // fillMatchResult tests
    // -------------------------------------------------------

    @Test
    void testFillMatchResult_beforeComplete_throwsException() {
        Match match = new Match(team1, team2);
        assertThrows(IllegalStateException.class, match::fillMatchResult);
    }

    // -------------------------------------------------------
    // reverseRecords tests
    // -------------------------------------------------------

    @Test
    void testReverseRecords_pointsAreSubtracted() {
        // Both team1 and team2 need baseline pointsAgainst > the reversed amount,
        // otherwise updateRatio divides by zero after the reversal.
        Team team3 = new Team(); team3.setName("Bears"); team3.setStats(new Stats());

        // Warmup for team1: gives it pAgainst=2 before the reversed match adds 1 more
        Match warmup1 = new Match(team1, team3);
        warmup1.enterResults(3, 2); // team1 pAgainst=2, team3 pAgainst=3

        // Warmup for team2: gives it pAgainst=6 before the reversed match adds 5 more
        Match warmup2 = new Match(team2, team3);
        warmup2.enterResults(2, 6); // team2 pAgainst=6 (won't underflow after -5)

        // The match we will reverse: team1 wins 5-1
        Match match = new Match(team1, team2);
        match.enterResults(5, 1);
        // After: team1.pAgainst = 2+1 = 3, team2.pAgainst = 6+5 = 11

        int pForBefore     = team1.getStats().getPointsFor();
        int pAgainstBefore = team1.getStats().getPointsAgainst();

        match.reverseRecords(5, 1);
        // After: team1.pAgainst = 3-1 = 2 (>0), team2.pAgainst = 11-5 = 6 (>0)

        assertEquals(pForBefore - 5,      team1.getStats().getPointsFor(),
            "PointsFor should decrease by the reversed score");
        assertEquals(pAgainstBefore - 1,  team1.getStats().getPointsAgainst(),
            "PointsAgainst should decrease by the reversed score");
    }

    @Test
    void testReverseRecords_winsAndLossesAdjusted() {
        Team team3 = new Team(); team3.setName("Bears"); team3.setStats(new Stats());

        // Give team1 and team2 existing stats so reversing one result won't zero pAgainst
        Match warmup1 = new Match(team1, team3);
        warmup1.enterResults(4, 2); // team1: pFor=4 pAgainst=2 wins=1

        Match warmup2 = new Match(team2, team3);
        warmup2.enterResults(3, 1); // team2: pFor=3 pAgainst=1 wins=1

        Match match = new Match(team1, team2);
        match.enterResults(5, 2); // team1 wins again

        int winsBefore   = team1.getStats().getWins();
        int lossesBefore = team2.getStats().getLosses();

        match.reverseRecords(5, 2);

        assertEquals(winsBefore - 1,   team1.getStats().getWins(),
            "Winner's wins should be reduced by 1 after reversal");
        assertEquals(lossesBefore - 1, team2.getStats().getLosses(),
            "Loser's losses should be reduced by 1 after reversal");
    }
}
