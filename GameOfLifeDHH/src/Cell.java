import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
/**
 * Class to store info about the cell object and extends Rectangle
 * @author saadheba
 *
 */
public class Cell extends Rectangle {

	private int lifespan = 1;
	private Game game;

	/**
	 * Constructor for the cell class
	 * @param cellSize the width and height of the cell
	 * @param x for x pos
	 * @param y for y pos
	 */
	public Cell(Game game, int cellSize, double x, double y) {
		super(cellSize, cellSize);
		setTranslateX(x);
		setTranslateY(y);
		this.game = game;

		setStroke(Color.WHITE);
		setStrokeType(StrokeType.INSIDE);
		setStrokeWidth(cellSize*0.05);
		colourRuleLifeSpan();
	}

	public void colourRuleLifeSpan() {
		int neighbours = this.game.getNumNeighbours(this.getTranslateX(),this.getTranslateY());
		System.out.println(getTranslateX() + " " + getTranslateY() + " "  + lifespan + " " + neighbours);
		
		if(lifespan == 1) {
			this.setFill(Color.YELLOW);
		} else if (neighbours == 2 || neighbours == 3) {
			System.out.println("ALIVE STILL YO " + lifespan);
			Color currentColour = (Color)getFill();
			double minusRed = currentColour.getRed();
			double minusGreen = currentColour.getGreen();
			double plusBlue = currentColour.getBlue();
			minusRed -= 0.1;
			plusBlue += 0.1;
			if(minusRed < 0 ) {
				minusRed = 0;
			}
			if(plusBlue>1.0) {
				plusBlue = 1.0;
			}
			if(plusBlue == 1.0) {
				minusGreen -= 0.1;
				if(minusGreen<0) {
					minusGreen = 0;
				}
			}
			this.setFill(Color.color(minusRed, minusGreen, plusBlue));
		} else {
			System.out.println("GONNA DIE YO " + lifespan);
			this.setFill(Color.RED);
		}

	}
	public void update() {
		lifespan++;
		colourRuleLifeSpan();
	}
}
