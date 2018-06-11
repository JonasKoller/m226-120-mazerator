package ch.jodo.mazerator.generator;

import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class RecursiveBacktrackerMazeGenerator {

	private List<MazeGeneratorEvent> eventListeners = new LinkedList<>();
	private List<MazeGeneratorFinishEvent> finishEventListeners = new LinkedList<>();

	public void generateMaze(final int width, final int height) {
		new Thread(() -> {
			MazeGrid maze = new MazeGrid(width, height);
			Stack<Cell> stack = new Stack<>();

			// Open left wall on startfield
			Cell start = maze.getStartCell();
			start.setLeftWall(false);

			// Open right wall on endfield
			Cell end = maze.getEndCell();
			end.setRightWall(false);

			// Push startfield to stack
			stack.push(start);

			// Start recursive function
			generateRecursive(maze, stack, start);

			// When no more recursive functions called, the algorithm is finished and the maze is finished
			notifyListenersForFinish(maze);
		}).start();
	}

	private void generateRecursive(MazeGrid maze, Stack<Cell> stack, Cell current) {
		notifyListenersForUpdates(maze, stack, current);

		// set current cell visited
		current.setVisited(true);

		// Check for unvisited neighbors and if they exist
		Optional<Cell> potentialNext = maze.getUnvisitedRandomNeighbor(current);
		if (potentialNext.isPresent()) {

			// Push neighbor to the stack
			Cell next = potentialNext.get();
			stack.push(next);

			// Remove walls between current cell and neighbor
			switch (maze.getNeighborDirection(current, next)) {
				case TOP:
					current.setTopWall(false);
					next.setBottomWall(false);
					break;
				case RIGHT:
					current.setRightWall(false);
					next.setLeftWall(false);
					break;
				case BOTTOM:
					current.setBottomWall(false);
					next.setTopWall(false);
					break;
				case LEFT:
					current.setLeftWall(false);
					next.setRightWall(false);
					break;
			}

			// Call recursive function again with neighbor
			generateRecursive(maze, stack, next);
		}

		// If stack is not empty, call recursive function with top of stack
		if (!stack.isEmpty()) {
			generateRecursive(maze, stack, stack.pop());
		}
	}

	public void subscribe(MazeGeneratorEvent listener) {
		eventListeners.add(listener);
	}

	public void subscribe(MazeGeneratorFinishEvent listener) {
		finishEventListeners.add(listener);
	}

	public void unSubscribe(MazeGeneratorEvent listener) {
		eventListeners.remove(listener);
	}

	public void unSubscribe(MazeGeneratorFinishEvent listener) {
		finishEventListeners.remove(listener);
	}

	private void notifyListenersForUpdates(final MazeGrid maze, final Stack<Cell> stack, final Cell current) {
		for (MazeGeneratorEvent listener : eventListeners)
			listener.onUpdate(maze, stack, current);
	}

	private void notifyListenersForFinish(final MazeGrid maze) {
		for (MazeGeneratorEvent listener : eventListeners)
			listener.onFinish(maze);

		for (MazeGeneratorFinishEvent mazeGeneratorFinishEvent : finishEventListeners)
			mazeGeneratorFinishEvent.onFinish(maze);
	}
}
