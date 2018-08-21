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
		getDeadNeighbours(100,100);
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
		
		//Iterate through the currentBuffer map to check each alive cell.
		//Once checked, the position of the dead neighbour is added to the checkedDead list.
		List<long[]> checkedDead = new ArrayList<long[]>();
		for (long[] cellPos: currentBuffer.keySet()) {
			//Get the list of dead neighbours around the cell.
			List<long[]> deadNeighbours = getDeadNeighbours(cellPos[0],cellPos[1]);
			//Iterate through the cell's deadNeighbours list to check whether it should be born.
			for (long[] deadCellPos: deadNeighbours) {
//				if (!checkedDead.contains(deadCellPos) && getNumNeighbours(deadCellPos[0],deadCellPos[1])==3){
//					//Cell is born.
//					createCell(deadCellPos[0],deadCellPos[1]);
//				}
			}
		}
		
		
	}

	/** Returns a list of dead neighbours in arrays */
	public List<long[]> getDeadNeighbours(long x, long y){

		//Search currentBuffer map using cell position as key.
		List<long[]> deadNeighbours = new ArrayList<long[]>();
		long cellX = x;
		long cellY = y;
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
		
		//Testing:
		for (long[] testCell: deadNeighbours) {
			System.out.println(testCell[0]+","+testCell[1]);
		}
		
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
