package src.domainclasses;
import java.util.ArrayList;

public class Team {
    private String name;
    private ArrayList<Player> players;
    private Record record;

    // GETTERS
    public String getName() {
        return this.name;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public Record getRecord() {
        return this.record;
    }

    // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
