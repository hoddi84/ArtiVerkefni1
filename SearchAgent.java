import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ListSelectionEvent;

public class SearchAgent implements Agent
{
	private MapInfo mapInfo;
	private State state;
	
	private Node goalNode;
	private LinkedList<String> goalPath;

	/*
		init(Collection<String> percepts) is called once before you have to select the first action. Use it to find a plan. Store the plan and just execute it step by step in nextAction.
	*/
    public void init(Collection<String> percepts) {
		/*
			Possible percepts are:
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forget to turn it on.
		*/
    	// MapInfo
    	int sizeX = 0;
    	int sizeY = 0;
    	Position homePos = new Position(0,0);
    	ArrayList<Position> obstacles = new ArrayList<Position>();
    	
    	// State
    	Orientation ori = Orientation.NORTH;
    	ArrayList<Position> dirts = new ArrayList<Position>();

		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+).*");
		for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
				String perceptName = perceptNameMatcher.group(1);
				// System.out.println(percept);
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						//System.out.println(perceptName + " is at " + m.group(1) + "," + m.group(2));
						homePos.x = Integer.valueOf(m.group(1));
						homePos.y = Integer.valueOf(m.group(2));
					}
				} else if (perceptName.equals("ORIENTATION")) {
					Matcher m = Pattern.compile("\\(\\s*ORIENTATION\\s+([A-Z]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						//System.out.println(perceptName + " is " + m.group(1));
						ori = Orientation.valueOf(m.group(1));
					}
				} else if (perceptName.equals("SIZE")) {
					Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						//System.out.println(perceptName + " is " + m.group(1) + "," + m.group(2));
						sizeX = Integer.valueOf(m.group(1));
						sizeY = Integer.valueOf(m.group(2));
					}
				} else if (perceptName.equals("AT")) {
					Matcher m = Pattern.compile("\\(\\s*AT\\s+([A-Z]+)\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						String atName = m.group(1);
						if (atName.equals("DIRT")) {
							//System.out.println(atName + " is at " + m.group(2) + "," + m.group(3));
							dirts.add(new Position(Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3))));
						}
						else if (atName.equals("OBSTACLE")) {
							//System.out.println(atName + " is at " + m.group(2) + "," + m.group(3));
							obstacles.add(new Position(Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3))));
						}
					}
				} else {
					System.out.println("other percept:" + percept);
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		
		mapInfo = new MapInfo(sizeX, sizeY, obstacles, homePos);
		state = new State(homePos, ori, false, dirts);

		// Debug
		System.out.println("MapInfo: ");
		System.out.println("\tSizeX: " + mapInfo.sizeX);
		System.out.println("\tSizeY: " + mapInfo.sizeY);
		System.out.println("\tObstacles: " + Arrays.toString(obstacles.toArray()));
		System.out.println("\tAgent Home: " + mapInfo.agentHome);
		System.out.println("State: ");
		System.out.println("\tPosition: " + state.position);
		System.out.println("\tOrientation: " + state.orientation);
		System.out.println("\tTurned on: " + state.turned_on);
		System.out.println("\tDirts: " + Arrays.toString(dirts.toArray()));
		
		
		// Start searching
		Node startNode = new Node();
		startNode.parentNode = null;
		startNode.state = state;
		startNode.Action = "";
		startNode.costFromRoot = 0;
		System.out.println("Starting search");
		goalNode = SearchUtil.search(startNode, mapInfo, SearchType.A_Star);
		System.out.println("search done");
		
		goalPath = createPath();
    }
    
	// Traverse through parent nodes to create the full path
    public LinkedList<String> createPath() {
    	System.out.println("Creating action path");
		Node current = goalNode;
		LinkedList<String> actionPath = new LinkedList<String>();
		while (current.parentNode != null)
		{
			actionPath.add(current.Action);
			current = current.parentNode;
		}
		System.out.println("Length: " + actionPath.size());
		System.out.println(actionPath);
		
    	return actionPath;
    }
    
    public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		
		//String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		return goalPath.removeLast();
	}
}
