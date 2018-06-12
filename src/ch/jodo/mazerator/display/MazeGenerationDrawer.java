package ch.jodo.mazerator.display;

import ch.jodo.mazerator.generator.MazeGeneratorEvent;
import ch.jodo.mazerator.util.Cell;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;

import java.util.Stack;

public class MazeGenerationDrawer implements MazeGeneratorEvent {

	private IntegerProperty waitTime;
	private DrawingUtil du;

	private ObjectProperty<Color> visitedColor;
	private ObjectProperty<Color> stackColor;
	private ObjectProperty<Color> currentCellColor;

	private ObjectProperty<Color> startCellColor;
	private ObjectProperty<Color> endCellColor;

	public MazeGenerationDrawer(final IntegerProperty waitTime, final DrawingUtil drawingUtil, final ObjectProperty<Color> visitedColor, final ObjectProperty<Color> stackColor,
	                            final ObjectProperty<Color> currentCellColor, final ObjectProperty<Color> startCellColor, final ObjectProperty<Color> endCellColor) {
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
				du.drawCell(cell, visitedColor.get());
			}
		}

		// Go through all cells in stack and highlight them (overwrites the visited highlight)
		for (Cell cell : stack) {
			du.drawCell(cell, stackColor.get());
		}

		// Highlight current cell
		du.drawCell(current, currentCellColor.get());

		// Draw the grid over the canvas
		du.drawMazeGrid(maze);

		try {
			Thread.sleep(waitTime.get()); // Wait
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

		du.drawStartAndEndCell(finishedMaze, startCellColor.get(), endCellColor.get());  // Highlight start and end

		du.drawMazeGrid(finishedMaze); // Draw the finished maze
		//});
	}


	public void setWaitTime(IntegerProperty waitTime) {
		this.waitTime = waitTime;
	}

	public ObjectProperty<Color> getVisitedColor() {
		return visitedColor;
	}

	public void setVisitedColor(ObjectProperty<Color> visitedColor) {
		this.visitedColor = visitedColor;
	}

	public ObjectProperty<Color> getStackColor() {
		return stackColor;
	}

	public void setStackColor(ObjectProperty<Color> stackColor) {
		this.stackColor = stackColor;
	}

	public ObjectProperty<Color> getCurrentCellColor() {
		return currentCellColor;
	}

	public void setCurrentCellColor(ObjectProperty<Color> currentCellColor) {
		this.currentCellColor = currentCellColor;
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
