package ch.jodo.mazerator;

import java.util.Stack;

public interface MazeGeneratorUpdateEvent {

    void onUpdate(final MazeGrid maze, final Stack<Cell> stack, final Cell current);

}
