import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ColorOption extends Parent{//treating ColorOptions object same as a Group object.
	
	private HBox optionsLayout = new HBox(5);
	
	
	public ColorOption(String option, Paint...colors) {
		
		Text optionName = new Text(option+"  ");
		optionsLayout.getChildren().add(optionName);
		this.getChildren().add(optionsLayout);//HBox
		for(Paint color: colors) {
			Rectangle colorSquare = new Rectangle(15,15, color);

			optionsLayout.getChildren().add(colorSquare);

		}
		
	}
	public ColorOption(String option) {
		ColorPicker colorSquare = new ColorPicker();
		optionsLayout.getChildren().add(colorSquare);
		this.getChildren().add(optionsLayout);
		
	}
	
	
}
