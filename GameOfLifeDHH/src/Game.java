import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This is the game class.

public class Game {
	private Map<long[], Cell> currentBuffer = new HashMap<long[], Cell>();
	private Map<long[], Cell> backBuffer = new HashMap<long[], Cell>();
	private int cellSize;
	private Map<String, List<int[]>> patterns;
	
	/* Constructor */
	public Game(int cellSize) {
		this.cellSize = cellSize;
		parsePatterns();
		defineInitialPattern();
	}

	/** Getter for currentBuffer */
	public Collection<Cell> getCurrentBuffer() {
		return currentBuffer.values();
	}
	
	/** Update Method */
	public void update() {
		swapBuffers();
		defineInitialPattern();//remove for production
	}
	
	public void swapBuffers() {
		currentBuffer = backBuffer;
		backBuffer.clear();
	}
	
	/** Get Number of neighbours Method */
	public int getNumNeighbours() {
		return 0; // to be changed
	}	
	
	/** Checks all cells, by getting neighbours and num of neighbours
	 * recursion magic happens here */
	public void checkCells() {
		
	}

	/** Returns a list of dead neighbours in arrays */
	public List<long[]> getDeadNeighbours(){
		return null;
	}
	
	/** creates cell */
	public void createCell(long x, long y) {
		currentBuffer.put(new long[] {x,y}, new Cell(cellSize, x, y));
	}
	
	/** places cells randomly
	 * possibly replaced later with specific patterns */
	public void placeRandom() {
		
	}
	
	/** places cells with defined initial patterns */
	public void defineInitialPattern() {
		//NOTE: Decide whether it should be random or not
		int patternIndex = (int)(Math.random() * patterns.size());
		List<int[]> pattern = (List<int[]>) patterns.values().toArray()[patternIndex];
		for (int[] position : pattern) {
			createCell(position[0]*cellSize, position[1]*cellSize);
		}
	}
	
	/** Takes pattern templates from file and parse them to map
	 *  */
	private void parsePatterns() {
		try {
			PatternParser parser = new PatternParser(getClass().getResourceAsStream("/patterns.gol"));
			patterns = parser.getContents();
		} catch(IOException e) {System.out.println(e);}
		
	}
	
}
