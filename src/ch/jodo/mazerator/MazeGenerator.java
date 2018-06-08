package ch.jodo.mazerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class MazeGenerator {

    private List<MazeGeneratorUpdateEvent> updateEventListeners = new LinkedList<>();

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
        }).start();
    }

    private void generateRecursive(MazeGrid maze, Stack<Cell> stack, Cell current) {
        notifyListeners(maze, stack, current);

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

    public void subscribe(MazeGeneratorUpdateEvent listener) {
        updateEventListeners.add(listener);
    }

    public void unSubscribe(MazeGeneratorUpdateEvent listener) {
        updateEventListeners.remove(listener);
    }

    private void notifyListeners(final MazeGrid maze, final Stack<Cell> stack, final Cell current) {
        for (MazeGeneratorUpdateEvent listener : updateEventListeners)
            listener.onUpdate(maze, stack, current);
    }
}
