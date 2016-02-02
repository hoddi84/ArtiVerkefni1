import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class SearchUtil {
	
	public static void LinkedListTest()
	{
		System.out.println("queue");
		Queue<String> frontier = new LinkedList<String>();
		frontier.add("7");
		frontier.add("5");
		frontier.add("7");
		frontier.add("11");
		String x = frontier.remove();
		
		System.out.println(x + " size: " + frontier.size());
		
		
	}
	
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
			//System.out.println(Arrays.toString(action.toArray()));
			
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
