package bu.edu.cs664;

/**
 * CS-664 AI Class Lab
 * 
 * Hunt the Wumpus.
 * 
 */
public class Wumpus {

	int size[] = new int[] {5,5};
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
		
		Player player = new Player(gameBoard);
		player.play();
	}
}
