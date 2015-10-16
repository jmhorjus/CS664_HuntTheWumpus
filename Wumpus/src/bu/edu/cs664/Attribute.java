package bu.edu.cs664;

public enum Attribute {
	
	// Position is OK
	OK("OK"),
	
	// Position was Visited
	VISITED("V"),
	
	// Possible Pit location
	POSSIBLE_PIT("P?"),
	
	// Position is a Pit
	IS_PIT("P"),
	
	// Breeze at Position
	BREEZE("B"),
	
	// Stench at Position
	STENCH("S"),
	
	// Wumpus at Position
	WUMPUS("W"),
	
	// Agent current location
	AGENT("A");
	
	private String attr;
	
	private Attribute(String attr) 
	{
		this.attr = attr;
	}
	
	public String getAttribute()
	{
		return attr;
	}
	
}
