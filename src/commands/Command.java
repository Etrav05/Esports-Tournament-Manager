package src.commands;

public interface Command {
    void execute();
    
    void unexecute();
}