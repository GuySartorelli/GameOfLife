import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
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
import javafx.scene.paint.Color;
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


	private int cellSize = 20;
	private double minScale = 0.5; 
	private Group displayBuffer = new Group();
	private Game game = new Game(cellSize);
	private GridBackground grid = new GridBackground(cellSize, minScale);
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
		MouseDragger dragger = new MouseDragger();
		scene.setOnMousePressed(dragger);
		scene.setOnMouseDragged(dragger);
		scene.setOnScroll(this::doMouseScroll);
		scene.setOnKeyPressed(this::doKeyPress);


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
		
		ComboBox<String> patternBox = new ComboBox<String>(FXCollections.observableArrayList(game.getPatternNames()));
		patternBox.getSelectionModel().select("cell");
		
		ComboBox<ColorOption> colorBox = new ComboBox<ColorOption>(FXCollections.observableArrayList(Cell.getColorRules()));
		colorBox.getSelectionModel().select(Cell.getDefaultColorRule());
		
		GridPane sliderPane = new GridPane();
		zoomSlider = new Slider();
		zoomSlider.setMin(minScale);
		zoomSlider.setMax(3);
		zoomSlider.setMajorTickUnit(minScale);
		zoomSlider.setMinorTickCount(0);
		//		zoomSlider.setBlockIncrement(minScale);
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

		//COLORPICKER
		//____________________
		
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
	private class MouseDragger implements EventHandler<MouseEvent>{
		private double prevX;
		private double prevY;

		@Override
		public void handle(MouseEvent event) {
			double x = event.getX();
			double y = event.getY();
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				prevX = x;
				prevY = y;
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				double dx = prevX - x;
				double dy = prevY - y;
				scrollGame(dx, dy);
				prevX = x;
				prevY = y;
			}
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
