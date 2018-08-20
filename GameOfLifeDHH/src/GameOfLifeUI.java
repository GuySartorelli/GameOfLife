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
 *  CONWAYS GAME OF LIFE
 *
 */

public class GameOfLifeUI extends Application {
	private int width = 500, height = 500;

	private Group root = new Group();
	private VBox pane = new VBox(5);
	private Scene scene = new Scene(root, width, height);
	private Button playButton = new Button("\u25B6");
	private Button pauseButton = new Button("\u23F8");
	private Button stopButton = new Button(	"\u23F9");

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stu
	
		KeyFrame frame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
							
										
					
				
			}
		});
		
		//TimelineBuilder.create().cycleCount(javafx.animation.Animation.INDEFINITE).keyFrames(frame).build().play();
		// TimelineBuilder is deprecated, Used Timeline below instead.
		Timeline timeline = new Timeline(); 
		timeline.setCycleCount(Timeline.INDEFINITE); 
		timeline.getKeyFrames().addAll(frame); 
		timeline.setAutoReverse(true);
//		timeline.play();

		pane.getChildren().add(playButton);
		pane.getChildren().add(pauseButton);
		pane.getChildren().add(stopButton);

		pane.setPadding(new Insets(10, 10, 10, 10));
		pane.setBackground(Background.EMPTY);
		root.getChildren().add(pane);
		
		playButton.setStyle("-fx-font: 15 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		pauseButton.setStyle("-fx-font: 12 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		stopButton.setStyle("-fx-font: 10 arial; -fx-base: #353535;-fx-text-fill: white; -fx-pref-width: 28px; -fx-pref-height: 28px;");
		
		playButton(timeline);
		pauseButton(timeline);
		stopButton(timeline);	
		
		primaryStage.setTitle("Bug World");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}			
	
	public void playButton(Timeline timeline) {
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				timeline.play();
			}
		});
	}

	public void pauseButton(Timeline timeline) {
		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				timeline.pause();
			}
		});
	}
	
	public void stopButton(Timeline timeline) {
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				timeline.stop();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	}

}
