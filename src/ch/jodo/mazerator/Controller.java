package ch.jodo.mazerator;

import ch.jodo.mazerator.display.DrawingUtil;
import ch.jodo.mazerator.display.MazeGenerationDrawer;
import ch.jodo.mazerator.display.MazeSolvingDrawer;
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
    private static final Color COLOR_END_CELL = Color.LIGHTCORAL;
    private Color currentColor = Color.BLUE;

    // Generation colors
    private Color visitedColor = Color.GREEN;
    private Color stackColor = Color.PINK;

    // Solving colors
    private Color queueColor = Color.CORNSILK;
    private Color solutionColor = Color.LIME;

    private int mazeSize = 10;
    private int waitTime = 200;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;
    private DrawingUtil drawingUtil;

    private Optional<MazeGrid> finishedMaze = Optional.empty();

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        drawingUtil = new DrawingUtil(gc, canvas.getWidth(), canvas.getHeight(), mazeSize);
    }

    @FXML
    public void createMaze() {
        RecursiveBacktrackerMazeGenerator gen = new RecursiveBacktrackerMazeGenerator();

        MazeGenerationDrawer mgd = new MazeGenerationDrawer(waitTime, drawingUtil, visitedColor, stackColor, currentColor, COLOR_START_CELL, COLOR_END_CELL);
        gen.subscribe(mgd); // subscribe with the drawer

        gen.subscribe((finishedMaze) -> { // Subscribe to get the finished maze
            this.finishedMaze = Optional.ofNullable(finishedMaze);
        });

        gen.generateMaze(mazeSize, mazeSize);
    }


    @FXML
    public void solveMaze() {
        DijkstraMazeSolver solver = new DijkstraMazeSolver();

        MazeSolvingDrawer msd = new MazeSolvingDrawer(waitTime, drawingUtil, queueColor, currentColor, solutionColor, COLOR_START_CELL, COLOR_END_CELL);
        solver.subscribe(msd);

        finishedMaze.ifPresent(solver::solveMaze);
    }
}