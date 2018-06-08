package ch.jodo.mazerator;

import ch.jodo.mazerator.generator.RecursiveBacktrackerMazeGenerator;
import ch.jodo.mazerator.generator.MazeGeneratorUpdateEvent;
import ch.jodo.mazerator.solver.DijkstraMazeSolver;
import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.Stack;

public class Controller implements MazeGeneratorUpdateEvent {

    private int mazeSize = 25;
    private Color visitedColor = Color.GREEN;
    private Color stackColor = Color.RED;
    private Color currentColor = Color.BLUE;
    private Color neighborColor = Color.FUCHSIA;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;
    private Optional<MazeGrid> finishedMaze = Optional.empty();

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
    }

    public void onUpdate(final MazeGrid maze, final Stack<Cell> stack, final Cell current) {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas
            double cellSize = getCellSize(); // Calc size of each cell

            // Go through all visited cells and highlight them
            for (Cell cell : maze.getCells()) {
                if (cell.isVisited()) {
                    drawCell(cell, visitedColor);
                }
            }

            // Go through all cells in stack and highlight them (overwrites the visited highlight)
            for (Cell cell : stack) {
                drawCell(cell, stackColor);
            }

            // Highlight current cell
            drawCell(current, currentColor);

            // Draw the grid over the canvas
            drawMazeGrid(maze);
        });

        try {
            Thread.sleep(0); // Wait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onFinish(final MazeGrid finishedMaze) {
        this.finishedMaze = Optional.of(finishedMaze);
    }

    private void drawMazeGrid(final MazeGrid maze) {
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

    private double getCellSize() {
        return canvas.getWidth() / mazeSize;
    }

    private void drawCell(final Cell cell, final Color color) {
        double cellSize = getCellSize();
        gc.setFill(color);
        gc.fillRect(Math.floor(cell.getX() * cellSize), Math.floor(cell.getY() * cellSize), Math.floor(cellSize), Math.floor(cellSize));
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
        drawLine(0, (int) getCellSize(), 0, (int) canvas.getHeight(), 6); // Linken Rand zeichnen
        drawLine(0, (int) canvas.getHeight(), (int) canvas.getWidth(), (int) canvas.getHeight(), 6); // Unteren Rand zeichen
        drawLine((int) canvas.getWidth(), 0, (int) canvas.getWidth(), (int) (canvas.getHeight() - getCellSize()), 6); // Rechten Rand zeichnen
        drawLine(0, 0, (int) canvas.getWidth(), 0, 6); // Oberen Rand zeichen
    }

    @FXML
    public void createMaze() {
        RecursiveBacktrackerMazeGenerator gen = new RecursiveBacktrackerMazeGenerator();
        gen.subscribe(this);
        gen.generateMaze(mazeSize, mazeSize);
    }

    @FXML
    public void solveMaze() {
        DijkstraMazeSolver solver = new DijkstraMazeSolver();
        solver.subscribe((maze, queue, current, neighbors, solution) -> {
            Platform.runLater(() -> {
                for (Cell cell : queue) {
                    drawCell(cell, visitedColor);
                }

                for (Cell cell : neighbors) {
                    drawCell(cell, neighborColor);
                }

                for (Cell cell : solution) {
                    drawCell(cell, stackColor);
                }

                drawCell(current, currentColor);

                drawMazeGrid(maze);
            });

            try {
                Thread.sleep(40); // Wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        finishedMaze.ifPresent(solver::solveMaze);
    }
}
