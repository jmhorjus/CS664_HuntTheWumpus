package bu.edu.cs664;

import java.util.List;
import java.util.ArrayList;

import bu.edu.cs664.Player.Action;
import bu.edu.cs664.Player.Direction;


/**
 * The knowledge base is the AI Logical Agent
 * that makes decisions for the Player.
 *
 */
public class KnowledgeBase {
	Board board = null; // My knowledge of the board.
	Position currentPos = null; // Where I believe myself to be; x and y.
	Direction currentDir = null;
	
	// Get the "position" object from the board where we are.
	private Position boardPosition()
	{
		return board.getPosition(currentPos.getX(), currentPos.getY());
	}
	
	// Constructor
	public KnowledgeBase(Board board)
	{
		this.board = board;
	}
	
	// The game s telling me the attributes of my current position.
	public void tell(Position pos)
	{
		// Set the attributes given as well as the "visited" attribute on my current space.
		pos.add(Attribute.VISITED);
		// board.setPosition(currentPos.getX(), currentPos.getY(), attributes);
	}
	
	// The Game is asking me what action (or series of actions) I want to take next.
	public List<Action> ask()
	{
		// First priority is if I know where the gold is (and I just found it and am standing on it)
		// then grab it and get out via a safe (i.e. visited) path.
		if ()
		
		
		// First make a list of "edge positions" which are not visited but are adjacent to
		// a visited position.
		List<Position> edgePositions = new ArrayList<Position>();
		for (int xx = 0; xx < board.getX(); xx++) {
			for (int yy = 0; yy < board.getY(); yy++) {
				
				
				
				if (board.getPosition(xx, yy).hasAttribute(Attribute.VISITED) ) {
				
					
					
					
				}
			}
		}
		
		// For each edge position, determine whether that position is safe.
		// a.) Must be adjacent to one visited non-breezy position.
		// b.) Must be adjacent to one visited non-smelly position, or not-adjacent to one visited smelly position.
		List<Position> safeEdgePositions = new ArrayList<Position>();
		
		// If there are *no* safe edge positions, then we need to get fancy with our arrow.
		// return wumpusKillCommand();
		
		// Among safe edge positions, choose the closest one.  
		// safeEdgePositions.sort( /*need to use a comparator here to compare distance from the current location*/ );
		Position destination = safeEdgePositions.get(0);
		
		return findPath(destination);
	}
	
	
	// The path-finding function.  Must find a series of actions which will 
	public List<Action> findPath(Position destination) {
		
		List<Action> outputActions = new ArrayList<Action>();
		
		// determine a series of *visited* positions that connect our position to the destination. 
		
		return outputActions;
	}
	
	
}
