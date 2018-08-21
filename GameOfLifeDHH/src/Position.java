/**
 * Class holds a Cell position as an object. 
 * @author saadheba
 *
 */
public class Position {

	private long x;
	private long y;
	/**
	 * Constructor
	 * @param x
	 * x position of Cell
	 * @param y
	 * y position of Cell.
	 */
	public Position(long x, long y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Constructor
	 * @param pos
	 * An array of long values containing only x and y positions
	 * of the Cell.
	 */
	public Position(long[] pos) {
		this.x = pos[0];
		this.y = pos[1];	
	}
	/**
	 * toArray returns x and y as an Array.
	 * @return
	 * returns x and y as an Array.
	 */
	public long [] toArray() {

		long[] posArray = new long[] {x,y};

		return posArray;
	}

	/*below methods (hashCode and equals) were automatically generated methods
	 * which were necessary for being able to check Position values (x and y) 
	 * against the hashMap values in the Game class.*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	//Setters and Getters.
	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public long getY() {
		return y;
	}

	public void setY(long y) {
		this.y = y;
	}

}
