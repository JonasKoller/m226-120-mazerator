package ch.jodo.mazerator.generator;

import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;

import java.util.Stack;

public interface MazeGeneratorEvent {

    void onUpdate(final MazeGrid maze, final Stack<Cell> stack, final Cell current);

    void onFinish(final MazeGrid finishedMaze);
}
