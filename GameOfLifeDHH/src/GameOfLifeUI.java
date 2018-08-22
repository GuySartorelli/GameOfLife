import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;


/**
 * CONWAYS GAME OF LIFE
 * 
 * @author DirgleHurbleHerb
 */

public class GameOfLifeUI extends Application {

	private int width = 700, height = 500;
	private int padding = 5;

	private BorderPane layout = new BorderPane();
	private Scene scene = new Scene(layout, width, height);
	private Timeline timeline = new Timeline();

	private HBox optionsBox = new HBox();

	// play /pause button and image fields
	private Button playButton = new Button();
	private Image playIcon = new Image(getClass().getResourceAsStream("play.png"));
	private Image pauseIcon = new Image(getClass().getResourceAsStream("pause.png"));
	private ImageView playView = new ImageView(playIcon);	
	private ImageView pauseView = new ImageView(pauseIcon);		

	// next generation button and image fields
	private Image nextGen = new Image(getClass().getResourceAsStream("forwards.png"));
	private ImageView nextGenView = new ImageView(nextGen);	
	private Button nextGenButton = new Button();

	//rotation button and image fields
	private Image rotate = new Image(getClass().getResourceAsStream("rotate.png"));
	private ImageView rotateView = new ImageView(rotate);
	private Button rotateButton = new Button();

	// Black and white button
	private Image bw = new Image(getClass().getResourceAsStream("contrast.png"));
	private ImageView contrastView = new ImageView(bw);	
	private Button toggleBackGroundButton = new Button();//toggles the background between black and white

	// String for toggling between background white and background black
	private String backgroundColour = "WHITE"; 

	private int cellSize = 20;
	private double minScale = 0.1; 
	private double scale = 1;
	private Group displayBuffer = new Group();
	private Game game = new Game(cellSize);
	private GridBackground grid = new GridBackground(cellSize, minScale);
	private Group scaleOffset = new Group(displayBuffer, grid);

	private Slider zoomSlider;
	private Slider speedSlider;
	private Label zoomLabel = new Label("zoom");
	private Label speedLabel = new Label("speed");	
	private Label patternLabel = new Label("patterns");	
	private Label colourLabel = new Label("colours");	

	private ColorPicker colorPicker = new ColorPicker();
	private ComboBox<String> patternBox = new ComboBox<String>();


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
				"-fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		nextGenButton.setStyle(
				"-fx-font: 8 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		rotateButton.setStyle(
				"-fx-font: 10 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		toggleBackGroundButton.setStyle(
				"-fx-font: 9 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");

		//play button
		playButton.setGraphic(playView);
		playView.setFitHeight(10);
		playView.setFitWidth(10);
		pauseView.setFitHeight(10);
		pauseView.setFitWidth(10);		

		//rotate button
		rotateView.setFitHeight(13);
		rotateView.setFitWidth(13);
		rotateButton.setGraphic(rotateView);

		//next gen button
		nextGenView.setFitHeight(10);
		nextGenView.setFitWidth(10);
		nextGenButton.setGraphic(nextGenView);		

		//contrast button
		contrastView.setFitHeight(10);
		contrastView.setFitWidth(10);
		toggleBackGroundButton.setGraphic(contrastView);				

		playButton.setOnAction(this::doPlay);

		//nextGenButton.setOnAction(this::doPause);
		rotateButton.setOnAction(this::doRestart);

		toggleBackGroundButton.setOnAction(this::doBlackAndWhite);

		//BOTTOM LAYOUT
		//_____________
		optionsBox.setAlignment(Pos.CENTER_LEFT);
		optionsBox.setSpacing(padding);
		optionsBox.setPadding(new Insets(padding, padding, padding, padding));


		GridPane patternPane = new GridPane(); //patterns grid
		optionsBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		patternBox.setItems(FXCollections.observableArrayList(game.getPatternNames()));
		patternBox.getSelectionModel().select("cell");

		GridPane colourPane = new GridPane(); //colours grid

		ComboBox<Map.Entry<String,Paint[]>> colorBox = new ComboBox<Map.Entry<String,Paint[]>>();
		for (Map.Entry<String, Paint[]> entry : Cell.getColorRules().entrySet()) {
			colorBox.getItems().add(entry);
		}

		colorBox.setCellFactory(new Factory());
		colorBox.setConverter(new Convertor());
		//colorBox.addActionListener();
		GridPane sliderPane = new GridPane();

		zoomSlider = new Slider();
		zoomSlider.setMin(minScale);
		zoomSlider.setMax(3);
		zoomSlider.setMajorTickUnit(minScale);
		zoomSlider.setMinorTickCount(0);
		//		zoomSlider.setBlockIncrement(minScale);
		//zoomSlider.setShowTickLabels(true);
		//		zoomSlider.setShowTickMarks(true);
		zoomSlider.setValue(1);
		zoomSlider.valueProperty().addListener(this::doZoom);

		speedSlider = new Slider();

		patternPane.addColumn(0, patternLabel, patternBox);
		colourPane.addColumn(1, colourLabel, colorBox);		
		sliderPane.addColumn(2, zoomLabel, zoomSlider);
		sliderPane.addColumn(3, speedLabel, speedSlider);

		GridPane.setHalignment(zoomLabel, HPos.CENTER);
		GridPane.setHalignment(speedLabel, HPos.CENTER);
		GridPane.setHalignment(patternLabel, HPos.CENTER);
		GridPane.setHalignment(colourLabel, HPos.CENTER);
		optionsBox.getChildren().addAll(patternPane, colourPane, sliderPane, nextGenButton, rotateButton, toggleBackGroundButton, playButton);

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

	/**
	 * Play/ Pause Toggle Button
	 * switches image to pause when playing and playing when paused 
	 * @param act
	 * Action Event
	 */
	private void doPlay(ActionEvent act){
		if (timeline.getStatus() == Animation.Status.RUNNING) {
			timeline.pause();
			playButton.setGraphic(playView);
		} else {
			timeline.play();
			playButton.setGraphic(pauseView);
		}
	}

	private void doBlackAndWhite(ActionEvent act){
		if (backgroundColour.equals("WHITE")) {
			backgroundColour = "BLACK";
			layout.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			optionsBox.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			zoomLabel.setTextFill(Color.WHITE);
			speedLabel.setTextFill(Color.WHITE);
		} else {
			backgroundColour = "WHITE";
			layout.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			optionsBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			zoomLabel.setTextFill(Color.BLACK);
			speedLabel.setTextFill(Color.BLACK);
		}
	}	

	public void doRestart(ActionEvent act) {
		displayBuffer.getChildren().clear();
		timeline.stop();	
		game.restart();


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
			double offsetX = (event.getX()/scale-displayBuffer.getTranslateX());
			double offsetY = (event.getY()/scale-displayBuffer.getTranslateY());
			double snapX = Math.round(offsetX/cellSize) * cellSize;
			double snapY = Math.round(offsetY/cellSize) * cellSize;
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				prevX = offsetX;
				prevY = offsetY;

				game.createPattern(patternBox.getValue(),snapX,snapY);
				displayBuffer.getChildren().clear();
				displayBuffer.getChildren().addAll(game.getCurrentBuffer());	
			}

			else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				double dx = prevX - offsetX;
				double dy = prevY - offsetY;
				scrollGame(dx, dy);
				prevX = offsetX;
				prevY = offsetY;
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

	private class Convertor extends StringConverter<Map.Entry<String, Paint[]>>{

		@Override
		public Map.Entry<String, Paint[]> fromString(String arg0) {
			Map<String, Paint[]> d = new HashMap<String, Paint[]>();
			d.put("TEST", new Paint[] {Color.RED});
			for (Map.Entry<String, Paint[]> d1 : d.entrySet()) {
				return d1;
			}
			return null;
		}

		@Override
		public String toString(Map.Entry<String, Paint[]> object) {
			return object.getKey();
		}

	}

	private class Factory implements Callback<ListView<Map.Entry<String,Paint[]>>, ListCell<Map.Entry<String,Paint[]>>>{

		@Override 
		public ListCell<Map.Entry<String,Paint[]>> call(ListView<Map.Entry<String,Paint[]>> p) {
			return new ListCell<Map.Entry<String,Paint[]>>() {
				private final HBox box;
				{ 
					setContentDisplay(ContentDisplay.LEFT); 
					box = new HBox(5);
				}

				@Override 
				protected void updateItem(Map.Entry<String,Paint[]> item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setGraphic(null);
						setText(null);
					} else {
						for (Paint p : item.getValue()){
							Rectangle r = new Rectangle(15,15,p);
							box.getChildren().add(r);
						}
						setGraphic(box);
						setText(item.getKey());
					}
				}
			};
		}
	}
}
