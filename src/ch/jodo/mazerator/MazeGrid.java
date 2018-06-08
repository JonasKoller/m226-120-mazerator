package ch.jodo.mazerator;

import java.util.*;

public class MazeGrid {

    private int width;
    private int height;

    private List<Cell> cells;

    public MazeGrid(int width, int height) {
        this.width = width;
        this.height = height;

        // get the initial list with Cells representing the maze
        cells = getInitialCells(width, height);
    }

    private ArrayList<Cell> getInitialCells(int width, int height) {
        ArrayList<Cell> result = new ArrayList<>();

        // loop through all fields and create cells and put them in fields
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                result.add(new Cell(w, h));
            }
        }

        return result;
    }

    public Cell getCell(int x, int y) {
        // check if x and y is not valid
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1)
            return null;
        // get the cell from the list
        return cells.get(y * width + x);
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Optional<Cell> getUnvisitedRandomNeighbor(int x, int y) {
        List<Cell> neighbors = new LinkedList<>();

        // get the neighbors;
        Cell top = getCell(x, y - 1);
        Cell right = getCell(x + 1, y);
        Cell bottom = getCell(x, y + 1);
        Cell left = getCell(x - 1, y);


        // check if neighbor is valid and not visited then add them to neighbors
        if (top != null && !top.isVisited()) {
            neighbors.add(top);
        }

        if (right != null && !right.isVisited()) {
            neighbors.add(right);
        }

        if (bottom != null && !bottom.isVisited()) {
            neighbors.add(bottom);
        }

        if (left != null && !left.isVisited()) {
            neighbors.add(left);
        }

        // if there are neighbors in the list return a random one
        if (neighbors.size() > 0) {
            int r = getRandomIntInRange(0, neighbors.size() - 1);
            return Optional.of(neighbors.get(r));
        }

        // return an empty optional
        return Optional.empty();
    }

    public Optional<Cell> getUnvisitedRandomNeighbor(Cell cell) {
        return getUnvisitedRandomNeighbor(cell.getX(), cell.getY());
    }

    public Direction getNeighborDirection(Cell current, Cell neighbor) {
        // check if neighbor is on the same x axis
        if (current.getX() == neighbor.getX()) {
            // look if neighbor is down
            if (current.getY() == neighbor.getY() - 1)
                return Direction.BOTTOM;
            // look if neighbor is up
            if (current.getY() == neighbor.getY() + 1)
                return Direction.TOP;
        }

        // check if neighbor is on the same y axis
        if (current.getY() == neighbor.getY()) {
            // look if neighbor is right
            if (current.getX() == neighbor.getX() - 1)
                return Direction.RIGHT;
            // look if neighbor is left
            if (current.getX() == neighbor.getX() + 1)
                return Direction.LEFT;
        }
        return null;
    }

    public Cell getStartCell() {
        return cells.get(0);
    }

    public Cell getEndCell() {
        return cells.get(cells.size() - 1);
    }

    private static int getRandomIntInRange(int min, int max) {
        // look if min / max is same then return min
        if (min == max)
            return min;

        // min is greater than max throw an error
        if (min > max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        // create the random int in the specified range
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
