import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
	@Override
	public int compare(Node a, Node b)
	{
		if (a.costFromRoot > b.costFromRoot)
		{
			return 1;
		}
		if (a.costFromRoot < b.costFromRoot)
		{
			return -1;
		}
		return 0;
	}
}
