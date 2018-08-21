import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to for creating Game object.
 * @author shawbeva
 *
 */

public class Game {
	private Map<Position, Cell> currentBuffer = new HashMap<Position, Cell>();
	private Map<Position, Cell> backBuffer = new HashMap<Position, Cell>();
	private int cellSize;
	private Map<String, List<int[]>> patterns;
	
	/**
	 * Constructor for the game.
	 * @param cellSize
	 * cellSize is the length of width and height of each Cell object.
	 */
	public Game(int cellSize) {
		this.cellSize = cellSize;
		parsePatterns();
		defineInitialPattern();
		
		//Test getDeadNeighbours method.
		getDeadNeighbours(100,100);
	}

	/** Getter for currentBuffer */
	public Collection<Cell> getCurrentBuffer() {
		System.out.println("getting current buffer");
		for (Position cell: currentBuffer.keySet()) {
			System.out.println(currentBuffer.get(cell));
		}
		return currentBuffer.values();
	}
	
	/**
	 * update method first checks the Cells and then swaps the buffers.
	 * update method calls checkCells method and swapbuffer methods.
	 * checkCells checks if the cell will live or die and checks 
	 * swapBuffer creates a copy of backBuffer and sets it into currentBuffer. 
	 * Then backBuffer is cleared.
	 */
	public void update() {
		//defineInitialPattern();//remove for production
		checkCells();
		swapBuffers();
	}
	
	/**
	 * swapBuffer puts backBuffer into currentBuffer.
	 * It does this by creating a copy of backBuffer and sets it into currentBuffer. 
	 * Then backBuffer is cleared.
	 */
	public void swapBuffers() {
		currentBuffer.clear();
		currentBuffer.putAll(backBuffer);
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
			if(currentBuffer.containsKey(pos)) {
				totalNeighbours++;
			}
		}

		return totalNeighbours; // to be changed
	}	
	
	/** Checks all cells, by getting neighbours and num of neighbours
	 * recursion magic happens here */
	public void checkCells() {
		
		//Iterate through the currentBuffer map to check each alive cell.
		//Once checked, the position of the dead neighbour is added to the checkedDead list.
		List<Position> checkedDead = new ArrayList<Position>();
		for (Position cellPos: currentBuffer.keySet()) {
			//Get the list of dead neighbours around the cell.
			List<Position> deadNeighbours = getDeadNeighbours(cellPos.getX(),cellPos.getY());
			//Iterate through the cell's deadNeighbours list to check whether it should be born.
			String deadCellPositions = "";
			for (Position deadCellPos: deadNeighbours) {
				if (!checkedDead.contains(deadCellPos) && getNumNeighbours(deadCellPos.getX(),deadCellPos.getY())==3){
					//Cell is born.
					createCell(deadCellPos.getX(),deadCellPos.getY());
				}
				checkedDead.add(deadCellPos);
				deadCellPositions += +deadCellPos.getX()+","+deadCellPos.getY()+"  ";
			}
			
			//Check self to see if it should remain alive or die.
			if (getNumNeighbours(cellPos.getX(),cellPos.getY()) == 2 || getNumNeighbours(cellPos.getX(),cellPos.getY()) == 3) {
				//Cell remains alive. Added to back buffer within createCell method.
				createCell(cellPos.getX(),cellPos.getY());
			} 
		}
		
		
	}

	/** Returns a list of dead neighbours in arrays */
	public List<Position> getDeadNeighbours(long x, long y){

		//Search currentBuffer map using cell position as key.		
		List<Position> deadNeighbours = new ArrayList<Position>();
		long cellX = x;
		long cellY = y;
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
		String returnedDeadNeighbours = "returned";
		for (Position testCell: deadNeighbours) {
			returnedDeadNeighbours += testCell.getX()+","+testCell.getY()+"  ";
		}
		System.out.println(returnedDeadNeighbours);

		return deadNeighbours;
	}
	
	/** creates cell */
	public void createCell(long x, long y) {
		Cell cell = new Cell(cellSize, x, y);
		backBuffer.put(new Position(x,y), cell);
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
			long x = position[0]*cellSize;
			long y = position[1]*cellSize;
			Cell cell = new Cell(cellSize,x, y);
			Position cellPos = new Position(x,y);
			currentBuffer.put(cellPos, cell);
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
