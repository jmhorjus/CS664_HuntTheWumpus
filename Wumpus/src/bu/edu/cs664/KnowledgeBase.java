package bu.edu.cs664;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
	public KnowledgeBase(Board board, Direction startDir)
	{
		this.board = board;
		this.currentDir = startDir;
	}
	
	// The game s telling me the attributes of my current position.
	public void tell(Position pos)
	{
		// Set the attributes given as well as the "visited" attribute on my current space.
		pos.add(Attribute.VISITED);
		boardPosition().setAttributes(pos.getAttributes());
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
									if (!pos2.adjacentTo(pos) && pos2.hasAttribute(Attribute.VISITED) && pos2.hasAttribute(Attribute.SMELLY)) {
										noWumpus = true; // adjacent to one visited non-smelly position
										break;
									}
							}}
						}
						
						if (noWumpus && noPits) {
							// THIS IS A SAFE EDGE SPACE.
							safeEdgePositions.add(pos);
						}
					}	
				}
			}
		}

		
		// If there are *no* safe edge positions, then we need to get fancy with our arrow.
		//return wumpusKillCommand();
		
		// Among safe edge positions, choose the closest one.  
		Position.curX = currentPos.getX();
		Position.curY = currentPos.getY();
		Collections.sort( safeEdgePositions );
		Position destination = safeEdgePositions.get(0);
		
		return findPath(destination);
	}
	
	protected List<Action> wumpusKillCommand()
	{
		Position posWump = null;
		Boolean posWumpKnown = false;
		List<Action> myacts;
		
		for (int x=0; x<board.getX(); x++)
		{
			for (int y=0; y<board.getY(); y++)
			{
				if (board.getPosition(x, y).hasWumpus())
				{
					posWumpKnown = true;
					posWump = board.getPosition(x, y);
				}
			}
		}
		
		if (posWumpKnown)
		{
			myacts = findPath(posWump);
		}
		else
		{
			//look for wumpus based on where smelly spots are on the board
			for (int x=0; x<board.getX(); x++)
			{
				for (int y=0; y<board.getY(); y++)
				{
					if (board.getPosition(x, y).hasSmelly())
					{
						if (((x+2) <= board.getX()) && board.getPosition(x+2, y).hasSmelly())
						{
							posWumpKnown = true;
							posWump = board.getPosition(x+1, y);
						}
						else if (((y+2) <= board.getY()) && board.getPosition(x, y+2).hasSmelly())
						{
							posWumpKnown = true;
							posWump = board.getPosition(x, y+2);
						}
						else if (((x+1) <= board.getX()) && ((y+1) <= board.getY()) && board.getPosition(x+1, y+1).hasSmelly())
						{
							posWumpKnown = true;
							posWump = board.getPosition(x, y+1);
						}
						else if (((x-1) >= 0) && ((y+1) <= board.getY()) && board.getPosition(x-1, y+1).hasSmelly())
						{
							posWumpKnown = true;
							posWump = board.getPosition(x, y+1);
						}
					}
				}
			}
			
			if (posWumpKnown)
			{
				myacts = findPath(posWump);
			}
			else
			{
				return null; //(unable to locate wumpus using all known information about board)
			}
		}
		
		//insert commands to kill wumpus with arrow before entering wumpus's position
		//*assuming that command to move into wumpus's position will be the last command in myacts*
		int index = myacts.size()-1;
		myacts.add(index, Action.SHOOT);
		
		return myacts;
		
	}
	
	
	// The path-finding function.  Must find a series of actions which will 
	protected List<Action> findPath(Position destination) {
		
		List<Action> outputActions = new ArrayList<Action>();
		
		// determine a series of *visited* positions that connect our position to the destination. 
		
		// Super naive solution:
		// Just try walking along a path in the general direction of the destination.  
		// If there's a un-visited space in our way, try another direction.  
		Position here = this.boardPosition();
		Direction facing = this.currentDir;
		while (here != destination)
		{
			// What's the direction from here to destination?
			int distanceSouthY = destination.getY() - here.getY();
			int distanceEastX = destination.getX() - here.getX();
			
			Direction nextDirection = null;
			
			// Try to find a good direction to move in that is into a visited space and generally toward the goal. 
			if (Math.abs(distanceSouthY) > Math.abs(distanceEastX)) {
				// Try to go north/south toward the destination.  
				if (distanceSouthY > 0 && board.getNextPosInDirection(here, Direction.SOUTH).hasAttribute(Attribute.VISITED)) {
					// Go south; it's safe and (probably) the right way!
					nextDirection = Direction.SOUTH;
				} else if (board.getNextPosInDirection(here, Direction.NORTH).hasAttribute(Attribute.VISITED)) {
					// Go north; it's safe and (probably) the right way!
					nextDirection = Direction.NORTH;
				}
			} else {
				// Try to go north/south toward the destination.  
				if (distanceEastX > 0 && board.getNextPosInDirection(here, Direction.EAST).hasAttribute(Attribute.VISITED)) {
					// Go east; it's safe and (probably) the right way!
					nextDirection = Direction.EAST;	
				} else if ( board.getNextPosInDirection(here, Direction.WEST).hasAttribute(Attribute.VISITED)) {
					// Go west; it's safe and (probably) the right way!
					nextDirection = Direction.WEST;	
				}
			}
			
			// Take a step in the next direction! 
			outputActions.addAll( turnToDirection(facing, nextDirection) );
			facing = Direction.SOUTH;
			outputActions.add(Action.MOVE_FORWARD);
			here = board.getNextPosInDirection(here, Direction.SOUTH);
			
		}
		
		return outputActions;
	}
	
	// Takes a list of actions and appends turn actions to that list so that the player will be facing the desired direction afterward.
	protected List<Action> turnToDirection(Direction st, Direction end)
	{
		List<Action> outputActions = new ArrayList<Action>();
		
		// Not the most elegant solution!  Brute force, yay!
		// Cases are: Turn right, Turn Left, Turn Around, or Steady On.
		// 1.) Steady On
		if (st == end) {
			// Do nothing!
		// 2.) Turn Around
		} else if ( (st == Direction.NORTH && end == Direction.SOUTH ) ||
				(st == Direction.SOUTH && end == Direction.NORTH ) ||
				(st == Direction.EAST && end == Direction.WEST ) ||
				(st == Direction.WEST && end == Direction.EAST )
				){ 
			outputActions.add(Action.TURN_RIGHT);
			outputActions.add(Action.TURN_RIGHT);
		// 3.) Turn Right
		} else if ( (st == Direction.NORTH && end == Direction.EAST ) ||
				(st == Direction.EAST && end == Direction.SOUTH ) ||
				(st == Direction.SOUTH && end == Direction.WEST ) ||
				(st == Direction.WEST && end == Direction.NORTH )
				){ 
			outputActions.add(Action.TURN_RIGHT);
		// 4.) Turn Left
		} else if ( (st == Direction.NORTH && end == Direction.WEST ) ||
				(st == Direction.WEST && end == Direction.SOUTH ) ||
				(st == Direction.SOUTH && end == Direction.EAST ) ||
				(st == Direction.EAST && end == Direction.NORTH )
				){ 
			outputActions.add(Action.TURN_LEFT);
		} else {
			throw new IllegalArgumentException("The directions defy logic! ");
		}
			
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
