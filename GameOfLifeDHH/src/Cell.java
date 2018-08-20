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

}
