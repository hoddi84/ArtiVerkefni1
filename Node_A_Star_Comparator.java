import java.util.Comparator;

public class Node_A_Star_Comparator implements Comparator<Node> {
	
	/* Information about the world */
	protected MapInfo mapInfo;
	
	Node_A_Star_Comparator(MapInfo mapInfo)
	{
		this.mapInfo = mapInfo;
	}
	
	@Override
	public int compare(Node a, Node b)
	{
		int a_f = a.costFromRoot + SearchUtil.heuristic(a, mapInfo);
		int b_f = b.costFromRoot + SearchUtil.heuristic(b, mapInfo);
		
		if (a_f > b_f)
		{
			return 1;
		}
		if (a_f < b_f)
		{
			return -1;
		}
		return 0;
	}
}
