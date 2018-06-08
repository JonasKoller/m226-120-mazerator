package ch.jodo.mazerator.solver;

import ch.jodo.mazerator.generator.MazeGeneratorUpdateEvent;
import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;

import java.util.*;

public class DijkstraMazeSolver {

    private List<DijkstraMazeSolverUpdateEvent> updateEventListeners = new LinkedList<>();

    public void solveMaze(MazeGrid maze) {

        new Thread(() -> {

            Cell sourceCell = maze.getStartCell(); // Define start cell

            Queue<Cell> queue = new PriorityQueue<>(Comparator.comparing(
                    Cell::getDistance
            ));

            // Add all cells to the queue
            for (Cell c : maze.getCells()) {
                if (sourceCell == c) { // Set the distance of start cell to 0
                    c.setDistance(0);
                }

                queue.add(c);
            }

            while (!queue.isEmpty()) {

                Cell current = queue.poll();
                List<Cell> neighbors = maze.getTouchableNeighbors(current.getX(), current.getY());

                notifyListenersForUpdates(maze, queue, current, neighbors);

                for (Cell n : neighbors) {
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

            List<Cell> solution = new LinkedList<>();
            Cell current = maze.getEndCell();
            while (current != sourceCell) {
                solution.add(current);
                notifyListenersForUpdates(maze, queue, current, new LinkedList<>(), solution);
                current = current.getPrevious();
            }
        }).start();

    }


    public void subscribe(DijkstraMazeSolverUpdateEvent listener) {
        updateEventListeners.add(listener);
    }

    public void unSubscribe(DijkstraMazeSolverUpdateEvent listener) {
        updateEventListeners.remove(listener);
    }


    private void notifyListenersForUpdates(final MazeGrid maze, final Queue<Cell> queue, final Cell current, final List<Cell> neighbors) {
        notifyListenersForUpdates(maze, queue, current, neighbors, new LinkedList<>());
    }

    private void notifyListenersForUpdates(final MazeGrid maze, final Queue<Cell> queue, final Cell current, final List<Cell> neighbors, final List<Cell> solution) {
        for (DijkstraMazeSolverUpdateEvent listener : updateEventListeners)
            listener.onUpdate(maze, queue, current, neighbors, solution);
    }
}
