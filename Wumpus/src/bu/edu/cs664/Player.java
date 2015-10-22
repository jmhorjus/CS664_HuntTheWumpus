package bu.edu.cs664;

import java.util.Stack;

public class Player {
	Board gameBoard = null;
	Board myBoard = null;
	KnowledgeBase kb = null;	
	Stack<Position> myMoves = new Stack<Position>();
	int points = 0;
	
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
		this.kb = new KnowledgeBase(myBoard);
	}
	
	public void play()
	{
		Position pos = null;
		java.util.List<Action> actions = null;
		
		// Enter the game
		pos = gameBoard.getStartingPosition();
		
		do {
			// Print the board state
			myBoard.print();
			
			// Tell the KB my position
			kb.tell(pos);
			
			// Ask what action i should perform next
			actions = kb.ask();
			
			// Perform the action
			// pos = performAction(action);
		}
		while(!gameIsOver());
	}
	
	private boolean gameIsOver()
	{
		return myMoves.isEmpty() ? true : false;
	}
	
	public enum Action {
		TURN_LEFT,
		TURN_RIGHT,
		MOVE_FORWARD,
		GRAB,
		SHOOT,
		CLIMB;
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
