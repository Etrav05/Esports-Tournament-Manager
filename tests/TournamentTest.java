package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.core.Tournament;
import src.core.TournamentFormat;
import src.core.TournamentState;
import src.factories.TournamentFactory;

import static org.junit.jupiter.api.Assertions.*;

// Mohamed Al-Husaini - Unit tests for Tournament class

public class TournamentTest {

    private TournamentFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TournamentFactory();
    }

    // -------------------------------------------------------
    // addTeam tests
    // -------------------------------------------------------

    @Test
    void testAddTeam_teamAppearsInList() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("Lions");
        assertEquals(1, t.getTeams().size());
        assertEquals("Lions", t.getTeams().get(0).getName());
    }

    @Test
    void testAddTeam_statsAreInitialized() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("Lions");
        assertNotNull(t.getTeams().get(0).getStats(),
            "New team should have Stats object initialized");
    }

    @Test
    void testAddTeam_exceedsMax_throwsException() {
        Tournament t = factory.createSingleElimination("Test", 2);
        t.addTeam("Lions");
        t.addTeam("Tigers");
        assertThrows(IllegalStateException.class, () -> t.addTeam("Bears"),
            "Should throw when adding a team beyond maxTeams");
    }

    @Test
    void testAddTeam_multipleTeams_allAdded() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");
        assertEquals(4, t.getTeams().size());
    }

    // -------------------------------------------------------
    // generateBracket tests
    // -------------------------------------------------------

    @Test
    void testGenerateBracket_notEnoughTeams_throwsException() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("Lions"); // only 1 team
        assertThrows(IllegalStateException.class, t::generateBracket);
    }

    @Test
    void testGenerateBracket_oddTeams_nonRoundRobin_throwsException() {
        // Bypass factory to create an odd-team situation for single elim
        Tournament t = new Tournament();
        t.setFormat(TournamentFormat.SINGLE_ELIM);
        t.setMaxTeams(4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C"); // 3 teams, odd
        assertThrows(IllegalStateException.class, t::generateBracket);
    }

    @Test
    void testGenerateBracket_singleElim_4teams_creates2Matches() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");
        t.generateBracket();
        assertEquals(2, t.getMatches().size(),
            "4-team single elim should have 2 first-round matches");
    }

    @Test
    void testGenerateBracket_roundRobin_4teams_creates6Matches() {
        Tournament t = factory.createRoundRobin("Test", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");
        t.generateBracket();
        // C(4,2) = 6 round-robin matchups
        assertEquals(6, t.getMatches().size(),
            "4-team round robin should produce 6 total matches");
    }

    @Test
    void testGenerateBracket_matchListIsNotEmpty() {
        // Note: generateBracket() can only be called once per tournament —
        // the state machine blocks INPROGRESS -> INPROGRESS transitions.
        // This test simply confirms the bracket is populated after one call.
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");
        t.generateBracket();
        assertFalse(t.getMatches().isEmpty(),
            "Match list should be populated after generating a bracket");
    }

    // -------------------------------------------------------
    // State transition tests
    // -------------------------------------------------------

    @Test
    void testInitialState_isIdle() {
        Tournament t = factory.createSingleElimination("Test", 4);
        assertEquals(TournamentState.IDLE, t.getTournamentState());
    }

    @Test
    void testAddTeam_transitionsToSetup() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("Lions");
        assertEquals(TournamentState.SETUP, t.getTournamentState());
    }

    @Test
    void testGenerateBracket_transitionsToInProgress() {
        Tournament t = factory.createSingleElimination("Test", 4);
        t.addTeam("A");
        t.addTeam("B");
        t.addTeam("C");
        t.addTeam("D");
        t.generateBracket();
        assertEquals(TournamentState.INPROGRESS, t.getTournamentState());
    }

    // -------------------------------------------------------
    // Setter / getter tests
    // -------------------------------------------------------

    @Test
    void testSetName_updatesName() {
        Tournament t = factory.createSingleElimination("Original", 4);
        t.setName("Updated Name");
        assertEquals("Updated Name", t.getName());
    }

    @Test
    void testGetMatchHistory_initiallyEmpty() {
        Tournament t = factory.createSingleElimination("Test", 4);
        assertTrue(t.getMatchHistory().isEmpty());
    }

    @Test
    void testGetCommandHistory_notNull() {
        Tournament t = factory.createSingleElimination("Test", 4);
        assertNotNull(t.getCommandHistory());
    }
}
