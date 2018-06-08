package ch.jodo.mazerator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Stack;

public class Controller implements MazeGeneratorUpdateEvent {
    private int mazeSize = 25;
    private Color visitedColor = Color.GREEN;
    private Color stackColor = Color.RED;
    private Color currentColor = Color.BLUE;

    @FXML
    private Canvas canvas;

    GraphicsContext gc;

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
    }

    public void onUpdate(final MazeGrid maze, final Stack<Cell> stack, final Cell current) {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Ganzens Canvas leeren
            double cellSize = getCellSize(); // Grösse der einzelnen Zellen berechnen

            // Durch alle Zellen gehen, welche bereits besucht wurden und diese mit der passenden Farbe markieren
            for (Cell cell : maze.getCells()) {
                if (cell.isVisited()) {
                    drawCell(cell, visitedColor);
                }
            }

            // Durch den aktuellen Stack gehen und diesem markieren (überschreibt die Markierung der besuchten Felder)
            for (Cell cell : stack) {
                drawCell(cell, stackColor);
            }

            // Aktuelle Zelle markieren
            drawCell(current, currentColor);

            // Das Gitter wieder darübermalen
            drawMazeGrid(maze);
        });


        try {
            Thread.sleep(40); // Warten
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void drawMazeGrid(final MazeGrid maze) {
        double cellSize = getCellSize();
        for (Cell cell : maze.getCells()) {
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

    private void drawLine(int x1, int y1, int x2, int y2, double factor,  int width) {
        gc.setLineWidth(width);
        gc.beginPath();
        gc.moveTo(x1 * factor, y1 * factor);
        gc.lineTo(x2 * factor, y2 * factor);
        gc.stroke();
    }

    private void drawBorder() {
        drawLine(0, (int)getCellSize(), 0, (int)canvas.getHeight(), 6); // Linken Rand zeichnen
        drawLine(0, (int)canvas.getHeight(), (int)canvas.getWidth(), (int)canvas.getHeight(), 6); // Unteren Rand zeichen
        drawLine((int)canvas.getWidth(), 0, (int)canvas.getWidth(), (int)(canvas.getHeight() - getCellSize()), 6); // Rechten Rand zeichnen
        drawLine(0, 0, (int)canvas.getWidth(), 0, 6); // Oberen Rand zeichen
    }

    @FXML
    public void createMaze() {
        MazeGenerator gen = new MazeGenerator();
        gen.subscribe(this);
        gen.generateMaze(mazeSize, mazeSize);
    }
}
