import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GridBackground extends Parent {
	private int cellSize;
	private double dx;
	private double dy;
	private double scale = 1;
	private double lineWidth = 1;
	private boolean adjustLineWidthToScale = false;
	
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
		double x = cellSize + dx;
		while (x < width) {
			//set linewidth if needed
			Line line = new Line(x, -cellSize, x, height);
			if (!adjustLineWidthToScale) {
				line.setStrokeWidth(lineWidth / scale);
			} else {
				line.setStrokeWidth(lineWidth);
			}
			
			buffer.add(line);
			x+= cellSize;
		}
		//Horizontal lines
		double y = cellSize + dy;
		while (y < height) {
			Line line = new Line(-cellSize, y, width, y);
			if (!adjustLineWidthToScale) {
				line.setStrokeWidth(lineWidth / scale);
			} else {
				line.setStrokeWidth(lineWidth);
			}
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
	
	public void adjustLineWidthToScale(boolean value) {
		adjustLineWidthToScale = value;
	}
	
	/**
	 * Scales the grid whilst maintaining a consistent lineWidth
	 * @param scaleBy
	 */
	public void scale(double scaleBy) {
		if (scale != scaleBy) {
			scale = scaleBy;
			construct();
		}
	}
}
