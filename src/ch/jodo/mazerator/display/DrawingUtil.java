package ch.jodo.mazerator.display;

import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawingUtil {

    private GraphicsContext gc;
    private double canvasWidth;
    private double canvasHeight;
    private int mazeSize;

    public DrawingUtil(final GraphicsContext gc, final double canvasWidth, final double canvasHeight, final int mazeSize) {
        this.gc = gc;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.mazeSize = mazeSize;
    }

    /**
     * Clears the canvas, so there is no content
     */
    public void clearCanvas() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
    }

    /**
     * Set the background-color for a specific cell
     * @param cell The highlighted cell
     * @param color Color of the highlight
     */
    public void drawCell(final Cell cell, final Color color) {
        drawCell(cell.getX(), cell.getY(), color);
    }

    /**
     * Draws the borders of each cell and a thick border around the maze
     * @param maze Maze with the cells
     */
    public void drawMazeGrid(final MazeGrid maze) {
        double cellSize = getCellSize();
        for (Cell cell : maze.getCells()) { // Go through all cells and look which walls should be drawn
            int x = cell.getX();
            int y = cell.getY();

            if (cell.isTopWall()) {
                drawLine(x, y, x + 1, y, cellSize);
            }
            if (cell.isRightWall()) {
                drawLine(x + 1, y, x + 1, y + 1, cellSize);
            }
            if (cell.isBottomWall()) {
                drawLine(x, y + 1, x + 1, y + 1, cellSize);
            }
            if (cell.isLeftWall()) {
                drawLine(x, y, x, y + 1, cellSize);
            }
        }

        drawBorder();
    }

    /**
     * A method to highlight the start and end cell
     * @param mazeGrid Grid to get start and end cell
     * @param startColor Color of start-cell
     * @param endColor Color of end-cell
     */
    public void drawStartAndEndCell(MazeGrid mazeGrid, Color startColor, Color endColor) {
        drawCell(mazeGrid.getStartCell(), startColor);
        drawCell(mazeGrid.getEndCell(), endColor);
    }
    private double getCellSize() {
        return canvasWidth / mazeSize;
    }

    private void drawCell(final int x, final int y, final Color color) {
        double cellSize = getCellSize();
        gc.setFill(color);
        gc.fillRect(Math.floor(x * cellSize), Math.floor(y * cellSize), Math.floor(cellSize), Math.floor(cellSize));
    }

    private void drawLine(int x1, int y1, int x2, int y2, double factor) {
        drawLine(x1, y1, x2, y2, factor, 1);
    }

    private void drawLine(int x1, int y1, int x2, int y2, int width) {
        drawLine(x1, y1, x2, y2, 1, width);
    }

    private void drawLine(int x1, int y1, int x2, int y2, double factor, int width) {
        gc.setLineWidth(width);
        gc.beginPath();
        gc.moveTo(x1 * factor, y1 * factor);
        gc.lineTo(x2 * factor, y2 * factor);
        gc.stroke();
    }

    private void drawBorder() {
        drawLine(0, (int) getCellSize(), 0, (int) canvasHeight, 6); // Linken Rand zeichnen
        drawLine(0, (int) canvasHeight, (int) canvasWidth, (int) canvasHeight, 6); // Unteren Rand zeichen
        drawLine((int) canvasWidth, 0, (int) canvasWidth, (int) (canvasHeight - getCellSize()), 6); // Rechten Rand zeichnen
        drawLine(0, 0, (int) canvasWidth, 0, 6); // Oberen Rand zeichen
    }


    /*
       GETTERS AND SETTERS
     */
    public double getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(double canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public double getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(double canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public int getMazeSize() {
        return mazeSize;
    }

    public void setMazeSize(int mazeSize) {
        this.mazeSize = mazeSize;
    }

}
