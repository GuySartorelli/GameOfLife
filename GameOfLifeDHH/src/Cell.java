import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
/**
 * Class to store info about the cell object and extends Rectangle
 * @author saadheba
 *
 */
public class Cell extends Rectangle {
	/**
	 * Constaucter for the cell class
	 * @param cellSize the width and height of the cell
	 * @param x for x pos
	 * @param y for y pos
	 */
	public Cell(int cellSize, long x, long y) {
		super(x, y, cellSize, cellSize);
	

		setStroke(Color.WHITE);
		setStrokeType(StrokeType.INSIDE);
		setStrokeWidth(cellSize*0.05);
	}

}
