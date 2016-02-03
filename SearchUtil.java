import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SearchUtil {
	
	public static Node search(Node startNode, MapInfo mapInfo, SearchType searchType)
	{
		Node start = startNode;
		Collection<Node> frontier = null;
		LinkedList<Node> visited = null;
		if (searchType == SearchType.DFS || searchType == SearchType.BFS)
		{
			frontier = new LinkedList<Node>();
			visited = new LinkedList<Node>();
		}
		else if (searchType == SearchType.UniformCostSearch)
		{
			frontier = new PriorityQueue<Node>(new NodeComparator());
			visited = new LinkedList<Node>();
		}
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
			else if (searchType == SearchType.UniformCostSearch)
			{
				current = ((PriorityQueue<Node>)frontier).remove();
			}
			//TODO add also uniform cost search
			
			if (current.state.isGoal(mapInfo))
			{
				return current;
			}

			ArrayList<String> action = current.state.getLegalActions(mapInfo);
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
				if (!visited.contains((nextNode.state.hashCode()))) {
					frontier.add(nextNode);
				}
				else
					visited.add(nextNode);
			}
		}
		
		return null;
	}
}
