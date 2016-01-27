import java.util.Collection;

public class MapInfo {
	public int sizeX;
	public int sizeY;
	public Collection<Position> obstacles;
	public Position agentHome;
	
	public MapInfo(int sizeX, int sizeY, Collection<Position> obstacles, Position agentHome)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.obstacles = obstacles;
		this.agentHome = agentHome;
	}
}
