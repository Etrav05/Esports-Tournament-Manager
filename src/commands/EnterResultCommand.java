package src.commands;

import java.util.ArrayList;

import src.core.Tournament;
import src.models.Match;
import src.models.modelstates.MatchStatus;

public class EnterResultCommand implements Command {
    private final Match match;
    private final Tournament tournament;
    private final int newScoreTeam1;
    private final int newScoreTeam2;

    // Here we store previous state so we can unexecute them
    private int prevScoreTeam1;
    private int prevScoreTeam2;
    private MatchStatus prevStatus;
    private String prevResult;

    private ArrayList<Match> prevMatches;
    private ArrayList<Match> prevMatchHistory;


    public EnterResultCommand(Match match, Tournament tournament, int scoreTeam1, int scoreTeam2) {
        this.match = match;
        this.tournament = tournament;
        this.newScoreTeam1 = scoreTeam1;
        this.newScoreTeam2 = scoreTeam2;
    }

    @Override
    public void execute() {
        this.prevScoreTeam1 = match.getScoreTeam1();
        this.prevScoreTeam2 = match.getScoreTeam2();
        this.prevStatus = match.getStatus();
        this.prevResult = match.getResult();

        this.prevMatches = new ArrayList<>(tournament.getMatches());
        this.prevMatchHistory = new ArrayList<>(tournament.getMatchHistory());

        match.enterResults(newScoreTeam1, newScoreTeam2);
    }

    @Override
    public void unexecute() {
        match.setScoreTeam1(prevScoreTeam1);
        match.setScoreTeam2(prevScoreTeam2);
        match.setStatus(prevStatus);
        match.setResult(prevResult);
        match.reverseRecords(newScoreTeam1, newScoreTeam2);

        tournament.getMatches().clear();
        tournament.getMatches().addAll(prevMatches);
        tournament.getMatchHistory().clear();
        tournament.getMatchHistory().addAll(prevMatchHistory);
    }
}