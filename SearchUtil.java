import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SearchUtil {
	
	public static Node breadthFirstSearch(Node startNode, MapInfo mapInfo)
	{
		Node start = startNode;
		LinkedList<Node> frontier = new LinkedList<Node>();
		frontier.add(start);
		
		while (!frontier.isEmpty())
		{
			//removeFirst -> queue
			//removeLast -> stack
			Node current = frontier.removeFirst();
			if (current.state.isGoal(mapInfo))
			{
				return current;
			}

			ArrayList<String> action = current.state.getLegalActions(mapInfo);
			for (int i = 0; i < action.size(); i++)
			{
				Node nextNode = new Node();
				nextNode.parentNode = current;
				nextNode.Action = action.get(i);
				try
				{
					nextNode.state = current.state.getNextState(action.get(i));
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
				frontier.add(nextNode);
			}
		}
		
		return null;
	}
}
