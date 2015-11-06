package bu.edu.cs664;

import java.util.LinkedList;
import java.util.List;

import bu.edu.cs664.Player.Direction;

/**
 * Board class understands how to create a board
 * of given dimensions, and how to populate each
 * position with attributes and how to retrieve
 * the attributes available at a position.
 *
 */
public class Board 
{
	// MaxX and MaxY are 1-based maximum X and maximum Y positions.
	// So for a Size=4,4 board, maxX and maxY will be 5.
	int maxX, maxY;
	Position[][] positions;
	String[] possibleAttributes = {
			Attribute.ENTRY.getSymbol(), 
			Attribute.VISITED.getSymbol(),
			Attribute.BREEZY.getSymbol(),
			Attribute.SMELLY.getSymbol(),
			Attribute.GLITTERS.getSymbol(),
			Attribute.PIT.getSymbol(),
			Attribute.WUMPUS.getSymbol(),
			Attribute.INFERENCE.getSymbol(),
			Attribute.SAFE.getSymbol()
	};
	
	/**
	 * Board Constructor. Define a board based on
	 * the 'Size=X,Y' definition of a board. These
	 * are 0-based integer values. So a Size=4,4 board
	 * will be a grid of 5 rows and 5 columns.
	 * 
	 * @param x zero-based row definition
	 * @param y zero-based column definition
	 */
	public Board(int x, int y) 
	{
		this.maxX = x;
		this.maxY = y;
		positions = new Position[x][y];
		
		// initialize with Positions
		for (int col = 0; col < y; col++)
		{
			for (int row = 0; row < x; row++)
			{
				Position p = new Position(row, col, new String());
				positions[row][col] = p;
			}
		}
	}
	
	/**
	 * Get a board positions.
	 * @param x zero-based x coordinate
	 * @param y zero-based y coordinate
	 * @return the Position, or null if the coordinates
	 *     specified are invalid
	 */
	public Position getPosition(int x, int y)
	{
		if (x > -1 && x < maxX && y > -1 && y < maxY)
		{
			return positions[x][y];
		}
		return null;
	}
	
	/**
	 * Set the contents of a board position.
	 * @param x zero-based x coordinate
	 * @param y zero-based y coordinate
	 * @param attrs attribute string
	 */
	public void setPosition(int x, int y, String attrs)
	{
		if (x > -1 && x < maxX && y > -1 && y < maxY)
		{
			Position pos = new Position(x, y, attrs);
			positions[x][y] = pos;
		}
		else {
			throw new IllegalArgumentException("The X or Y coordinate is invalid.");
		}
	}

	/**
	 * Get the starting position of the board.
	 * @return starting Position
	 */
	public Position getStartingPosition()
	{
		return getFirstPositionWithAttribute(Attribute.ENTRY.getSymbol());
	}
	
	/**
	 * Search from 0,0 sequentially for the first
	 * position that contains the specified attribute. The
	 * attr is expected to be a single character string. 
	 * To find the coordinate of the Entry cell for example,
	 * pass in the string: : "E".
	 * 
	 * @param attr single character string
	 * @return the Position
	 */
	public Position getFirstPositionWithAttribute(String attr)
	{
		// Check that only a single attribute was specified
		if (attr.length() != 1)
		{
			throw new IllegalArgumentException("Only a single attribute may be searched for!");
		}
				
		// Search for attribute specified
		Position pos = null;
		for (int y = 0; y < maxY; y++)
		{
			for (int x = 0; x < maxX; x++)
			{
				pos = this.getPosition(x, y);
				if (pos.getAttributes() != null && 
						((pos.getAttributes())[Attribute.ForSymbol(attr).ordinal()]))
				{
					return pos;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a list of adjacent positions that the player
	 * can move into, from the position specified. This abides
	 * by the rules that the player cannot move diagonally and
	 * knows where the board boundaries are.
	 * 
	 * @param pos a position
	 * @return list of adjacent positions
	 */
	public List<Position> getAdjacentPositions(Position pos)
	{
		return this.getAdjacentPositions(pos.getX(), pos.getY());
	}
	
	/**
	 * Returns a list of adjacent positions that the player
	 * can move into, from the position specified. This abides
	 * by the rules that the player cannot move diagonally and
	 * knows where the board boundaries are.
	 * 
	 * @param x zero-based x coordinate
	 * @param y zero-based y coordinate
	 * @return a List of Position objects that are adjacent (ie.
	 *         the player may move into to) to the passed in 
	 *         coordinates.
	 */
	public List<Position> getAdjacentPositions(int x, int y)
	{
		Position pos = null;
		LinkedList<Position> list = new LinkedList<Position>();
		
		// Get the x - 1, y position
		pos = this.getPosition(x - 1, y);
		if (pos != null)
		{
			list.add(pos);
		}
		
		// Get the x + 1, y position
		pos = this.getPosition(x + 1, y);
		if (pos != null)
		{
			list.add(pos);
		}
		
		// Get the y - 1, y position
		pos = this.getPosition(x, y - 1);
		if (pos != null)
		{
			list.add(pos);
		}
		
		// Get the y + 1, y position
		pos = this.getPosition(x, y + 1);
		if (pos != null)
		{
			list.add(pos);
		}
		
		return list;
	}
	
	
	// Returns the next position in the board which is in the 
	// specified direction from the given start.
	public Position getNextPosInDirection(Position start, Direction dir)
	{
		Position retVal = null;
		switch(dir) {
		case NORTH:
			retVal = this.getPosition(start.getX(), start.getY()-1);
			break;
		case SOUTH:
			retVal = this.getPosition(start.getX(), start.getY()+1);
			break;
		case EAST:
			retVal = this.getPosition(start.getX()+1, start.getY());
			break;
		case WEST:
			retVal = this.getPosition(start.getX()-1, start.getY());
			break;
		}
		return retVal;
	}
	
	/**
	 * Populate the board from a list of positions.
	 * @param positions
	 */
	public void populate(String[][] positions)
	{
		for (String[] position : positions)
		{
			// y position is vertical
			Integer y = Integer.valueOf(position[0]);
			
			// x is horizontal
			Integer x = Integer.valueOf(position[1]);
			
			// Set attrs
			setPosition(x, y, position[2]);
		}
	}
	
	/**
	 * Print an ASCII view of the this board, its positions
	 * and the attribute contents.
	 */
	public void print()
	{
		System.out.println();
		
		// Print out the X header across the top
		System.out.print("       ");
		for (int x = 0; x < maxX; x++)
		{
			System.out.print("    " + x + "     ");
		}
		System.out.println();
		
		// Underline the initial header
		System.out.print("       ");
		for (int l = 0; l < maxX; l++)
		{
			System.out.print("__________");
		}
		System.out.println("_");
		
		// Display cell contents
		for (int y = 0; y < maxY; y++)
		{
							
			for (int i = 0; i < 2; i++)
			{
				// First line of a cell has the Y coord
				if (i == 0)
				{
					System.out.print("  " + y + "    |");
				}
				// Other wise just an initial margin spacer
				else 
				{
					System.out.print("       |");
				}
				
				// Now we print the cell attributes (if any)
				Boolean[] attrs;
				for (int x = 0; x < maxX; x++)
				{
					// Print attributes on first line
					if (i == 0)
					{
						// print the x,y position attributes
						attrs = this.getPosition(x, y).getAttributes();
						
						//print the attributes
						int printed = 0;
						for (int p=0; p<attrs.length; p++)
						{
							if (attrs[p] == true)
							{
								System.out.print(possibleAttributes[p]);
								printed++;
							}
						}
						System.out.print("          ".substring(0, 9 - printed));
						System.out.print("|");
					}
					else
					{
						System.out.print("         |");
					}
				}
				System.out.println();
			}
			
			// Display the bottom of the cell
			System.out.print("       ");
			for (int x = 0; x < maxX; x++)
			{
				System.out.print("__________");
			}
			System.out.println("_");
		}
	}
	
	
	//***Overloaded method to use current x and y positions as input in order to display player on board***
	/**
	 * Print an ASCII view of the this board, its positions
	 * and the attribute contents.
	 */
	public void print(int myCurrX, int myCurrY)
	{
		System.out.println();
		
		// Print out the X header across the top
		System.out.print("       ");
		for (int x = 0; x < maxX; x++)
		{
			System.out.print("    " + x + "     ");
		}
		System.out.println();
		
		// Underline the initial header
		System.out.print("       ");
		for (int l = 0; l < maxX; l++)
		{
			System.out.print("__________");
		}
		System.out.println("_");
		
		// Display cell contents
		for (int y = 0; y < maxY; y++)
		{
							
			for (int i = 0; i < 2; i++)
			{
				// First line of a cell has the Y coord
				if (i == 0)
				{
					System.out.print("  " + y + "    |");
				}
				// Other wise just an initial margin spacer
				else 
				{
					System.out.print("       |");
				}
				
				// Now we print the cell attributes (if any)
				Boolean[] attrs;
				for (int x = 0; x < maxX; x++)
				{
					// Print attributes on first line
					if (i == 0)
					{
						// print the x,y position attributes
						attrs = this.getPosition(x, y).getAttributes();
						
						//print the attributes
						int printed = 0;
						for (int p=0; p<attrs.length; p++)
						{
							if (attrs[p] == true)
							{
								System.out.print(possibleAttributes[p]);
								printed++;
							}
						}
						if (x==myCurrX && y==myCurrY)
						{
							System.out.print("@");
							printed++;
						}
						System.out.print("          ".substring(0, 9 - printed));
						System.out.print("|");
					}
					else
					{
						System.out.print("         |");
					}
				}
				System.out.println();
			}
			
			// Display the bottom of the cell
			System.out.print("       ");
			for (int x = 0; x < maxX; x++)
			{
				System.out.print("__________");
			}
			System.out.println("_");
		}
	}
	
	
	/**
	 * Get initial x size of board
	 * @return
	 */
	public int getX() 
	{
		return maxX;
	}
	
	/**
	 * Return the y size of the board
	 * @return
	 */
	public int getY() 
	{
		return maxY;
	}
}
