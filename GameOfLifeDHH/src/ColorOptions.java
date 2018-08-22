import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ColorOptions extends Parent{//treating ColorOptions object same as a Group object.
	
	private HBox optionsLayout = new HBox(5);
	
	
	public ColorOptions(String option, Color...colors) {
		
		Text optionName = new Text(option+"  ");
		optionsLayout.getChildren().add(optionName);
		this.getChildren().add(optionsLayout);//HBox
		for(Color color: colors) {
			Rectangle colorSquare = new Rectangle(15,15, color);

			optionsLayout.getChildren().add(colorSquare);

		}
		
	}
	public ColorOptions() {
		ColorPicker colorSquare = new ColorPicker();
		this.getChildren().add(colorSquare);
		this.getChildren().add(optionsLayout);
		
	}
	
	
}
