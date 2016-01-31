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
	
	public static Node breathFirstSearch(Node startNode, MapInfo mapInfo)
	{
		Node start = startNode;
		Queue<Node> frontier = new LinkedList<Node>();
		frontier.add(start);
		
		while (!frontier.isEmpty())
		{
			Node current = frontier.remove();
			if (current.state.isGoal(mapInfo))
			{
				return current;
			}
			Collection<String> action = current.state.getLegalActions(mapInfo);
			for (int i = 0; i < action.size(); i++)
			{
				//frontier.add(current.state.get);
				//TODO update method for state that takes
				//in an action and returns a new state
			}
			
		}
		
		return null;
	}
}
