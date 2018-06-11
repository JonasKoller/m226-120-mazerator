package ch.jodo.mazerator.generator;

import ch.jodo.mazerator.util.MazeGrid;

@FunctionalInterface
public interface MazeGeneratorFinishEvent {
	void onFinish(final MazeGrid finishedMaze);
}
