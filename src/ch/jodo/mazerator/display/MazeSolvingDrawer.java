package ch.jodo.mazerator.display;

import ch.jodo.mazerator.solver.DijkstraMazeSolverEvent;
import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MazeSolvingDrawer implements DijkstraMazeSolverEvent {

	private IntegerProperty waitTime;
	private DrawingUtil du;

	private ObjectProperty<Color> queueColor;
	private ObjectProperty<Color> currentColor;
	private ObjectProperty<Color> solutionColor;

	private ObjectProperty<Color> startCellColor;
	private ObjectProperty<Color> endCellColor;

	public MazeSolvingDrawer(IntegerProperty waitTime, DrawingUtil du, ObjectProperty<Color> queueColor, ObjectProperty<Color> currentColor, ObjectProperty<Color> solutionColor, ObjectProperty<Color> startCellColor, ObjectProperty<Color> endCellColor) {
		this.waitTime = waitTime;
		this.du = du;
		this.queueColor = queueColor;
		this.currentColor = currentColor;
		this.solutionColor = solutionColor;
		this.startCellColor = startCellColor;
		this.endCellColor = endCellColor;
	}

	public void onUpdate(MazeGrid maze, Queue<Cell> queue, Cell current) {

		du.clearCanvas(); // Clear the canvas

		// Highlight all cells in queue
		for (Cell cell : queue) {
			du.drawCell(cell, queueColor.get());
		}

		// Highlight current cell
		du.drawCell(current, currentColor.get());

		du.drawMazeGrid(maze);

		try {
			Thread.sleep(waitTime.get()); // Wait
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void onFinish(MazeGrid finishedMaze) {
		List<Cell> solution = new LinkedList<>();

		Cell current = finishedMaze.getEndCell();
		while (current != finishedMaze.getStartCell()) {
			solution.add(current);
			current = current.getPrevious();
		}


		// Draw path back to start
		du.clearCanvas();
		for (Cell cell : solution) {
			du.drawCell(cell, solutionColor.get());
			du.drawMazeGrid(finishedMaze);

			try {
				Thread.sleep(waitTime.get()); // Wait
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		du.drawStartAndEndCell(finishedMaze, startCellColor.get(), endCellColor.get());  // Highlight start and end
		du.drawMazeGrid(finishedMaze);
	}

	public void setWaitTime(IntegerProperty waitTime) {
		this.waitTime = waitTime;
	}

	public ObjectProperty<Color> getQueueColor() {
		return queueColor;
	}

	public void setQueueColor(ObjectProperty<Color> queueColor) {
		this.queueColor = queueColor;
	}

	public ObjectProperty<Color> getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(ObjectProperty<Color> currentColor) {
		this.currentColor = currentColor;
	}

	public ObjectProperty<Color> getSolutionColor() {
		return solutionColor;
	}

	public void setSolutionColor(ObjectProperty<Color> solutionColor) {
		this.solutionColor = solutionColor;
	}

	public ObjectProperty<Color> getStartCellColor() {
		return startCellColor;
	}

	public void setStartCellColor(ObjectProperty<Color> startCellColor) {
		this.startCellColor = startCellColor;
	}

	public ObjectProperty<Color> getEndCellColor() {
		return endCellColor;
	}

	public void setEndCellColor(ObjectProperty<Color> endCellColor) {
		this.endCellColor = endCellColor;
	}
}
