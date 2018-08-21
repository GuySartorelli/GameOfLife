import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This is the game class.

public class Game {
	private Map<Position, Cell> currentBuffer = new HashMap<Position, Cell>();
	private Map<Position, Cell> backBuffer = new HashMap<Position, Cell>();
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
	public int getNumNeighbours(long x,long y) {
		// find a live cell in the currentBuffer
		// then find how many live neighbour cells we have 
		int totalNeighbours = 0;

		//create list to store cell's neighbouring positions.
		List<Position> neighbourPos = new ArrayList<Position>();
		
		//construct 8 positions to check around cell in long [] format
		Position leftTopPos = new Position (x-cellSize, y-cellSize);
		Position topPos = new Position (x, y-cellSize);
		Position rightTopPos = new Position (x+cellSize, y-cellSize);
		
		Position leftPos = new Position (x-cellSize, y);
		Position rightPos = new Position (x+cellSize, y);
		
		Position leftBottomPos = new Position (x-cellSize, y+cellSize);
		Position bottomPos = new Position (x, y+cellSize);
		Position rightBottomPos = new Position (x+cellSize, y+cellSize);
		
		//adding all 8 positions into the ArrayList neighbourPos.
		neighbourPos.addAll(Arrays.asList(leftTopPos,leftPos,leftBottomPos,
				topPos,bottomPos,rightTopPos, rightPos,rightBottomPos));

		//ask currentBuffer what is in that position in currentBuffer
		for(Position pos : neighbourPos) {
//			System.out.println("checking key: "+ pos[0]+","+pos[1]);
						
			if(currentBuffer.containsKey(pos)) {
				totalNeighbours++;
			}
		}
		for (Position cell: currentBuffer.keySet()) {
//			System.out.println("currentBuffer cell: "+ cell[0]+","+cell[1]);
		}
		return totalNeighbours; // to be changed
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
	public List<Position> getDeadNeighbours(Cell cell){

		//Search currentBuffer map using cell position as key.		
		List<Position> deadNeighbours = new ArrayList<Position>();
		long cellX = (long)cell.getTranslateX();
		long cellY = (long)cell.getTranslateY();
		Position key = new Position (cellX,cellY);

		//Check the neighbour to the left.
		key.setX(key.getX()-cellSize);
		if (!currentBuffer.containsKey(key)) {
			Position position = new Position (key.getX(),key.getY());
			deadNeighbours.add(position);
		}
		//Check the 3 neighbours above from left to right.
		key.setY(key.getY()-cellSize);
		for (int i = 0; i < 3; i++) {
			if (!currentBuffer.containsKey(key)) {
				Position position = new Position (key.getX(),key.getY());
				deadNeighbours.add(position);
			}
			if (i != 2) {
				key.setX(key.getX()+cellSize);
			}
		}
		//Check the neighbour to the right.
		key.setY(key.getY()+cellSize);
		if (!currentBuffer.containsKey(key)) {
			Position position = new Position (key.getX(),key.getY());
			
			deadNeighbours.add(position);
		}
		//Check the 3 neighbours below from right to left.
		key.setY( key.getY()+ cellSize);
		
		for (int i = 0; i < 3; i++) {
			if (!currentBuffer.containsKey(key)) {
				Position position = new Position (key.getX(),key.getY());
			
				deadNeighbours.add(position);
			}
			key.setX(key.getX()-cellSize);
		}
		
		//Testing:
//		for (Position testCell: deadNeighbours) {
//			System.out.println(testCell[0]+","+testCell[1]);
//		}

		return deadNeighbours;
	}
	
	/** creates cell */
	public void createCell(long x, long y) {
		Cell cell = new Cell(cellSize, x, y);
		System.out.println(getNumNeighbours(x,y));
		currentBuffer.put(new Position (x,y), cell);
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
