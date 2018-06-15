package ch.jodo.mazerator.solver;

import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;

import java.util.*;

public class DijkstraMazeSolver {

	private List<DijkstraMazeSolverEvent> eventListeners = new LinkedList<>();
	private List<DijkstraMazeSolverFinishEvent> finishEventListeners = new LinkedList<>();

	public void solveMaze(MazeGrid maze) {

		new Thread(() -> {

			Cell sourceCell = maze.getStartCell(); // Define start cell

			Queue<Cell> queue = new PriorityQueue<>(Comparator.comparing(
					Cell::getDistance
			));

			// Add all cells to the queue
			for (Cell c: maze.getCells()) {
				if (sourceCell == c) // Set the distance of start cell to 0
					c.setDistance(0);

				queue.add(c);
			}

			while (!queue.isEmpty()) {

				Cell current = queue.poll();
				List<Cell> neighbors = maze.getTouchableNeighbors(current.getX(), current.getY());

				notifyListenersForUpdates(maze, queue, current);

				for (Cell n: neighbors) {
					if (queue.contains(n)) {
						int newDistance = current.getDistance() + 1;

						if (newDistance < n.getDistance()) {
							n.setDistance(newDistance);
							n.setPrevious(current);

							queue.remove(n); // Add and remove cell to update queue
							queue.add(n);
						}
					}
				}
			}

			notifyListenersForFinish(maze);
		}).start();

	}


	public void subscribe(DijkstraMazeSolverEvent listener) {
		eventListeners.add(listener);
	}

	public void subscribe(DijkstraMazeSolverFinishEvent listener) {
		finishEventListeners.add(listener);
	}

	public void unsubscribe(DijkstraMazeSolverEvent listener) {
		eventListeners.remove(listener);
	}

	public void unsubscribe(DijkstraMazeSolverFinishEvent listener) {
		finishEventListeners.remove(listener);
	}

	private void notifyListenersForUpdates(final MazeGrid maze, final Queue<Cell> queue, final Cell current) {
		for (DijkstraMazeSolverEvent listener: eventListeners)
			listener.onUpdate(maze, queue, current);
	}

	private void notifyListenersForFinish(final MazeGrid finishedMaze) {
		for (DijkstraMazeSolverEvent listener: eventListeners)
			listener.onFinish(finishedMaze);

		for (DijkstraMazeSolverFinishEvent listener: finishEventListeners) {
			listener.onFinish(finishedMaze);
		}
	}
}
