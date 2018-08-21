import javafx.scene.control.ColorPicker;
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
		colourRuleLifespan();
		//colourRuleNumNeighbours();
		//colourRuleRandom();
	}

	public void colourRuleLifespan() {
		int neighbours = this.game.getNumNeighbours(this.getTranslateX(),this.getTranslateY());
		System.out.println(getTranslateX() + " " + getTranslateY() + " "  + lifespan + " " + neighbours);

		if(lifespan == 1) {
			this.setFill(Color.YELLOW); //Made from green 1.0 and red 1.0, (blue is 0.0).
		} else if (neighbours == 2 || neighbours == 3) {
			Color currentColour = (Color)getFill();
			double red = currentColour.getRed();
			double green = currentColour.getGreen();
			double blue = currentColour.getBlue();
			red -= 0.2;
			if(red < 0 ) {
				red = 0;
				if(red==0) {
					blue+=0.1;
				}
			}
			if(blue>1.0) {
				blue = 1.0;
			}
			if(blue == 1.0) {
				green -= 0.2;
				if(green<0) {
					green = 0;
					red += 0.25; //Note: must be larger than initial red decrement otherwise will not turn purple.
					if(red>1.0) {
						red = 1.0;
					}

				}
			}
			this.setFill(Color.color(red, green, blue));
		} else {
			this.setFill(Color.RED);//NEVER GETS HERE SO POSSIBLY REDUNDANT? NO RED DESPITE VIGOROUS TESTING!!
		}
	}

	/**
	 * Setting the colour based on a selection made with the ColorPicker in the GUI.
	 * @param colorPicker
	 * 
	 */
	public void colourRuleSolid(ColorPicker colorPicker) {
		this.setFill(colorPicker.getValue());
	}

	public void colourRuleNumNeighbours() {
		int neighbours = this.game.getNumNeighbours(this.getTranslateX(),this.getTranslateY());
		//System.out.println(getTranslateX() + " " + getTranslateY() + " "  + lifespan + " " + neighbours);
		this.setFill(Color.YELLOW);
		if(neighbours == 1) {
			this.setFill(Color.YELLOW);
		} else if (neighbours == 2) { 
			this.setFill(Color.DARKORANGE);
		}else if (neighbours == 3){
			this.setFill(Color.RED);
		}
	}

	public void colourRuleRandom(){

		double red = Math.random();
		double green = Math.random();
		double blue = Math.random();
		
		if((red != 0.0 && green !=0.0 && blue != 0.0)||((red != 1.0 && green !=1.0 && blue != 1.0))){
			this.setFill(Color.color(red, green, blue));
		}
	}
	public void update() {
		lifespan++;
		colourRuleLifespan();
		//colourRuleNumNeighbours();
		//colourRuleRandom();
	}
}
