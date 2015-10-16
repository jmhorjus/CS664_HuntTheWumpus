package bu.edu.cs664;

import java.util.List;

import bu.edu.cs664.Player.Action;

/**
 * The knowledge base is the AI Logical Agent
 * that makes decisions for the Player.
 *
 */
public class KnowledgeBase {
	Board board = null; // My knowledge of the board.
	Position currentPos = null; // Where I believe myself to be; x and y.
	
	
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
	
	
	public void smellTheAir(List<Attribute> attributes)
	{
		attributes.add(Attribute.VISITED);
		board.setPosition(currentPos.getX(), currentPos.getY(), attributes);
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
