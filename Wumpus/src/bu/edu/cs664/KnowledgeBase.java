package bu.edu.cs664;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

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
		if (boardPosition().hasAttribute(Attribute.GLITTERS)){
			return grabAndGo();
		}
		
		
		// First make a list of "edge positions" which are not visited but are adjacent to
		// a visited position.
		List<Position> edgePositions = new ArrayList<Position>();
		List<Position> safeEdgePositions = new ArrayList<Position>();
		for (int xx = 0; xx < board.getX(); xx++) {
			for (int yy = 0; yy < board.getY(); yy++) {
				Position pos = board.getPosition(xx, yy);
				// Not a visited position.
				if (!pos.hasAttribute(Attribute.VISITED)) {
					List<Position> adjacents = board.getAdjacentPositions(pos);
					boolean isEdge = false;
					//Adjacent to at least one visited position.
					for (Iterator<Position> adjPosIter = adjacents.iterator(); adjPosIter.hasNext();){
						Position adjPos = adjPosIter.next();
						if (adjPos.hasAttribute(Attribute.VISITED)) {
							isEdge = true;
							edgePositions.add(pos);
							break;
						}
					}
					if (isEdge) {
						// Now we need to determine if this edge position is safe.
						// a.) Must be adjacent to one visited non-breezy position.
						boolean noPits = false;
						for (Iterator<Position> adjPosIter = adjacents.iterator(); adjPosIter.hasNext();){
							Position adjPos = adjPosIter.next();
							if (adjPos.hasAttribute(Attribute.VISITED) && !adjPos.hasAttribute(Attribute.BREEZY)) {
								noPits = true;
								break;
							}
						}			
						// b.) Must be adjacent to one visited non-smelly position, or not-adjacent to one visited smelly position.
						boolean noWumpus = false;
						for (Iterator<Position> adjPosIter = adjacents.iterator(); adjPosIter.hasNext();){
							Position adjPos = adjPosIter.next();
							if (adjPos.hasAttribute(Attribute.VISITED) && !adjPos.hasAttribute(Attribute.BREEZY)) {
								noWumpus = true; // adjacent to one visited non-smelly position
								break;
							}
						}
						if (!noWumpus) {
							// not adjacent to one visited smelly position.
							for (int xxx = 0; xxx < board.getX(); xxx++) {
								for (int yyy = 0; yyy < board.getY(); yyy++) {
									Position pos2 = board.getPosition(xxx, yyy);
									if (pos2.adjacentTo(pos) && pos2.hasAttribute(Attribute.VISITED) && pos2.hasAttribute(Attribute.SMELLY)) {
										
									}
							}}
						}
					}
					
				}
			}
		}
		
		// For each edge position, determine whether that position is safe.
	
		
		// If there are *no* safe edge positions, then we need to get fancy with our arrow.
		// return wumpusKillCommand();
		
		// Among safe edge positions, choose the closest one.  
		// safeEdgePositions.sort( /*need to use a comparator here to compare distance from the current location*/ );
		Position destination = safeEdgePositions.get(0);
		
		return findPath(destination);
	}
	
	
	// The path-finding function.  Must find a series of actions which will 
	protected List<Action> findPath(Position destination) {
		
		List<Action> outputActions = new ArrayList<Action>();
		
		// determine a series of *visited* positions that connect our position to the destination. 
		
		
		return outputActions;
	}
	
	protected List<Action> grabAndGo()
	{
		// Find the exit - we know where it is.  
		Position exitPosition = board.getStartingPosition();
		
		List<Action> outputActions = new ArrayList<Action>();
		outputActions.add(Action.GRAB);
		outputActions.addAll(findPath(exitPosition));
		outputActions.add(Action.CLIMB);
		return outputActions;
	}
	
}
