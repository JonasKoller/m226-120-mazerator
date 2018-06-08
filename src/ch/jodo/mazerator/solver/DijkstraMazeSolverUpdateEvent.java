package ch.jodo.mazerator.solver;

import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;

import java.util.List;
import java.util.Queue;

public interface DijkstraMazeSolverUpdateEvent {
    void onUpdate(final MazeGrid maze, final Queue<Cell> queue, final Cell current, final List<Cell> neighbors, final List<Cell> solution);
}
