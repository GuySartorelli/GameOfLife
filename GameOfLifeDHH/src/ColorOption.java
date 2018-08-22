import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ColorOption {//treating ColorOptions object same as a Group object.
	
	private HBox optionsLayout = new HBox(5);
	private String option;
	
	public ColorOption(String option, Paint...colors) {
		
		this.option= option;
		Text optionName = new Text(option+"  ");
		optionsLayout.getChildren().add(optionName);
		//this.getChildren().add(optionsLayout);//HBox
//		for(Paint color: colors) {
//			Rectangle colorSquare = new Rectangle(15,15, color);
//
//			optionsLayout.getChildren().add(colorSquare);
//
//		}
		
	}
	public ColorOption(String option) {
		this.option= option;
		ColorPicker colorSquare = new ColorPicker();
		optionsLayout.getChildren().add(colorSquare);
	//	this.getChildren().add(optionsLayout);
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null || !(obj instanceof ColorOption)) {
			return false;
		}
		ColorOption other = (ColorOption) obj;
		
		return option.equals(other.option);
	}
	
	
}
