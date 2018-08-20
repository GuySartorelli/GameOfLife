import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GridBackground extends Parent {
	private int cellSize;
	private double dx;
	private double dy;
	private final int LINE_WIDTH = 1;
	
	public GridBackground(int cellSize) {
		super();
		this.cellSize = cellSize;
	}
	
	/**
	 * Constructs the grid based on cellSize and scene dimensions
	 */
	public void construct() {
		List<Line> buffer = new ArrayList<Line>();
		double width = getScene().getWidth();
		double height = getScene().getHeight();
		//Vertical lines
		double x = cellSize*0.5 + dx;
		while (x < width) {
			Line line = new Line(x, 0, x, height);
			line.setStroke(Color.BLACK);
			line.setStrokeWidth(LINE_WIDTH);
			buffer.add(line);
			x+= cellSize;
		}
		//Horizontal lines
		double y = cellSize*0.5 + dy;
		while (y < height) {
			Line line = new Line(0, y, width, y);
			line.setStroke(Color.BLACK);
			line.setStrokeWidth(LINE_WIDTH);
			buffer.add(line);
			y+= cellSize;
		}
		getChildren().clear();
		getChildren().addAll(buffer);
	}
	
	/** 
	 * Scrolls the grid by the specified change in x and y
	 * @param x
	 * @param y
	 */
	public void scroll(double dx, double dy) {
		this.dx = (this.dx + dx) % cellSize;
		this.dy = (this.dy + dy) % cellSize;
		construct();
	}
	
	/**
	 * Scales the grid whilst maintaining a consistent lineWidth
	 * @param scaleBy
	 */
	public void scale(double scaleBy) {
		
	}
}
