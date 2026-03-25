package src.commands;

import java.util.Stack;

public class CommandHistory {
    private final Stack<Command> cmdhistory = new Stack<>(); // This makes sure commands are stored INORDER 

    // FUNCTIONS
    public void executeCommand(Command command) {
        command.execute();
        
        cmdhistory.push(command);
    }

    public void undoLast() {
        if (cmdhistory.isEmpty()) {
            throw new IllegalStateException("No commands to undo");
        }
        
        Command last = cmdhistory.pop();
        last.unexecute();
    }

    public boolean hasHistory() {
        return !cmdhistory.isEmpty();
    }
}