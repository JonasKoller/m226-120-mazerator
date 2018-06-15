package ch.jodo.mazerator.solver;

import ch.jodo.mazerator.util.MazeGrid;

@FunctionalInterface
public interface DijkstraMazeSolverFinishEvent {
	void onFinish(final MazeGrid finishedMaze);
}
