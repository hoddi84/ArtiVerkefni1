public class Position {
	
	public int x;
	public int y;
	
	public Position(int x, int y) {
		this.x = x; this.y = y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Position))
		{
			return false;
		}
		Position p = (Position)o;
		if (this.x == p.x && this.y == p.y)
		{
			return true;
		}
		return false;
	}

}
