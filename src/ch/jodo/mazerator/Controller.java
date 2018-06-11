package ch.jodo.mazerator;

import ch.jodo.mazerator.display.DrawingUtil;
import ch.jodo.mazerator.display.MazeGenerationDrawer;
import ch.jodo.mazerator.generator.RecursiveBacktrackerMazeGenerator;
import ch.jodo.mazerator.solver.DijkstraMazeSolver;
import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Optional;

public class Controller {

    private static final Color COLOR_START_CELL = Color.LIGHTGREEN;
    private static final Color COLOR_END_CELL = Color.RED;
    private Color visitedColor = Color.GREEN;
    private Color stackColor = Color.PINK;
    private Color currentColor = Color.BLUE;

    private int mazeSize = 10;
    private int waitTime = 100;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;
    private DrawingUtil drawingUtil;

    //private Optional<MazeGrid> finishedMaze = Optional.empty();

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        drawingUtil = new DrawingUtil(gc, canvas.getWidth(), canvas.getHeight(), mazeSize);
    }

    @FXML
    public void createMaze() {
        RecursiveBacktrackerMazeGenerator gen = new RecursiveBacktrackerMazeGenerator();
        MazeGenerationDrawer mgd = new MazeGenerationDrawer(waitTime, drawingUtil, visitedColor, stackColor, currentColor, COLOR_START_CELL, COLOR_END_CELL);
        gen.subscribe(mgd);
        gen.subscribe((finishedMaze) -> {
            System.out.print(finishedMaze.getStartCell());
        });
        gen.generateMaze(mazeSize, mazeSize);
    }

    /*
    @FXML
    public void solveMaze() {
        DijkstraMazeSolver solver = new DijkstraMazeSolver();

        solver.subscribe((maze, queue, current) -> {
            Platform.runLater(() -> {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas

                for (Cell cell : queue) {
                    drawCell(cell, visitedColor);
                }

                drawCell(current, currentColor);

                drawMazeGrid(maze);
            });

            try {
                Thread.sleep(60); // Wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        finishedMaze.ifPresent(solver::solveMaze);
    }
    */
}


/*

            List<Cell> solution = new LinkedList<>();
            Cell current = maze.getEndCell();
            while (current != sourceCell) {
                solution.add(current);
                notifyListenersForUpdates(maze, queue, current, new LinkedList<>(), solution);
                current = current.getPrevious();
            }

 */
