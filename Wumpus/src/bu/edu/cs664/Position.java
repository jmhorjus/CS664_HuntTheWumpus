package bu.edu.cs664;


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
	//attributes can be accessed in the following order:  Entry, visited, breezy, smelly, glitters, pit, wumpus, inference, safe    
	Boolean[] attributes;
	
	/**
	 * Constructor requires x and y coords.
	 * @param x zero-based x coordinate
	 * @param y zero-based y coordinate
	 * @param attributes a String of attribute letters
	 */
	public Position(int x, int y, String attrib)
	{
		this.x = x;
		this.y = y;
		attributes = new Boolean[9];
		for (int i=0; i<attributes.length; i++)
		{
			attributes[i] = false;
		}
		setAttributes(attrib);
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
	public Boolean[] getAttributes()
	{
		return this.attributes;
	}
	
	/**
	 * Set the attributes on the position.
	 * @param attributes2 attributes to set
	 */
	public void setAttributes(String attributes2)
	{
		String attr = null;
		for (int i = 0; i < attributes2.length(); i++)
		{
			attr = attributes2.substring(i, 1);
			
			if (attr.equals("E"))
			{
				attributes[0] = true;
			}
			else if (attr.equals("W"))
			{
				attributes[6] = true;
			}
			else if (attr.equals("B"))
			{
				attributes[2] = true;
			}
			else if (attr.equals("S"))
			{
				attributes[3] = true;
			}
			else if (attr.equals("G"))
			{
				attributes[4] = true;
			}
			else if (attr.equals("P"))
			{
				attributes[5] = true;
			}
			
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
