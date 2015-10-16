package bu.edu.cs664;

public enum Attribute {
	
	ENTRY("E"),
	
	VISITED("V"),
	
	BREEZY("B"),
	
	SMELLY("S"),
	
	GLITTERS("G"),
	
	PIT("P"),
	
	WUMPUS("W"),
	
	INFERENCE("I"),
	
	SAFE("A");
	
	private String symbol;
	
	private Attribute(String symbol)
	{
		this.symbol = symbol;
	}
	
	public String getSymbol()
	{
		return symbol;
	}
	
	public static Attribute ForSymbol(String attr)
	{
		for (Attribute a : Attribute.values())
		{
			if (attr.equals(a.getSymbol()))
			{
				return a;
			}
		}
		return null;
	}
}