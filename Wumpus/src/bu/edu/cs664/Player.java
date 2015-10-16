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
	}
	
	public void play()
	{
		Position currentPos = null;
		Position nextPos = null;
		Position pos = null;
		Action action = null;
		
		// Enter the game
		pos = gameBoard.getStartingPosition();
		
		do {
			// Print the board state
			myBoard.print();
			
			// Tell the KB what is here
			kb.tell(pos);
			
			// Ask what action i should perform next
			action = kb.ask();
			
			// Perform the action
			pos = performAction(action);
		}
		while(!gameIsOver());
	}
	
	private boolean gameIsOver()
	{
		return myMoves.isEmpty() ? true : false;
	}
	
	/**
	 * The knowledge base is the AI Logical Agent
	 * that makes decisions for the Player.
	 *
	 */
	public class KnowledgeBase {
		Board board = null;
		Position currentPos = null;

		
		// Constructor
		public KnowledgeBase(Board board)
		{
			this.board = board;
		}
		
		/**
		 * Tell the board what I perceive.
		 * @param pos the current position
		 */
		public void tell(Position pos)
		{
			Position boardPos = board.getPosition(pos.getX(), pos.getY());
			boardPos.setAttributes(pos.getAttributes());
			boardPos.addAttributes("A");
		}
		
		/**
		 * Ask what Action I should take
		 * 
		 * @return
		 */
		public Action ask()
		{
			return null;
		}
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
}
