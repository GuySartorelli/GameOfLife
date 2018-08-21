
public class Position {

	private long x;
	private long y;
	
	public Position(long x, long y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(long[] pos) {
		this.x = pos[0];
		this.y = pos[1];	
	}

	public long [] toArray() {
		
		long[] posArray = new long[] {x,y};
		
		return posArray;
	}
	
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
