import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Cell extends Rectangle {
	
	public Cell(int cellSize, long x, long y) {
		super(x, y, cellSize, cellSize);
		// if enough time add colour to cell

		setStroke(Color.WHITE);
		setStrokeType(StrokeType.INSIDE);
		setStrokeWidth(cellSize*0.05);
	}
	
	/**
	 * THIS NEEDS TO BE CHANGED LATER TO THE ACTUAL RULES
	 * @return
	 */
	public static List<String> getColorRules() {
		List<String> colorRules = new ArrayList<String>();
		colorRules.add("Bog");
		colorRules.add("Beg");
		colorRules.add("Bug");
		colorRules.add("Bag");
		return colorRules;
		
	}

}
