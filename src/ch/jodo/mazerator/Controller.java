package ch.jodo.mazerator;

import ch.jodo.mazerator.display.DrawingUtil;
import ch.jodo.mazerator.display.MazeGenerationDrawer;
import ch.jodo.mazerator.display.MazeSolvingDrawer;
import ch.jodo.mazerator.generator.RecursiveBacktrackerMazeGenerator;
import ch.jodo.mazerator.solver.DijkstraMazeSolver;
import ch.jodo.mazerator.util.MazeGrid;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

import java.util.Optional;

public class Controller {

	// setting the default colors
	private static final Color COLOR_START_CELL = Color.LIGHTGREEN;
	private static final Color COLOR_END_CELL = Color.LIGHTCORAL;
	private static final Color CURRENT_COLOR = Color.BLUE;

	// Generation colors
	private static final Color VISITED_COLOR = Color.GREEN;
	private static final Color STACK_COLOR = Color.PINK;

	// Solving colors
	private static final Color QUEUE_COLOR = Color.CORNSILK;
	private static final Color SOLUTION_COLOR = Color.LIME;

	private int mazeSize = 10;

	private boolean isWorking = false;

	@FXML
	private Canvas canvas;

	@FXML
	private Button editMazeButton;

	@FXML
	private ColorPicker startColorPicker;
	@FXML
	private ColorPicker endColorPicker;
	@FXML
	private ColorPicker currentColorPicker;

	@FXML
	private ColorPicker visitedColorPicker;
	@FXML
	private ColorPicker stackColorPicker;
	@FXML
	private ColorPicker queueColorPicker;
	@FXML
	private ColorPicker solutionColorPicker;

	@FXML
	private Slider waitTimeSlider;

	private IntegerProperty waitTimeProperty;

	private GraphicsContext gc;
	private DrawingUtil drawingUtil;

	private Optional<MazeGrid> finishedMaze = Optional.empty();

	/***
	 * setting the initial state of the Gui components
	 */
	@FXML
	public void initialize() {
		gc = canvas.getGraphicsContext2D();

		drawingUtil = new DrawingUtil(gc, canvas.getWidth(), canvas.getHeight(), mazeSize);

		startColorPicker.setValue(COLOR_START_CELL);
		endColorPicker.setValue(COLOR_END_CELL);
		currentColorPicker.setValue(CURRENT_COLOR);

		visitedColorPicker.setValue(VISITED_COLOR);
		stackColorPicker.setValue(STACK_COLOR);

		solutionColorPicker.setValue(SOLUTION_COLOR);
		queueColorPicker.setValue(QUEUE_COLOR);

		waitTimeProperty = new SimpleIntegerProperty();
		waitTimeProperty.bindBidirectional(waitTimeSlider.valueProperty());
	}

	/***
	 * creates a new Maze
	 */
	@FXML
	public void createMaze() {
		isWorking = true;
		RecursiveBacktrackerMazeGenerator gen = new RecursiveBacktrackerMazeGenerator();

		MazeGenerationDrawer mgd = new MazeGenerationDrawer(
				waitTimeProperty,
				drawingUtil,
				visitedColorPicker.valueProperty(),
				stackColorPicker.valueProperty(),
				currentColorPicker.valueProperty(),
				startColorPicker.valueProperty(),
				endColorPicker.valueProperty()
		);
		gen.subscribe(mgd); // subscribe with the drawer

		gen.subscribe((finishedMaze) -> { // Subscribe to get the finished maze
			this.finishedMaze = Optional.ofNullable(finishedMaze);
			isWorking = false;
		});

		gen.generateMaze(mazeSize, mazeSize);
	}


	/***
	 * Solves the current maze
	 */
	@FXML
	public void solveMaze() {
		isWorking = true;
		DijkstraMazeSolver solver = new DijkstraMazeSolver();

		MazeSolvingDrawer msd = new MazeSolvingDrawer(
				waitTimeProperty,
				drawingUtil,
				queueColorPicker.valueProperty(),
				currentColorPicker.valueProperty(),
				solutionColorPicker.valueProperty(),
				startColorPicker.valueProperty(),
				endColorPicker.valueProperty()
		);
		solver.subscribe(msd);

		solver.subscribe((mazeGrid) -> {
			isWorking = false;
		});

		finishedMaze.ifPresent(solver::solveMaze);
	}

	/***
	 * If there is a maze present it solves it
	 * and when there is no maze present it generates on
	 */
	@FXML
	private void editMaze() {
		if (isWorking)
			return;

		updateEditMazeButton();
		if (this.finishedMaze.isPresent()) {
			solveMaze();
		} else {
			createMaze();
		}
	}

	/***
	 * clear the canvas and set the text on the edit button
	 */
	@FXML
	private void clearMaze() {
		if (isWorking)
			return;

		this.finishedMaze = Optional.empty();
		updateEditMazeButton();
		editMazeButton.setText("Labyrinth generieren");
		drawingUtil.clearCanvas();
	}

	/***
	 * updates the text on the edit button
	 */
	private void updateEditMazeButton() {
		String newButtonText;
		if (this.finishedMaze.isPresent()) {
			newButtonText = "Labyrinth generieren";
		} else {
			newButtonText = "Labyrinth lösen";
		}
		editMazeButton.setText(newButtonText);
	}
}