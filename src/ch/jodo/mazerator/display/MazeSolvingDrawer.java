package ch.jodo.mazerator.display;

import ch.jodo.mazerator.solver.DijkstraMazeSolverUpdateEvent;
import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MazeSolvingDrawer implements DijkstraMazeSolverUpdateEvent {

    private int waitTime;
    private DrawingUtil du;

    private Color queueColor;
    private Color currentColor;
    private Color solutionColor;

    private Color startCellColor;
    private Color endCellColor;

    public MazeSolvingDrawer(int waitTime, DrawingUtil du, Color queueColor, Color currentColor, Color solutionColor, Color startCellColor, Color endCellColor) {
        this.waitTime = waitTime;
        this.du = du;
        this.queueColor = queueColor;
        this.currentColor = currentColor;
        this.solutionColor = solutionColor;
        this.startCellColor = startCellColor;
        this.endCellColor = endCellColor;
    }

    public void onUpdate(MazeGrid maze, Queue<Cell> queue, Cell current) {
        //Platform.runLater(() -> {

            du.clearCanvas(); // Clear the canvas

            // Highlight all cells in queue
            for (Cell cell : queue) {
                du.drawCell(cell, queueColor);
            }

            // Highlight current cell
            du.drawCell(current, currentColor);

            du.drawMazeGrid(maze);

            try {
                Thread.sleep(waitTime); // Wait
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        //});
    }

    public void onFinish(MazeGrid finishedMaze) {
        List<Cell> solution = new LinkedList<>();

        Cell current = finishedMaze.getEndCell();
        while (current != finishedMaze.getStartCell()) {
            solution.add(current);
            current = current.getPrevious();
        }


        //Platform.runLater(() -> {

            // Draw path back to start
            du.clearCanvas();
            for (Cell cell : solution) {
                du.drawCell(cell, solutionColor);
                du.drawMazeGrid(finishedMaze);

                try {
                    Thread.sleep(waitTime); // Wait
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            du.drawStartAndEndCell(finishedMaze, startCellColor, endCellColor);  // Highlight start and end
            du.drawMazeGrid(finishedMaze);
        //});
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public Color getQueueColor() {
        return queueColor;
    }

    public void setQueueColor(Color queueColor) {
        this.queueColor = queueColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public Color getSolutionColor() {
        return solutionColor;
    }

    public void setSolutionColor(Color solutionColor) {
        this.solutionColor = solutionColor;
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
