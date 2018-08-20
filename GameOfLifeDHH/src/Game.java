import java.io.IOException;
import java.util.ArrayList;
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
		
		//Test getDeadNeighbours method.
		Cell testCell = new Cell(cellSize,100,100);
		getDeadNeighbours(testCell);
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
		// find a live cell in the currentBuffer
		// then find how many live neighbour cells we have 
		int totalNeighbours = 0;
		for(long[] n : currentBuffer.keySet()) {
			
			Cell c = currentBuffer.get(n);
			double x = c.getX();
			double y = c.getY();
			
			if(x + cellSize > 0) {
				totalNeighbours++;
			}
			
		}
		
		return totalNeighbours; // to be changed
		
	}	
	
	/** Checks all cells, by getting neighbours and num of neighbours
	 * recursion magic happens here */
	public void checkCells() {
		
	}

	/** Returns a list of dead neighbours in arrays */
	public List<long[]> getDeadNeighbours(Cell cell){

		//Search currentBuffer map using cell position as key.
		List<long[]> deadNeighbours = new ArrayList<long[]>();
		long cellX = (long)cell.getX();
		long cellY = (long)cell.getY();
		long[] key = {cellX,cellY};
		
		//Check the neighbour to the left.
		key[0] -= cellSize;
		if (!currentBuffer.containsKey(key)) {
			long[] position = {key[0],key[1]};
			deadNeighbours.add(position);
		}
		//Check the 3 neighbours above from left to right.
		key[1] -= cellSize;
		for (int i = 0; i < 3; i++) {
			if (!currentBuffer.containsKey(key)) {
				long[] position = {key[0],key[1]};
				deadNeighbours.add(position);
			}
			if (i != 2) {
				key[0] += cellSize;
			}
		}
		//Check the neighbour to the right.
		key[1] += cellSize;
		if (!currentBuffer.containsKey(key)) {
			long[] position = {key[0],key[1]};
			deadNeighbours.add(position);
		}
		//Check the 3 neighbours below from right to left.
		key[1] += cellSize;
		for (int i = 0; i < 3; i++) {
			if (!currentBuffer.containsKey(key)) {
				long[] position = {key[0],key[1]};
				deadNeighbours.add(position);
			}
			key[0] -= cellSize;
		}
		
//		//Testing:
//		for (long[] testCell: deadNeighbours) {
//			System.out.println(testCell[0]+","+testCell[1]);
//		}
		
		return deadNeighbours;
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
