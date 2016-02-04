import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SearchUtil {
	
	/*
	 * Calculate the manhattan distance between 2 positions
	 * */
	public static int manhattanDistance(Position a, Position b)
	{
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	/*
	 * find the nearest position in positions to the position p
	 * measured in manhattan distance
	 * if no position with a distance greater than zero is found null is returned
	 * */
	public static Position findNearest(Position p, ArrayList<Position> positions)
	{
		Position nearest = null;
		int minDistSoFar = 0;
		int n = positions.size();
		boolean init = true;
		for (int i = 0; i < n; i++)
		{
			Position current = positions.get(i);
			int dist = manhattanDistance(p, current);
			if (dist > 0)
			{
				if (init)
				{
					minDistSoFar = dist;
					nearest = current;
					init = false;
				}
				if (dist < minDistSoFar)
				{
					minDistSoFar = dist;
					nearest = current;
				}
			}
		}
		return nearest;
	}
	
	/*
	 * Find the position in positions that is furthest away from p
	 * Measured in manhattan distances
	 * */
	public static Position findFurthest(Position p, ArrayList<Position> positions)
	{
		Position furthest = positions.get(0);
		int maxDistSoFar = 0;
		int n = positions.size();
		for (int i = 0; i < n; i++)
		{
			Position current = positions.get(i);
			int dist = manhattanDistance(p, current);
			if (dist > maxDistSoFar)
			{
				maxDistSoFar = dist;
				furthest = current;
			}
		}
		return furthest;
	}
	
	/*
	 * A heuristic function
	 * Calculates the estimated cost from a node to a the goal
	 * The cost should always be less or equal to the actual cost for the heuristic
	 * to be admissible
	 * Takes in a node and a mapInfo object that has information about the world
	 * */
	public static int heuristic(Node node, MapInfo mapInfo)
	{
		int agentDistance = 0;
		int homeDistance = 0;
		int dirtTotalDistance = 0;
		int dirtCount = node.state.dirt.size();
		if (dirtCount == 0)
		{
			agentDistance = 0;
			homeDistance = manhattanDistance(node.state.position, mapInfo.agentHome);
			dirtTotalDistance = 0;
		}
		else if (dirtCount == 1)
		{
			Position dirtPos = ((ArrayList<Position>)node.state.dirt).get(0);
			agentDistance = manhattanDistance(node.state.position, dirtPos);
			homeDistance = manhattanDistance(mapInfo.agentHome, dirtPos);
		}
		else
		{
			ArrayList<Position> dirtList = (ArrayList<Position>)node.state.dirt;
			Position nearestDirtToAgent = findNearest(node.state.position, dirtList);
			agentDistance = manhattanDistance(node.state.position, nearestDirtToAgent);
			Position nearestDirtToHome = findNearest(mapInfo.agentHome, dirtList);
			homeDistance = manhattanDistance(mapInfo.agentHome, nearestDirtToHome);
			
			dirtTotalDistance = 0;
			for (int i = 0; i < dirtCount; i++)
			{
				Position currentDirt = dirtList.get(i);
				/* Count for each dirt except the one that is nearest to home */
				if (!currentDirt.equals(nearestDirtToHome))
				{
					Position nearestDirt = findNearest(currentDirt, dirtList);
					if (nearestDirt != null)
					{
						dirtTotalDistance += manhattanDistance(currentDirt, nearestDirt);
					}
				}
			}
		}
		
		int h = agentDistance + homeDistance + dirtTotalDistance;
		System.out.println("AGENT=" + agentDistance + "," +
		"HOME=" + homeDistance + "," +
		"DIRT=" + dirtTotalDistance);
		
		return h;
	}
	
	public static Node search(Node startNode, MapInfo mapInfo, SearchType searchType)
	{
		Collection<Integer> visitedStates = new ArrayList<Integer>();
		
		
		Node start = startNode;
		Collection<Node> frontier = null;
		if (searchType == SearchType.DFS || searchType == SearchType.BFS)
		{
			frontier = new LinkedList<Node>();
		}
		else if (searchType == SearchType.UniformCostSearch)
		{
			frontier = new PriorityQueue<Node>(new NodeComparator());
		}
		else if (searchType == SearchType.A_Star)
		{
			frontier = new PriorityQueue<Node>(new Node_A_Star_Comparator(mapInfo));
		}
		visitedStates.add(startNode.state.hashCode());
		frontier.add(start);
		
		while (!frontier.isEmpty())
		{
			//removeFirst -> queue
			//removeLast -> stack
			Node current = null;
			if (searchType == SearchType.DFS)
			{
				current = ((LinkedList<Node>)frontier).removeLast();
			}
			else if (searchType == SearchType.BFS)
			{
				current = ((LinkedList<Node>)frontier).removeFirst();
			}
			else if (searchType == SearchType.UniformCostSearch || searchType == SearchType.A_Star)
			{
				current = ((PriorityQueue<Node>)frontier).remove();
			}
			
			//TODO add also uniform cost search
			
			if (current.state.isGoal(mapInfo))
			{
				return current;
			}

			ArrayList<String> action = current.state.getLegalActions(mapInfo);
			Collections.shuffle(action);
			for (int i = 0; i < action.size(); i++)
			{
				Node nextNode = new Node();
				nextNode.parentNode = current;
				nextNode.costFromRoot = current.costFromRoot + 1;
				nextNode.Action = action.get(i);
				try
				{
					nextNode.state = current.state.getNextState(action.get(i));
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
				
				if (!visitedStates.contains(nextNode.state.hashCode()))
				{
					visitedStates.add(nextNode.state.hashCode());
					frontier.add(nextNode);
				}
				
			}
		}
		return null;
	}
}
