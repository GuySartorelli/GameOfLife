import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Label;
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
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * CONWAYS GAME OF LIFE
 * 
 * @author dirglehurbleherb
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
	private double minScale = 0.5; 
	private Group displayBuffer = new Group();
	private Game game = new Game(cellSize);
	private GridBackground grid = new GridBackground(cellSize, minScale);
	private Group scaleOffset = new Group(displayBuffer, grid);
	
	private Slider zoomSlider;
	private Slider speedSlider;
	private Label zoomLabel = new Label("zoom");
	private Label speedLabel = new Label("speed");	
	
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
		rotateButton.setOnAction(this::doStop);
		
		toggleBackGroundButton.setOnAction(this::doBlackAndWhite);
		
		//BOTTOM LAYOUT
		//_____________
		optionsBox.setAlignment(Pos.CENTER_LEFT);
		optionsBox.setSpacing(padding);
		optionsBox.setPadding(new Insets(padding, padding, padding, padding));
		optionsBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		ComboBox<String> patternBox = new ComboBox<String>(FXCollections.observableArrayList(game.getPatternNames()));
		patternBox.getSelectionModel().select("cell");
		ComboBox<Node> colorBox = new ComboBox<Node>(FXCollections.observableArrayList(Cell.getColorRules()));
		colorBox.getSelectionModel().select(Cell.getDefaultColorRule());
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

		sliderPane.addColumn(0, zoomSlider, zoomLabel);
		sliderPane.addColumn(1, speedSlider, speedLabel);
		GridPane.setHalignment(zoomLabel, HPos.CENTER);
		GridPane.setHalignment(speedLabel, HPos.CENTER);
		optionsBox.getChildren().addAll(patternBox, colorBox, sliderPane, nextGenButton, rotateButton, toggleBackGroundButton, playButton);
		
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
