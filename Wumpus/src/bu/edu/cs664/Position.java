package bu.edu.cs664;

import java.util.LinkedList;
import java.util.List;

/**
 * Position class defines each individual
 * board position. Each position contains
 * a zero-based X and zero-based Y coordinates
 * with position 0,0 being the top left board
 * position.
 *
 */
public class Position
{
	int x, y;
	List<Attribute> attributes = new LinkedList<Attribute>();
	
	/**
	 * Constructor requires x and y coords.
	 * @param x zero-based x coordinate
	 * @param y zero-based y coordinate
	 * @param attributes a String of attribute letters
	 */
	public Position(int x, int y, String attributes)
	{
		this.x = x;
		this.y = y;
		setAttributes(attributes);
	}
	
	/**
	 * Get the x coordinate
	 * @return x coordinate
	 */
	public int getX()
	{
		return this.x;
	}
	/**
	 * Get the y coordinate
	 * @return y coordinate
	 */
	public int getY()
	{
		return this.y;
	}
	
	/**
	 * Get the list of attributes at this
	 * position.
	 * 
	 * @return attributes
	 */
	public List<Attribute> getAttributes()
	{
		return this.attributes;
	}
	
	/**
	 * Set the attributes on the position.
	 * @param attributes attributes to set
	 */
	public void setAttributes(String attributes)
	{
		Attribute attr = null;
		for (int i = 0; i < attributes.length(); i++)
		{
			attr = Attribute.valueOf(attributes.substring(i, 1));
			this.attributes.add(attr);
		}
	}
	
	/**
	 * String form of a Position.
	 */
	public String toString()
	{
		return "x: " + x + " y:" + y + " Attr: " + attributes;
	}
}