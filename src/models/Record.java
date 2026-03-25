package src.models;

public class Record {
    private int wins;
    private int losses;
    private int pointsFor;
    private int pointsAgainst;
    private double pointsRatio;

    // GETTERS
    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }

    public int getPointsFor() {
        return this.pointsFor;
    }

    public int getPointsAgainst() {
        return this.pointsAgainst;
    }

    public double getPointsRatio() {
        return this.pointsRatio;
    }

    // SETTERS
    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setPointsFor(int pointsFor) {
        this.pointsFor = pointsFor;
    }

    public void setPointsAgainst(int pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

    public void setPointsRatio(double pointsRatio) {
        this.pointsRatio = pointsRatio;
    }
}
