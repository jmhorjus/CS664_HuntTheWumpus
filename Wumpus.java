package bu.edu.cs664;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * CS-664 AI Class Lab
 * 
 * Hunt the Wumpus.
 * 
 */
public class Wumpus {

	int size[] = new int[] {4,4};
	String positions [][] = new String[][] {
			{"0", "0", "E"},
			{"0", "1", "B"},
			{"0", "2", "P"},
			{"0", "3", "B"},
			{"1", "2", "B"},
			{"2", "0", "B"},
			{"2", "2", "S"},
			{"2", "4", "B"},
			{"3", "0", "P"},
			{"3", "1", "BS"},
			{"3", "2", "W"},
			{"3", "3", "GBS"},
			{"3", "4", "P"},
			{"4", "0", "B"},
			{"4", "1", "P"},
			{"4", "2", "BS"},
			{"4", "4", "B"}
	};
	
	Board gameBoard = null;
		
	/**
	 * Program main entry point when run as application.
	 * @param args
	 */
	public static final void main(String[] args) {
		Wumpus w = new Wumpus();
		w.play();
	}
	
	/**
	 * Starts the game play.
	 */
	public void play() {
		/*
		 * Create the game board that has position attributes
		 */
		int x = size[0];
		int y = size[1];
		gameBoard = new Board(x, y);
		gameBoard.populate(positions);
		
		// Display the initial state of the full game board.
		// debugging purposes only.
		gameBoard.print();
		
		// Get the Entry point position
		Position pos = gameBoard.getFirstPositionWithAttribute("E");
		
		// These are the list of places the player can move.
		List<Position> adj = gameBoard.getAdjacentPositions(pos);
	}

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
		List<Position> positions;
		
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
			this.maxX = x + 1;
			this.maxY = y + 1;
			positions = new ArrayList<Position>(x * y);
			
			// initialize with Positions
			for (int col = 0; col <= y; col++)
			{
				for (int row = 0; row <= x; row++)
				{
					Position p = new Position(row, col, new String());
					int index = col * maxX + row;
					positions.add(index, p);
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
				int index = y * maxX + x;
				return positions.get(index);
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
				int index = y * maxX + x;
				positions.add(index, pos);
			}
			else {
				throw new IllegalArgumentException("The X or Y coordinate is invalid.");
			}
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
					if (pos.getAttributes() != null && pos.getAttributes().contains(attr))
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
				System.out.print("   " + x + "  ");
			}
			System.out.println();
			
			// Underline the initial header
			System.out.print("       ");
			for (int l = 0; l < maxX; l++)
			{
				System.out.print("______");
			}
			System.out.println("_");
			
			// Display cell contents
			for (int y = 0; y < maxY; y++)
			{
								
				for (int i = 0; i < 3; i++)
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
					String attrs = null;
					for (int x = 0; x < maxX; x++)
					{
						// Print attributes on first line
						if (i == 0)
						{
							// print the x,y position attributes
							attrs = this.getPosition(x, y).getAttributes();
							System.out.print(attrs);
							
							// Fill the cell with spaces
							System.out.print("     ".substring(0, 5 - attrs.length()));
							System.out.print("|");
						}
						else
						{
							System.out.print("     |");
						}
					}
					System.out.println();
				}
				
				// Display the bottom of the cell
				System.out.print("       ");
				for (int x = 0; x < maxX; x++)
				{
					System.out.print("______");
				}
				System.out.println("_");
			}
		}
	}
	
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
		String attributes;
		
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
			this.attributes = attributes;
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
		public String getAttributes()
		{
			return this.attributes;
		}
		
		/**
		 * String form of a Position.
		 */
		public String toString()
		{
			return "x: " + x + " y:" + attributes ;
		}
	}
}
