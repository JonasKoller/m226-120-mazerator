package ch.jodo.mazerator.solver;

import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;

import java.util.Queue;

public interface DijkstraMazeSolverEvent {
    void onUpdate(final MazeGrid maze, final Queue<Cell> queue, final Cell current);

    void onFinish(final MazeGrid finishedMaze);
}
