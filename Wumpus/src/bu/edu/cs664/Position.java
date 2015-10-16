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
	//attributes can be accessed in the ordr of the Attribute enum ordinals
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
		attributes = new Boolean[Attribute.values().length];
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
			attr = attributes2.substring(i, i + 1);
			
			if (attr.equals(Attribute.ENTRY.getSymbol()))
			{
				attributes[Attribute.ENTRY.ordinal()] = true;
			}
			else if (attr.equals(Attribute.WUMPUS.getSymbol()))
			{
				attributes[Attribute.WUMPUS.ordinal()] = true;
			}
			else if (attr.equals(Attribute.BREEZY.getSymbol()))
			{
				attributes[Attribute.BREEZY.ordinal()] = true;
			}
			else if (attr.equals(Attribute.SMELLY.getSymbol()))
			{
				attributes[Attribute.SMELLY.ordinal()] = true;
			}
			else if (attr.equals(Attribute.GLITTERS.getSymbol()))
			{
				attributes[Attribute.GLITTERS.ordinal()] = true;
			}
			else if (attr.equals(Attribute.PIT.getSymbol()))
			{
				attributes[Attribute.PIT.ordinal()] = true;
			}
			
		}
	}
	
	public void add(Attribute attr)
	{
		attributes[attr.ordinal()] = true;
	}
	
	public void remove(Attribute attr)
	{
		attributes[attr.ordinal()] = false;
	}
	
	public boolean hasAttribute(Attribute att) {
		return attributes[att.ordinal()];
	}
	
	public boolean hasEntry()
	{
		return hasAttribute(Attribute.ENTRY);
	}
	
	public boolean hasVisited()
	{
		return hasAttribute(Attribute.VISITED);
	}
	
	public boolean hasBreezy()
	{
		return hasAttribute(Attribute.BREEZY);
	}
	
	public boolean hasSmelly()
	{
		return hasAttribute(Attribute.SMELLY);
	}
	
	public boolean hasGlitter()
	{
		return hasAttribute(Attribute.GLITTERS);
	}
	
	public boolean hasPit()
	{
		return hasAttribute(Attribute.PIT);
	}
	
	public boolean hasWumpus()
	{
		return hasAttribute(Attribute.WUMPUS);
	}
	
	public boolean hasInference()
	{
		return hasAttribute(Attribute.INFERENCE);
	}
	
	public boolean hasSafe()
	{
		return hasAttribute(Attribute.SAFE);
	}
	
	/**
	 * String form of a Position.
	 */
	public String toString()
	{
		return "x: " + x + " y:" + y + " Attr: " + attributes;
	}
}
