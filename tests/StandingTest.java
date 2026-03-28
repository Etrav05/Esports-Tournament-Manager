import org.junit.jupiter.api.Test;
import src.models.Standing;
import src.models.Stats;
import src.models.Team;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Mohamed Al-Husaini - Unit tests for Standing class (sorting and ranking)

public class StandingTest {

    // Helper: build a Team with preset stats
    private Team makeTeam(String name, int wins, int losses, double ratio) {
        Team team = new Team();
        team.setName(name);
        Stats stats = new Stats();
        stats.setWins(wins);
        stats.setLosses(losses);
        stats.setPointsRatio(ratio);
        team.setStats(stats);
        return team;
    }

    // -------------------------------------------------------
    // Sorting tests
    // -------------------------------------------------------

    @Test
    void testSortByWins_descendingOrder() {
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(makeTeam("C", 1, 2, 1.0));
        teams.add(makeTeam("A", 3, 0, 3.0));
        teams.add(makeTeam("B", 2, 1, 2.0));

        Standing standing = new Standing(teams);
        standing.updateStandings();

        assertEquals("A", standing.getTeamStandings().get(0).getName(), "1st place should be A (3 wins)");
        assertEquals("B", standing.getTeamStandings().get(1).getName(), "2nd place should be B (2 wins)");
        assertEquals("C", standing.getTeamStandings().get(2).getName(), "3rd place should be C (1 win)");
    }

    @Test
    void testSortByRatio_whenWinsAreTied() {
        ArrayList<Team> teams = new ArrayList<>();
        // Both have 2 wins; Alpha has better ratio
        teams.add(makeTeam("Low",  2, 1, 1.5));
        teams.add(makeTeam("High", 2, 1, 3.0));

        Standing standing = new Standing(teams);
        standing.updateStandings();

        assertEquals("High", standing.getTeamStandings().get(0).getName(),
            "Team with higher ratio should rank first when wins are tied");
    }

    // -------------------------------------------------------
    // getTopStanding tests
    // -------------------------------------------------------

    @Test
    void testGetTopStanding_returnsHighestRankedTeam() {
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(makeTeam("Loser",  0, 3, 0.5));
        teams.add(makeTeam("Winner", 3, 0, 3.0));
        teams.add(makeTeam("Middle", 1, 2, 1.0));

        Standing standing = new Standing(teams);
        standing.updateStandings();

        assertEquals("Winner", standing.getTopStanding().getName());
    }

    @Test
    void testGetTopStanding_singleTeam() {
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(makeTeam("Solo", 5, 0, 5.0));

        Standing standing = new Standing(teams);
        standing.updateStandings();

        assertEquals("Solo", standing.getTopStanding().getName());
    }

    @Test
    void testGetTopStanding_emptyList_throwsException() {
        Standing standing = new Standing(new ArrayList<>());
        assertThrows(IndexOutOfBoundsException.class, standing::getTopStanding,
            "Getting top standing from empty list should throw");
    }

    // -------------------------------------------------------
    // updateStandings / sortStandings tests
    // -------------------------------------------------------

    @Test
    void testUpdateStandings_alreadySorted_remainsOrdered() {
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(makeTeam("First",  3, 0, 3.0));
        teams.add(makeTeam("Second", 2, 1, 2.0));
        teams.add(makeTeam("Third",  1, 2, 1.0));

        Standing standing = new Standing(teams);
        standing.updateStandings();

        assertEquals("First",  standing.getTeamStandings().get(0).getName());
        assertEquals("Second", standing.getTeamStandings().get(1).getName());
        assertEquals("Third",  standing.getTeamStandings().get(2).getName());
    }

    @Test
    void testUpdateStandings_reverseSorted_correctlyOrdered() {
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(makeTeam("C", 0, 3, 0.5));
        teams.add(makeTeam("B", 1, 2, 1.0));
        teams.add(makeTeam("A", 3, 0, 3.0));

        Standing standing = new Standing(teams);
        standing.updateStandings();

        assertEquals("A", standing.getTeamStandings().get(0).getName());
        assertEquals("B", standing.getTeamStandings().get(1).getName());
        assertEquals("C", standing.getTeamStandings().get(2).getName());
    }
}
