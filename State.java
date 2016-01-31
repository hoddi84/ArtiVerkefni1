import java.util.ArrayList;
import java.util.Collection;

public class State {
	public Orientation orientation;
	public Position position;
	public boolean turned_on;

	public Collection<Position> dirt;
	
	public State(Position position, Orientation orientation, boolean turned_on, Collection<Position> dirt) {
		this.position = position;
		this.orientation = orientation;
		this.turned_on = turned_on;
		this.dirt = dirt;
	}
	
	public boolean isGoal(MapInfo mapInfo)
	{
		return (dirt.isEmpty() && position.equals(mapInfo.agentHome));
	}
	
	/* Check if the given position is in a collection of positions */
	public boolean isPositionInCollection(Position pos, Collection<Position> posCollection)
	{
		for (int i = 0; i < posCollection.size(); i++)
		{
			if (((ArrayList<Position>) posCollection).get(i).equals(pos))
			{
				return true;
			}
		}
		return false;
	}
	
	/* Check if there is dirt at a given position */
	public boolean isDirtAtPosition(Position pos)
	{
		return isPositionInCollection(pos, dirt);
	}
	
	/* Check if there is an obstacle at a given position */
	public boolean isObstacleAtPosition(Position pos, MapInfo mapInfo)
	{
		return isPositionInCollection(pos, mapInfo.obstacles);
	}
	
	/* Checks if the agent is faced towards the border of the map */
	public boolean isAgentFacedTowardsWall(MapInfo mapInfo)
	{
		if (this.orientation == Orientation.SOUTH && this.position.y == 1)
		{
			return true;
		}
		if (this.orientation == Orientation.NORTH && this.position.y == mapInfo.sizeY)
		{
			return true;
		}
		if (this.orientation == Orientation.EAST && this.position.x == mapInfo.sizeX)
		{
			return true;
		}
		if (this.orientation == Orientation.WEST && this.position.x == 1)
		{
			return true;
		}
		return false;
	}
	
	public boolean isAgentFacedTowardsObstacle(MapInfo mapInfo)
	{
		Position positionToLookAt = new Position(this.position.x, this.position.y);
		if (this.orientation == Orientation.SOUTH)
		{
			positionToLookAt.y -= 1;
		}
		else if (this.orientation == Orientation.NORTH)
		{
			positionToLookAt.y += 1;
		}
		else if (this.orientation == Orientation.EAST)
		{
			positionToLookAt.x += 1;
		}
		else if (this.orientation == Orientation.WEST)
		{
			positionToLookAt.x -= 1;
		}
		
		if (isObstacleAtPosition(positionToLookAt, mapInfo))
		{
			return true;
		}
		return false;
	}
	
	public Collection<String> getLegalActions(MapInfo mapInfo)
	{
		/* These are only the actions that are rational for the agent
		 * it its current state */
		Collection<String> legalActions = new ArrayList<String>();
		
		if (!turned_on)
		{
			legalActions.add(Actions.TURN_ON);
			return legalActions;
		}
		
		if (isDirtAtPosition(this.position))
		{
			legalActions.add(Actions.SUCK);
			return legalActions;
		}
		
		if (isGoal(mapInfo))
		{
			legalActions.add(Actions.TURN_OFF);
			return legalActions;
		}
		
		legalActions.add(Actions.TURN_RIGHT);
		legalActions.add(Actions.TURN_LEFT);
		
		if (!isAgentFacedTowardsWall(mapInfo) && !isAgentFacedTowardsObstacle(mapInfo))
		{
			legalActions.add(Actions.GO);
		}
		
		return legalActions;
	}
}
