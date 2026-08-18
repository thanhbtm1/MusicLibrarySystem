package com.musiclibrary.commands;

import java.util.Stack;

public class CommandManager {
    private final Stack<ICommand> history = new Stack<>();
    private final Stack<ICommand> redoStack = new Stack<>();

    public void executeCommand(ICommand command) {
        command.execute();
        history.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!history.isEmpty()) {
            ICommand command = history.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("Nothing to undo."); // Simple feedback for CLI
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            ICommand command = redoStack.pop();
            command.execute();
            history.push(command);
        } else {
            System.out.println("Nothing to redo.");
        }
    }
}
