import java.util.List;

//This is the game class.

public class Game {
	private List<Cell> currentBuffer;
	private List<Cell> backBuffer;
	private int cellSize;
//	private Map<String, List<Cell>> patterns; --for using patterns in future
	
	/* Constructor */
	public Game(int cellSize) {
		this.cellSize = cellSize;
	}

	/** Getter for currentBuffer */
	public List<Cell> getCurrentBuffer() {
		return currentBuffer;
	}
	
	/** Update Method */
	public void update() {
		
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
	public void createCell() {
		
	}
	
	/** places cells randomly
	 * possibly replaced later with specific patterns */
	public void placeRandom() {
		
	}
	
	/** places cells with defined initial patterns */
	public void defineInitialPattern() {
		
	}
	
	/** Will take pattern templates from file and parse them to map
	 * to do --if there is enough time
	 *  */
//	private void parsePatterns() {
//		
//	}
	
}
