import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * CONWAYS GAME OF LIFE
 * 
 * @author dirglehurbleherb
 */

public class GameOfLifeUI extends Application {
	private int width = 500, height = 500;

	private Group mainGrp = new Group();
	private VBox buttonsBox = new VBox(5);
	private Scene scene = new Scene(mainGrp, width, height);
	private Timeline timeline = new Timeline();
	private Button playButton = new Button("\u25B6");
	private Button pauseButton = new Button("\u23F8");
	private Button stopButton = new Button("\u23F9");

	private Group displayBuffer = new Group();
	private Game game = new Game(20);

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//TIMELINE
		//________________
		KeyFrame frame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				game.update();
				displayBuffer.getChildren().clear();
				displayBuffer.getChildren().addAll(game.getCurrentBuffer());
			}
		});

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(frame);
		timeline.setAutoReverse(true);//what does this do?
		
		//SCROLLING
		//____________________
		MouseDragger dragger = new MouseDragger();
		scene.setOnMousePressed(dragger);
		scene.setOnMouseDragged(dragger);
		scene.setOnScroll(this::doMouseScroll);
		scene.setOnKeyPressed(this::doKeyPress);

		//LAYOUT
		//____________________
		mainGrp.getChildren().add(displayBuffer);
		buttonsBox.getChildren().add(playButton);
		buttonsBox.getChildren().add(pauseButton);
		buttonsBox.getChildren().add(stopButton);

		buttonsBox.setPadding(new Insets(10, 10, 10, 10));
		buttonsBox.setBackground(Background.EMPTY);
		mainGrp.getChildren().add(buttonsBox);

		playButton.setStyle(
				"-fx-font: 15 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		pauseButton.setStyle(
				"-fx-font: 12 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		stopButton.setStyle(
				"-fx-font: 10 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");

		playButton.setOnAction(this::doPlay);
		pauseButton.setOnAction(this::doPause);
		stopButton.setOnAction(this::doStop);
		
		displayBuffer.getChildren().addAll(game.getCurrentBuffer());
		displayBuffer.setTranslateX(width*0.5);
		displayBuffer.setTranslateY(height*0.5);

		primaryStage.setTitle("Conway's Game of Life");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public void doMouseScroll(ScrollEvent event) {
		displayBuffer.setTranslateX(displayBuffer.getTranslateX() + event.getDeltaX());
		displayBuffer.setTranslateY(displayBuffer.getTranslateY() + event.getDeltaY());
	}
	
	public void doKeyPress(KeyEvent event) {
		if (event.getCode() == KeyCode.DOWN) {
			displayBuffer.setTranslateY(displayBuffer.getTranslateY() - 20);
		}
		if (event.getCode() == KeyCode.UP) {
			displayBuffer.setTranslateY(displayBuffer.getTranslateY() + 20);
		}
		if (event.getCode() == KeyCode.LEFT) {
			displayBuffer.setTranslateX(displayBuffer.getTranslateX() + 20);
		}
		if (event.getCode() == KeyCode.RIGHT) {
			displayBuffer.setTranslateX(displayBuffer.getTranslateX() - 20);
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
		// TODO Auto-generated method stub
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
				double distanceX = prevX - x;
				double distanceY = prevY - y;
				displayBuffer.setTranslateX(displayBuffer.getTranslateX() + distanceX);
				displayBuffer.setTranslateY(displayBuffer.getTranslateY() + distanceY);
				prevX = x;
				prevY = y;
			}
		}
	}
}
