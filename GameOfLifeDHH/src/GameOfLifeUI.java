import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * CONWAYS GAME OF LIFE
 * 
 * @author dirglehurbleherb
 */

public class GameOfLifeUI extends Application {
	private int width = 500, height = 500;
	private int padding = 5;
	
	private BorderPane layout = new BorderPane();
	private Scene scene = new Scene(layout, width, height);
	private Timeline timeline = new Timeline();
	private Button playButton = new Button("\u25B6");
	private Button pauseButton = new Button("\u23F8");
	private Button stopButton = new Button("\u23F9");
	private Button backGround = new Button("b/w");//toggles the background between black and white
	private ComboBox<String> patternBox = new ComboBox<String>();
	
	private int cellSize = 20;
	private Group displayBuffer = new Group();
	private Game game = new Game(cellSize);
	private GridBackground grid = new GridBackground(cellSize);
	private Group scaleOffset = new Group(displayBuffer, grid);
	
	private Slider zoomSlider;
	private ColorPicker colorPicker = new ColorPicker();
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//TIMELINE
		//________________
		KeyFrame frame = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				game.update();
				displayBuffer.getChildren().clear();
				displayBuffer.getChildren().addAll(game.getCurrentBuffer());
			}
		});

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(frame);
		//timeline.setAutoReverse(true);//what does this do?
		
		//SCROLLING
		//____________________
		MouseListener listener = new MouseListener();
		scene.setOnMousePressed(listener);
		scene.setOnMouseDragged(listener);
		scene.setOnMouseMoved(listener);
		scene.setOnScroll(this::doMouseScroll);
		scene.setOnKeyPressed(this::doKeyPress);

		//COLORPICKER
		//____________________
	
		
		
		//LAYOUT
		//____________________
		layout.getChildren().add(scaleOffset);

		playButton.setStyle(
				"-fx-font: 15 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		pauseButton.setStyle(
				"-fx-font: 12 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		stopButton.setStyle(
				"-fx-font: 10 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		backGround.setStyle(
				"-fx-font: 9 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		
		playButton.setOnAction(this::doPlay);
		pauseButton.setOnAction(this::doPause);
		stopButton.setOnAction(this::doStop);
		
		//BOTTOM LAYOUT
		//_____________
		HBox optionsBox = new HBox();
		optionsBox.setAlignment(Pos.CENTER);
		optionsBox.setSpacing(padding);
		optionsBox.setPadding(new Insets(padding, padding, padding, padding));
		optionsBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		patternBox.setItems(FXCollections.observableArrayList(game.getPatternNames()));
		patternBox.getSelectionModel().select("cell");
		ComboBox<String> colorBox = new ComboBox<String>(FXCollections.observableArrayList(Cell.getColorRules()));
//		colorBox.getSelectionModel().select(THE_DEFAULT);
		GridPane sliderPane = new GridPane();
		zoomSlider = new Slider();
		zoomSlider.setMin(0.5);
		zoomSlider.setMax(3);
		zoomSlider.setMajorTickUnit(0.5);
		zoomSlider.setMinorTickCount(0);
		zoomSlider.setBlockIncrement(0.5);
//		zoomSlider.setSnapToTicks(true);
		zoomSlider.setShowTickLabels(true);
//		zoomSlider.setShowTickMarks(true);
		zoomSlider.setValue(1);
		zoomSlider.valueProperty().addListener(this::doZoom);
		Label zoomLabel = new Label("zoom");
		Slider speedSlider = new Slider();
		Label speedLabel = new Label("speed");
		sliderPane.addColumn(0, zoomSlider, zoomLabel);
		sliderPane.addColumn(1, speedSlider, speedLabel);
		GridPane.setHalignment(zoomLabel, HPos.CENTER);
		GridPane.setHalignment(speedLabel, HPos.CENTER);
		optionsBox.getChildren().addAll(patternBox, colorBox, sliderPane, playButton, pauseButton, stopButton);
		layout.setBottom(optionsBox);
		
		
		displayBuffer.getChildren().addAll(game.getCurrentBuffer());
		scrollGame(width*0.5, height*0.5);

		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> grid.construct());
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> grid.construct());
		primaryStage.setTitle("Conway's Game of Life");
		primaryStage.setScene(scene);
		primaryStage.show();
		grid.construct(); //must be after stage is shown
	}
	
	public void doZoom(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
		double scaleVal = Math.round(newVal.doubleValue() * 2.0) * 0.5;
		scaleVal = newVal.doubleValue();
		scaleOffset.setScaleX(scaleVal);
		scaleOffset.setScaleY(scaleVal);
		grid.scale(scaleVal);
		//zoomSlider.setValue(scaleVal);
	}
	
	public void scrollGame(double dx, double dy) {
		displayBuffer.setTranslateX(displayBuffer.getTranslateX() + dx);
		displayBuffer.setTranslateY(displayBuffer.getTranslateY() + dy);
		grid.scroll(dx, dy);
	}
	
	public void doMouseScroll(ScrollEvent event) {
		double dx = event.getDeltaX();
		double dy = event.getDeltaY();
		scrollGame(dx, dy);
	}
	
	public void doKeyPress(KeyEvent event) {
		if (event.getCode() == KeyCode.DOWN) {
			scrollGame(0, -20);
		}
		if (event.getCode() == KeyCode.UP) {
			scrollGame(0, 20);
		}
		if (event.getCode() == KeyCode.LEFT) {
			scrollGame(20, 0);
		}
		if (event.getCode() == KeyCode.RIGHT) {
			scrollGame(-20, 0);
		}
	}

	public void doPlay(ActionEvent act) {
		timeline.play();
	}

	public void doPause(ActionEvent act) {
		timeline.pause();
	}

	public void doStop(ActionEvent act) {
		timeline.stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch();
	}

	
	/**
	 * Handles mouse dragging for infinite scrolling
	 */
	private class MouseListener implements EventHandler<MouseEvent>{
		private double prevX;
		private double prevY;
		
		@Override
		public void handle(MouseEvent event) {
			double x = event.getX()-displayBuffer.getTranslateX();
			double y = event.getY()-displayBuffer.getTranslateY();
			double snapX = Math.round(x/cellSize) * cellSize;
			double snapY = Math.round(y/cellSize) * cellSize;
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				System.out.println("mouse pressed");
				prevX = x;
				prevY = y;
						
				game.createPattern(patternBox.getValue(),snapX,snapY);
				displayBuffer.getChildren().clear();
				displayBuffer.getChildren().addAll(game.getCurrentBuffer());	
			}
			
			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				double dx = prevX - x;
				double dy = prevY - y;
				scrollGame(dx, dy);
				prevX = x;
				prevY = y;
			}
			/* Method for tracking the mouse with an outline of the pattern about to be placed,
			   and then once clicked add the pattern to the view*/
			if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
				
				displayBuffer.getChildren().clear();
				displayBuffer.getChildren().addAll(game.getCurrentBuffer());
				createTemporaryPattern(patternBox.getValue(),snapX,snapY);
			}
		}
	}
	
	/** Creates temporary cell that follows the mouse*/
	public void createTemporaryCell(double x, double y) {
		Cell cell = new Cell(game, cellSize, x, y);
		displayBuffer.getChildren().add(cell);
	}
	
	/** Creates temporary cell pattern that follows the mouse*/
	public void createTemporaryPattern(String patternKey,double mouseX,double mouseY) {

		List<int[]> pattern = game.returnPattern(patternKey);
		for (int[] position : pattern) {
			double x = mouseX + position[0]*cellSize;
			double y = mouseY + position[1]*cellSize;
			createTemporaryCell(x,y);
		}
	}
	
	/**
	 * Adding a ColorPicker for the colourRuleSolid(ColorPicker colorPicker) method under cell.
	 * @author shawbeva
	 *
	 */
//	public class ColorPickerSample extends Application {    
//	   
//	    @Override
//	    public void start(Stage stage) {
//	      
//	        colorPicker.setOnAction(new EventHandler() {
//	            public void handle(Event t) {
//	                  
//	            }
//	        });
//	 
//	        //box.getChildren().addAll(colorPicker);
//	    }
//	}
}
