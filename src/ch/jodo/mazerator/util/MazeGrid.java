package ch.jodo.mazerator.util;

import java.util.*;

public class MazeGrid {

    // width and height (example: 10x10 -> 100 cells in total)
    private int width;
    private int height;

    // actual maze cells
    private List<Cell> cells;

    /**
     * Called when instantiating a new MazeGrid
     * @param width Amount of horizontal cells
     * @param height Amount of vertical cells
     */
    public MazeGrid(final int width, final int height) {
        this.width = width;
        this.height = height;

        // get the initial list with Cells representing the maze
        cells = getInitialCells(width, height);
    }

    /**
     * Get a cell at a specific x and y coordinate
     * <i>x and y start at 0</i>
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @return Cell at the specified coordinates
     */
    public Cell getCell(final int x, final int y) {
        // check if x or y is invalid
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1)
            return null;

        // get the cell from the list
        return cells.get(y * width + x);
    }

    /**
     * @return All cells of the maze
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     * Returns all neighbors of cell at x, y with this condition:
     * <b>Returns only neighbors which are connected to the cell (no walls between)</b>
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @return Connected neighbors of the cell
     */
    public List<Cell> getTouchableNeighbors(final int x, final int y) {
        List<Cell> neighbors = new LinkedList<>();
        Cell cell = getCell(x, y);

        // get the neighbors;
        if (!cell.isTopWall()) {
            Cell top = getCell(x, y - 1);
            neighbors.add(top);
        }

        if (!cell.isRightWall()) {
            Cell right = getCell(x + 1, y);
            neighbors.add(right);
        }

        if (!cell.isBottomWall()) {
            Cell bottom = getCell(x, y + 1);
            neighbors.add(bottom);
        }

        if (!cell.isLeftWall()) {
            Cell left = getCell(x - 1, y);
            neighbors.add(left);
        }

        // remove all neighbors which are NULL (can happen at border of the field)
        neighbors.removeIf(Objects::isNull);

        return neighbors;
    }

    /**
     * Method to get a random neighbor-cell, which wasn't visited
     * @param cell Cell, which neighbors are wanted
     * @return A random neighbor, if there is one
     */
    public Optional<Cell> getUnvisitedRandomNeighbor(final Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        List<Cell> neighbors = getUnvisitedNeighbors(x, y);

        // if there are neighbors in the list return a random one
        if (neighbors.size() > 0) {
            int r = getRandomIntInRange(0, neighbors.size() - 1);
            return Optional.of(neighbors.get(r));
        }

        // return an empty optional
        return Optional.empty();
    }

    /**
     * Return where the neighbor cell is
     * @param current Current cell
     * @param neighbor Neighbor cell
     * @return Direction, in which the cell is
     */
    public Direction getNeighborDirection(final Cell current, final Cell neighbor) {
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

    /**
     * @return The top left cell
     */
    public Cell getStartCell() {
        return cells.get(0);
    }

    /**
     * @return The bottom right cell
     */
    public Cell getEndCell() {
        return cells.get(cells.size() - 1);
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

    private List<Cell> getNeighbors(int x, int y) {
        List<Cell> neighbors = new LinkedList<>();

        // get the neighbors;
        Cell top = getCell(x, y - 1);
        Cell right = getCell(x + 1, y);
        Cell bottom = getCell(x, y + 1);
        Cell left = getCell(x - 1, y);

        // check if neighbor is valid and not visited then add them to neighbors

        neighbors.add(top);
        neighbors.add(right);
        neighbors.add(bottom);
        neighbors.add(left);

        // remove the neighbors which are null
        neighbors.removeIf(Objects::isNull);

        return neighbors;
    }

    private List<Cell> getUnvisitedNeighbors(int x, int y) {
        List<Cell> neighbors = getNeighbors(x, y);

        // remove all neighbors which are NULL (can happen at border of the field)
        neighbors.removeIf(Cell::isVisited);

        return neighbors;
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
