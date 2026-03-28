import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.core.Tournament;
import src.core.TournamentFormat;
import src.factories.TournamentFactory;

import static org.junit.jupiter.api.Assertions.*;

// Mohamed Al-Husaini - Unit tests for TournamentFactory

public class TournamentFactoryTest {

    private TournamentFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TournamentFactory();
    }

    // -------------------------------------------------------
    // createSingleElimination
    // -------------------------------------------------------

    @Test
    void testCreateSingleElimination_setsNameFormatMaxTeams() {
        Tournament t = factory.createSingleElimination("SE Cup", 8);
        assertEquals("SE Cup", t.getName());
        assertEquals(TournamentFormat.SINGLE_ELIM, t.getFormat());
        assertEquals(8, t.getMaxTeams());
    }

    @Test
    void testCreateSingleElimination_startsWithNoTeams() {
        Tournament t = factory.createSingleElimination("SE Cup", 8);
        assertTrue(t.getTeams().isEmpty());
    }

    @Test
    void testCreateSingleElimination_calculateRounds_8teams() {
        Tournament t = factory.createSingleElimination("SE Cup", 8);
        assertEquals(3, t.calculateRounds()); // log2(8) = 3
    }

    @Test
    void testCreateSingleElimination_calculateRounds_4teams() {
        Tournament t = factory.createSingleElimination("SE Cup", 4);
        assertEquals(2, t.calculateRounds()); // log2(4) = 2
    }

    // -------------------------------------------------------
    // createDoubleElimination
    // -------------------------------------------------------

    @Test
    void testCreateDoubleElimination_setsFormat() {
        Tournament t = factory.createDoubleElimination("DE Cup", 4);
        assertEquals(TournamentFormat.DOUBLE_ELIM, t.getFormat());
    }

    @Test
    void testCreateDoubleElimination_calculateRounds_8teams() {
        Tournament t = factory.createDoubleElimination("DE Cup", 8);
        assertEquals(6, t.calculateRounds()); // log2(8)*2 = 6
    }

    // -------------------------------------------------------
    // createRoundRobin
    // -------------------------------------------------------

    @Test
    void testCreateRoundRobin_setsFormat() {
        Tournament t = factory.createRoundRobin("RR League", 4);
        assertEquals(TournamentFormat.ROUND_ROBIN, t.getFormat());
    }

    @Test
    void testCreateRoundRobin_calculateRounds_4teams() {
        Tournament t = factory.createRoundRobin("RR League", 4);
        assertEquals(3, t.calculateRounds()); // 4 - 1 = 3
    }

    // -------------------------------------------------------
    // Validation: invalid team counts
    // -------------------------------------------------------

    @Test
    void testValidation_notPowerOfTwo_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.createSingleElimination("Bad", 3));
    }

    @Test
    void testValidation_notPowerOfTwo_5teams_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.createDoubleElimination("Bad", 5));
    }

    @Test
    void testValidation_tooManyTeams_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.createSingleElimination("Bad", 64));
    }

    @Test
    void testValidation_onlyOneTeam_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.createRoundRobin("Bad", 1));
    }

    @Test
    void testValidation_zeroTeams_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.createRoundRobin("Bad", 0));
    }

    @Test
    void testValidation_exactlyMaxAllowed_32teams_valid() {
        assertDoesNotThrow(() -> factory.createSingleElimination("Max", 32));
    }
}
