package src.models;
import src.models.modelstates.MatchStatus;

public class Match {
    private Team team1;
    private Team team2;
    private String result;
    private int scoreTeam1;
    private int scoreTeam2;
    private MatchStatus status;

    public Match(Team team1, Team team2) {
        if (team1 == null || team2 == null) {
            throw new IllegalArgumentException("Match requires exactly 2 teams");
        }

        if (team1 == team2) {
            throw new IllegalArgumentException("A team cannot play against itself");
        }

        this.team1 = team1;
        this.team2 = team2;
        this.status = MatchStatus.PENDING;
    }

    // FUNCTIONS
    public void enterResults(int scoreTeam1, int scoreTeam2) {
        if (scoreTeam1 == scoreTeam2) {
            throw new IllegalArgumentException("Match cannot end in a tie");
        }
        
        this.scoreTeam1 = scoreTeam1;
        this.scoreTeam2 = scoreTeam2;
        this.status = MatchStatus.COMPLETE;

        fillMatchResult();
        updateStatss();
    }

    public void fillMatchResult() { // NO TIES ALLOWED
        if (this.status != MatchStatus.COMPLETE) {
            throw new IllegalStateException("Match has not completed yet");
        }

        // Team 1 wins 
        if (this.scoreTeam1 > this.scoreTeam2) {                
            this.result = this.team1.getName() + " def. " + this.team2.getName() + " (" + this.scoreTeam1 + " - " + this.scoreTeam2 + ")";
        } 

        // Team 2 wins
        else { 
            this.result = this.team2.getName() + " def. " + this.team1.getName() + " (" + this.scoreTeam2 + " - " + this.scoreTeam1 + ")";
        }
    }

    private void updateStatss(){
        team1.getStats().setPointsFor(team1.getStats().getPointsFor() + scoreTeam1);
        team1.getStats().setPointsAgainst(team1.getStats().getPointsAgainst() + scoreTeam2);

        team2.getStats().setPointsFor(team2.getStats().getPointsFor() + scoreTeam2);
        team2.getStats().setPointsAgainst(team2.getStats().getPointsAgainst() + scoreTeam1);

        if (scoreTeam1 > scoreTeam2) {
            team1.getStats().setWins(team1.getStats().getWins() + 1);
            team2.getStats().setLosses(team2.getStats().getLosses() + 1);
        } 
        
        else {
            team2.getStats().setWins(team2.getStats().getWins() + 1);
            team1.getStats().setLosses(team1.getStats().getLosses() + 1);
        }

        updateRatio(team1);
        updateRatio(team2);
    }

    private void updateRatio(Team team) {
        int pAgainst = team.getStats().getPointsAgainst();
        int pFor = team.getStats().getPointsFor();

        if (pAgainst == 0) {
             team.getStats().setPointsRatio((double)pFor);
        }

        double ratio = pFor / pAgainst;
        
        team.getStats().setPointsRatio(ratio);
    }

    public void reverseRecords(int prevScoreTeam1, int prevScoreTeam2) {        
        team1.getStats().setPointsFor(team1.getStats().getPointsFor() - prevScoreTeam1);
        team1.getStats().setPointsAgainst(team1.getStats().getPointsAgainst() - prevScoreTeam2);

        team2.getStats().setPointsFor(team2.getStats().getPointsFor() - prevScoreTeam2);
        team2.getStats().setPointsAgainst(team2.getStats().getPointsAgainst() - prevScoreTeam1);

        if (prevScoreTeam1 > prevScoreTeam2) {
            team1.getStats().setWins(team1.getStats().getWins() - 1);
            team2.getStats().setLosses(team2.getStats().getLosses() - 1);
        } else {
            team2.getStats().setWins(team2.getStats().getWins() - 1);
            team1.getStats().setLosses(team1.getStats().getLosses() - 1);
        }

        updateRatio(team1);
        updateRatio(team2);
    }

    // GETTERS
    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public String getResult() {
        return result;
    }

    // SETTERS
    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setResult(String result) {
        this.result = result;
    }
}