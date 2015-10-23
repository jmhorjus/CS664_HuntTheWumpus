package bu.edu.cs664;

import java.util.List;
import java.util.Stack;

public class Player {
	Board gameBoard = null;
	Board myBoard = null;
	KnowledgeBase kb = null;	
	Stack<Position> myMoves = new Stack<Position>();
	int points = 0;
	
	Position pos = null;
	Direction myDirection = Direction.SOUTH;
	
	/**
	 * This is the Player object. The player is
	 * given a board by the Wumpus game, and is
	 * expected to act upon it.
	 * 
	 * @param board
	 */
	public Player(Board gameBoard)
	{
		this.gameBoard = gameBoard;
		this.myBoard = new Board(gameBoard.getX(), gameBoard.getY());
		this.kb = new KnowledgeBase(myBoard, gameBoard.getStartingPosition().getX(), gameBoard.getStartingPosition().getY(),  myDirection);
		this.kb.tell(gameBoard.getStartingPosition());
	}
	
	public void play()
	{
		java.util.List<Action> actions = null;
		
		// Enter the game
		pos = gameBoard.getStartingPosition();
		
		do {
			// Print the board state
			myBoard.print();
			
			// Ask what action i should perform next
			actions = kb.ask();
			
			// Perform the action
			pos = performActions(actions);
		}
		while(!gameIsOver());
	}
	
	public Position performActions(List<Action> actions)
	{
		for (Action action : actions)
		{
			switch(action)
			{
				case TURN_LEFT:
					if (myDirection == Direction.SOUTH) 
					{
						myDirection = Direction.EAST;
					}
					else if (myDirection == Direction.EAST) 
					{
						myDirection = Direction.NORTH;
					}
					else if (myDirection == Direction.NORTH)
					{
						myDirection = Direction.WEST;
					}
					else if (myDirection == Direction.WEST) 
					{
						myDirection = Direction.SOUTH;
					}
					points--;
					
					// position stays the same
					break;
				case TURN_RIGHT:
					if (myDirection == Direction.SOUTH) 
					{
						myDirection = Direction.WEST;
					}
					else if (myDirection == Direction.EAST) 
					{
						myDirection = Direction.SOUTH;
					}
					else if (myDirection == Direction.NORTH)
					{
						myDirection = Direction.EAST;
					}
					else if (myDirection == Direction.WEST) 
					{
						myDirection = Direction.NORTH;
					}
					points--;
					
					// position stays the same
					break;
				case MOVE_FORWARD:
					if (myDirection == Direction.SOUTH) 
					{
						if (pos.getY() + 1 > myBoard.getY())
						{
							throw new IllegalArgumentException("Y axis out of bounds while moving FORWARD "
									+ "while facing SOUTH at pos (" 
									+ pos.getX() + "," + pos.getY());
						}
						else 
						{
							pos = myBoard.getPosition(pos.getX(), pos.getY() + 1);
						}
					}
					else if (myDirection == Direction.EAST) 
					{
						if (pos.getX() + 1 > myBoard.getX())
						{
							throw new IllegalArgumentException("X axis out of bounds while moving FORWARD "
									+ "while facing EAST at pos (" 
									+ pos.getX() + "," + pos.getY());
						}
						else
						{
							pos = myBoard.getPosition(pos.getX() + 1, pos.getY());
						}
					}
					else if (myDirection == Direction.NORTH)
					{
						if (pos.getY() - 1 < 0)
						{
							throw new IllegalArgumentException("Y axis out of bounds while moving FORWARD "
									+ "while facing NORTH at pos (" 
									+ pos.getX() + "," + pos.getY());
						}
						else 
						{
							pos = myBoard.getPosition(pos.getX(), pos.getY() - 1);
						}
					}
					else if (myDirection == Direction.WEST) 
					{
						if (pos.getX() - 1 < 0)
						{
							throw new IllegalArgumentException("Y axis out of bounds while moving FORWARD "
									+ "while facing WEST at pos (" 
									+ pos.getX() + "," + pos.getY());
						}
						else 
						{
							pos = myBoard.getPosition(pos.getX() - 1, pos.getY());
						}					
					}
					
					//TODO: Now that we've moved to a new pos, check whether the new pos has a pit or live wumpus in it. 
					//      If so, then the game is over and the knowledgebase is dead.  
					
					points--;
					break;
				case GRAB:
					if (pos.hasGlitter())
					{
						points += 1000;
					}
					break;
				case SHOOT:
					// 10 points to shoot
					points -= 10;
					// TODO: Shooting also shoots an arrow, potentially kills the wumpus, and tells the player whether the wumpus is dead.
					break;
				case CLIMB:
					// TODO: If you're at the enterance, this aciton ends the game.
					break;
				default:
					break;
				case SNIFF_AIR:
					// This is the one where they're saying they want us to tell them the attributes of their current position.
					kb.tell(pos);
			}
		}
		return pos;
	}
	
	// Return true if a.) the knowledgebase is dead or b.) the knowledgebase has escaped with the gold.
	private boolean gameIsOver()
	{
		//return myMoves.isEmpty() ? true : false;
		// TODO: Only return false if the game is actually over. i.e. the knowledgebase is dead or has escaped with the gold.
		return false;
	}
	
	public enum Action {
		TURN_LEFT,
		TURN_RIGHT,
		MOVE_FORWARD,
		GRAB,
		SHOOT,
		CLIMB,
		SNIFF_AIR;
	}
	
	public enum Sensors {
		STENCH,
		GLITTER,
		BREEZE,
		BUMP,
		SCREAM
	}
	
	public enum Direction {
		NORTH,  // y decreasing 
		SOUTH,  // y increasing
		EAST,   // x increasing
		WEST    // x decreasing
	}
	
}
