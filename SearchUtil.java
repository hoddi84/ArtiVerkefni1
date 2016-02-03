import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class SearchUtil {
	
	public static Node search(Node startNode, MapInfo mapInfo, SearchType searchType)
	{
		Collection<Integer> visitedStates = new ArrayList<Integer>();
		Collection<Node> frontier = null;
		if (searchType == SearchType.DFS || searchType == SearchType.BFS)
		{
			frontier = new LinkedList<Node>();
		}
		else if (searchType == SearchType.UniformCostSearch)
		{
			frontier = new PriorityQueue<Node>(new NodeComparator());
		}
		visitedStates.add(startNode.state.hashCode());
		frontier.add(startNode);
		
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
			else if (searchType == SearchType.UniformCostSearch)
			{
				current = ((PriorityQueue<Node>)frontier).remove();
			}
			//TODO add also uniform cost search
			
			if (current.state.isGoal(mapInfo))
			{
				return current;
			}

			ArrayList<String> actions = current.state.getLegalActions(mapInfo);
			Collections.shuffle(actions);
			for (int i = 0; i < actions.size(); i++)
			{
				Node nextNode = new Node();
				nextNode.parentNode = current;
				nextNode.costFromRoot = current.costFromRoot + 1;
				nextNode.Action = actions.get(i);
				try
				{
					nextNode.state = current.state.getNextState(actions.get(i));
				}
				catch (Exception e)
				{
					System.out.println("Error: "+e.getMessage());
				}
				
				if (!visitedStates.contains(nextNode.state.hashCode())) {
					visitedStates.add(nextNode.state.hashCode());
					frontier.add(nextNode);
				}
			}
		}
		
		return null;
	}
}
