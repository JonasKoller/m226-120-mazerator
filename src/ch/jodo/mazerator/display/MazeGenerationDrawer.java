package ch.jodo.mazerator.display;

import ch.jodo.mazerator.generator.MazeGeneratorEvent;
import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.Stack;

public class MazeGenerationDrawer implements MazeGeneratorEvent {

    private int waitTime;
    private DrawingUtil du;

    private Color visitedColor;
    private Color stackColor;
    private Color currentCellColor;

    private Color startCellColor;
    private Color endCellColor;

    public MazeGenerationDrawer(final int waitTime, final DrawingUtil drawingUtil, final Color visitedColor, final Color stackColor,
                                final Color currentCellColor, final Color startCellColor, final Color endCellColor) {
        this.waitTime = waitTime;
        this.du = drawingUtil;

        this.visitedColor = visitedColor;
        this.stackColor = stackColor;
        this.currentCellColor = currentCellColor;

        this.startCellColor = startCellColor;
        this.endCellColor = endCellColor;
    }

    public void onUpdate(MazeGrid maze, Stack<Cell> stack, Cell current) {
        //Platform.runLater(() -> {
            du.clearCanvas(); // clear everything of the canvas

            // Go through all visited cells and highlight them
            for (Cell cell : maze.getCells()) {
                if (cell.isVisited()) {
                    du.drawCell(cell, visitedColor);
                }
            }

            // Go through all cells in stack and highlight them (overwrites the visited highlight)
            for (Cell cell : stack) {
                du.drawCell(cell, stackColor);
            }

            // Highlight current cell
            du.drawCell(current, currentCellColor);

            // Draw the grid over the canvas
            du.drawMazeGrid(maze);

            try {
                Thread.sleep(waitTime); // Wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //});
    }

    public void onFinish(MazeGrid finishedMaze) {
        if (finishedMaze == null)
            return; // Return if finishedMaze is NULL

        //Platform.runLater(() -> {
            du.clearCanvas(); // Clean the whole canvas

            du.drawStartAndEndCell(finishedMaze, startCellColor, endCellColor);  // Highlight start and end

            du.drawMazeGrid(finishedMaze); // Draw the finished maze
        //});
    }


    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public Color getVisitedColor() {
        return visitedColor;
    }

    public void setVisitedColor(Color visitedColor) {
        this.visitedColor = visitedColor;
    }

    public Color getStackColor() {
        return stackColor;
    }

    public void setStackColor(Color stackColor) {
        this.stackColor = stackColor;
    }

    public Color getCurrentCellColor() {
        return currentCellColor;
    }

    public void setCurrentCellColor(Color currentCellColor) {
        this.currentCellColor = currentCellColor;
    }

    public Color getStartCellColor() {
        return startCellColor;
    }

    public void setStartCellColor(Color startCellColor) {
        this.startCellColor = startCellColor;
    }

    public Color getEndCellColor() {
        return endCellColor;
    }

    public void setEndCellColor(Color endCellColor) {
        this.endCellColor = endCellColor;
    }
}
