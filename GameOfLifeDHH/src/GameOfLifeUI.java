import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * CONWAYS GAME OF LIFE
 * 
 * @author dirglehurbleherb
 */

public class GameOfLifeUI extends Application {
	private int width = 500, height = 500;

	private Group displayBuffer = new Group();
	private VBox buttonsBox = new VBox(5);
	private Scene scene = new Scene(displayBuffer, width, height);
	private Timeline timeline = new Timeline();
	private Button playButton = new Button("\u25B6");
	private Button pauseButton = new Button("\u23F8");
	private Button stopButton = new Button("\u23F9");

	@Override
	public void start(Stage primaryStage) throws Exception {

		KeyFrame frame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

			}
		});

		// TimelineBuilder.create().cycleCount(javafx.animation.Animation.INDEFINITE).keyFrames(frame).build().play();
		// TimelineBuilder is deprecated, Used Timeline below instead.
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().addAll(frame);
		timeline.setAutoReverse(true);
		// timeline.play();

		buttonsBox.getChildren().add(playButton);
		buttonsBox.getChildren().add(pauseButton);
		buttonsBox.getChildren().add(stopButton);

		buttonsBox.setPadding(new Insets(10, 10, 10, 10));
		buttonsBox.setBackground(Background.EMPTY);
		displayBuffer.getChildren().add(buttonsBox);

		playButton.setStyle(
				"-fx-font: 15 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		pauseButton.setStyle(
				"-fx-font: 12 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		stopButton.setStyle(
				"-fx-font: 10 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");

		playButton.setOnAction(this::doPlay);
		pauseButton.setOnAction(this::doPause);
		stopButton.setOnAction(this::doStop);

		primaryStage.setTitle("Conway's Game of Life");
		primaryStage.setScene(scene);
		primaryStage.show();

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

}
