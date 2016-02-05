import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class State implements Cloneable {
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
	
	public int hashCode()
	{
		int ori = 0;
		if (this.orientation == Orientation.SOUTH)
		{
			ori = 1;
		}
		if (this.orientation == Orientation.EAST)
		{
			ori = 2;
		}
		if (this.orientation == Orientation.WEST)
		{
			ori = 3;
		}
		
		int turnedOn = turned_on ? 0 : 1;
		
        int hash = 1;
        int bigPrime = 6029;
        hash = hash * bigPrime + position.x;
        hash = hash * bigPrime + position.y;
        hash = hash * bigPrime + ori;
        hash = hash * bigPrime + turnedOn;

		int dirtSize = this.dirt.size();
		for (int i = 0; i < dirtSize; i++) {
			Position dirts = ((ArrayList<Position>)dirt).get(i);
			hash = hash * bigPrime + dirts.x;
			hash = hash * bigPrime + dirts.y;
		}
        return hash;
	}
	
	/* Make a copy of this state object
	 * All the members are also new copies
	 * such as the position */
	public State makeCopy()
	{
		Position newPos = new Position(this.position.x, this.position.y);
		Collection<Position> newDirt = new ArrayList<Position>();
		int n = dirt.size();
		for (int i = 0; i < n; i++)
		{
			Position dirtPos = ((ArrayList<Position>)dirt).get(i);
			newDirt.add(new Position(dirtPos.x, dirtPos.y));
		}
		return new State(newPos, this.orientation, this.turned_on, newDirt);
	}
	
	/* Get the next state for a given action
	 * The action must be a legal action
	 * else the resulting new state will be incorrect
	 *  */
	public State getNextState(String action) throws Exception
	{
		State newState = this.makeCopy();
		if (action == Actions.TURN_ON)
		{
			newState.turned_on = true;
			return newState;
		}
		
		if (action == Actions.TURN_OFF)
		{
			newState.turned_on = false;
			return newState;
		}
		
		if (action == Actions.SUCK)
		{
			int n = newState.dirt.size();
			for (int i = 0; i < n; i++)
			{
				Position dirtPos = ((ArrayList<Position>)newState.dirt).get(i);
				if (this.position.equals(dirtPos))
				{
					newState.dirt.remove(dirtPos);
					return newState;
				}
			}
		}
		
		if (action == Actions.GO)
		{
			if (this.orientation == Orientation.NORTH)
			{
				newState.position.y += 1;
			}
			else if (this.orientation == Orientation.SOUTH)
			{
				newState.position.y -= 1;
			}
			else if (this.orientation == Orientation.EAST)
			{
				newState.position.x += 1;
			}
			else if (this.orientation == Orientation.WEST)
			{
				newState.position.x -= 1;
			}
			return newState;
		}
		
		if (action == Actions.TURN_RIGHT)
		{
			if (this.orientation == Orientation.NORTH)
			{
				newState.orientation = Orientation.EAST;
			}
			else if (this.orientation == Orientation.EAST)
			{
				newState.orientation = Orientation.SOUTH;
			}
			else if (this.orientation == Orientation.SOUTH)
			{
				newState.orientation = Orientation.WEST;
			}
			else if (this.orientation == Orientation.WEST)
			{
				newState.orientation = Orientation.NORTH;
			}
			return newState;
		}
		
		if (action == Actions.TURN_LEFT)
		{
			if (this.orientation == Orientation.NORTH)
			{
				newState.orientation = Orientation.WEST;
			}
			else if (this.orientation == Orientation.WEST)
			{
				newState.orientation = Orientation.SOUTH;
			}
			else if (this.orientation == Orientation.SOUTH)
			{
				newState.orientation = Orientation.EAST;
			}
			else if (this.orientation == Orientation.EAST)
			{
				newState.orientation = Orientation.NORTH;
			}
			return newState;
		}
		
		throw new Exception("Not a valid action string");
		//State nextState = new State();
		//return newState;
	}
	
	public boolean isHome(MapInfo mapInfo) 
	{
		return (dirt.isEmpty() && position.equals(mapInfo.agentHome));
	}
	
	public boolean isGoal(MapInfo mapInfo)
	{
		return (dirt.isEmpty() && position.equals(mapInfo.agentHome) && !turned_on);
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
	
	public ArrayList<String> getLegalActions(MapInfo mapInfo)
	{
		/* These are only the actions that are rational for the agent
		 * it its current state */
		ArrayList<String> legalActions = new ArrayList<String>();
		
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
		
		if (isHome(mapInfo))
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
