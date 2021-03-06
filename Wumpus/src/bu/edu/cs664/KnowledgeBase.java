package bu.edu.cs664;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import bu.edu.cs664.Player.Action;
import bu.edu.cs664.Player.Direction;

/**
 * The knowledge base is the AI Logical Agent that makes decisions for the
 * Player.
 *
 */
public class KnowledgeBase {
	Board board = null; // My knowledge of the board.
	int currentX = -1; // Where I believe myself to be; x and y.
	int currentY = -1;
	Direction currentDir = null;
	boolean haveArrow = true;
	boolean wumpusDead = false;

	// Get the "position" object from the board where we are.
	private Position boardPosition() {
		return board.getPosition(currentX, currentY);
	}

	// Constructor
	public KnowledgeBase(Board newBoard, int startX, int startY, Direction startDir) {
		this.board = newBoard;
		this.currentX = startX;
		this.currentY = startY;
		this.currentDir = startDir;
	}

	public void youHaveMovedForward() {
		switch (this.currentDir) {
		case NORTH:
			this.currentY--;
			break;
		case SOUTH:
			this.currentY++;
			break;
		case EAST:
			this.currentX++;
			break;
		case WEST:
			this.currentX--;
			break;
		}
	}

	public void youHaveTurnedRight() {
		switch (this.currentDir) {
		case NORTH:
			this.currentDir = Direction.EAST;
			break;
		case SOUTH:
			this.currentDir = Direction.WEST;
			break;
		case EAST:
			this.currentDir = Direction.SOUTH;
			break;
		case WEST:
			this.currentDir = Direction.NORTH;
			break;
		}
	}

	public void youHaveTurnedLeft() {
		switch (this.currentDir) {
		case NORTH:
			this.currentDir = Direction.WEST;
			break;
		case SOUTH:
			this.currentDir = Direction.EAST;
			break;
		case EAST:
			this.currentDir = Direction.NORTH;
			break;
		case WEST:
			this.currentDir = Direction.SOUTH;
			break;
		}
	}

	public void youHaveShotTheWumpus() {
		// TODO
		this.haveArrow = false;
		this.wumpusDead = true;
	}

	public void youHaveMissedTheWumpus() {
		this.haveArrow = false;
	}

	// The game is telling me the attributes of my current position.
	public void youHaveSniffed(Position pos) {
		if (pos.x != currentX || pos.y != currentY) {
			// DEBUG
			throw new IllegalArgumentException("Position confusion detected.");
		}

		// Set the attributes given as well as the "visited" attribute on my
		// current space.
		pos.add(Attribute.VISITED);
		boardPosition().setAttributes(pos.getAttributes());
	}

	// The Game is asking me what action (or series of actions) I want to take
	// next.
	public List<Action> ask() {
		// First priority is if I know where the gold is (and I just found it
		// and am standing on it)
		// then grab it and get out via a safe (i.e. visited) path.
		if (boardPosition().hasAttribute(Attribute.GLITTERS)) {
			return grabAndGo();
		}

		// First make a list of "edge positions" which are not visited but are
		// adjacent to
		// a visited position.
		List<Position> edgePositions = new ArrayList<Position>();
		List<Position> safeEdgePositions = new ArrayList<Position>();
		for (int xx = 0; xx < board.getX(); xx++) {
			for (int yy = 0; yy < board.getY(); yy++) {
				Position pos = board.getPosition(xx, yy);
				// Not a visited position.
				if (!pos.hasAttribute(Attribute.VISITED)) {
					List<Position> adjacents = board.getAdjacentPositions(pos);
					boolean isEdge = false;
					// Adjacent to at least one visited position.
					for (Iterator<Position> adjPosIter = adjacents.iterator(); adjPosIter.hasNext();) {
						Position adjPos = adjPosIter.next();
						if (adjPos.hasAttribute(Attribute.VISITED)) {
							isEdge = true;
							edgePositions.add(pos);
							break;
						}
					}
					if (isEdge) {
						// Now we need to determine if this edge position is
						// safe.
						// a.) Must be adjacent to one visited non-breezy
						// position.
						boolean noPits = false;
						for (Iterator<Position> adjPosIter = adjacents.iterator(); adjPosIter.hasNext();) {
							Position adjPos = adjPosIter.next();
							if (adjPos.hasAttribute(Attribute.VISITED) && !adjPos.hasAttribute(Attribute.BREEZY)) {
								noPits = true;
								break;
							}
						}
						// b.) Must be adjacent to one visited non-smelly
						// position, or not-adjacent to one visited smelly
						// position.
						boolean noWumpus = wumpusDead;
						if (!noWumpus) {
							for (Iterator<Position> adjPosIter = adjacents.iterator(); adjPosIter.hasNext();) {
								Position adjPos = adjPosIter.next();
								if (adjPos.hasAttribute(Attribute.VISITED) && !adjPos.hasAttribute(Attribute.SMELLY)) {
									noWumpus = true; // adjacent to one visited
														// non-smelly position
									break;
								}
							}
						}
						if (!noWumpus) {
							// not adjacent to one visited smelly position.
							outerloop: for (int xxx = 0; xxx < board.getX(); xxx++) {
								for (int yyy = 0; yyy < board.getY(); yyy++) {
									Position pos2 = board.getPosition(xxx, yyy);
									if (!pos2.adjacentTo(pos) && pos2.hasAttribute(Attribute.VISITED)
											&& pos2.hasAttribute(Attribute.SMELLY)) {
										noWumpus = true; // adjacent to one
															// visited
															// non-smelly
															// position
										break outerloop;
									}
								}
							}
						}

						if (noWumpus && noPits) {
							// THIS IS A SAFE EDGE SPACE.
							safeEdgePositions.add(pos);
						}
					}
				}
			}
		}

		// If there are *no* safe edge positions, then we need to get fancy with
		// our arrow.
		if (safeEdgePositions.size() == 0) {
			if (haveArrow) {
				return wumpusKillCommand();
			} else {
				// We've used our arrow already and still can't safely find the
				// gold.
				// Get out of here!
				return grabAndGo();
			}
		}

		// Among safe edge positions, choose the closest one.
		Position.curX = currentX;
		Position.curY = currentY;
		Collections.sort(safeEdgePositions);
		Position destination = safeEdgePositions.get(0);

		return findPath(destination);
	}

	protected List<Action> wumpusKillCommand() {
		Position posWump = null;
		Boolean posWumpKnown = false;
		List<Action> myacts;

		for (int x = 0; x < board.getX(); x++) {
			for (int y = 0; y < board.getY(); y++) {
				// TODO: This check will never work. We never mark the wumpus on
				// the board prior to executing this function.
				if (board.getPosition(x, y).hasWumpus()) {
					posWumpKnown = true;
					posWump = board.getPosition(x, y);
				}
			}
		}

		if (posWumpKnown) {
			myacts = findPath(posWump);
		} else {
			// look for wumpus based on where smelly spots are on the board
			for (int x = 0; x < board.getX(); x++) {
				for (int y = 0; y < board.getY(); y++) {
					if (board.getPosition(x, y).hasSmelly()) {
						// Do an exhaustive, case by case search of spaces which share an adjacent space with this space.
						// (Note that we don't need to check the other 4 cases, since they are symmetrical)
						if ((x + 2 < board.getX()) && board.getPosition(x + 2, y).hasSmelly()) {
							posWumpKnown = true;
							posWump = board.getPosition(x + 1, y);
						} else if ((y + 2 < board.getY()) && board.getPosition(x, y + 2).hasSmelly()) {
							posWumpKnown = true;
							posWump = board.getPosition(x, y + 2);
						} else if ((x + 1 < board.getX()) && (y + 1 < board.getY())
								&& board.getPosition(x + 1, y + 1).hasSmelly()) {
							posWumpKnown = true;
							posWump = board.getPosition(x, y + 1);
						} else if ((x - 1 >= 0) && (y + 1 < board.getY())
								&& board.getPosition(x - 1, y + 1).hasSmelly()) {
							posWumpKnown = true;
							posWump = board.getPosition(x, y + 1);
						}
					}
				}
			}

			if (posWumpKnown) {
				myacts = findPath(posWump);
			} else {
				return null; // (unable to locate wumpus using all known
								// information about board)
				// TODO: In this case we still need to guess where he is, shoot
				// into a space that would be safe
				// if not for the wumpus, and then enter the space we just shot
				// into.
			}
		}

		// insert commands to kill wumpus with arrow before entering wumpus's
		// position
		// *assuming that command to move into wumpus's position will be the
		// last command in myacts*
		int index = myacts.size() - 2;
		myacts.add(index, Action.SHOOT);

		// After entering the wumpus's space our last action has to be to sniff
		// the air again.
		myacts.add(Action.SNIFF_AIR);

		return myacts;

	}

	// The path-finding function.
	protected List<Action> findPath(Position destination) {

		List<Action> outputActions = new ArrayList<Action>();

		// determine a series of *visited* positions that connect our position
		// to the destination.

		// Super naive solution:
		// Just try walking along a path in the general direction of the
		// destination.
		// If there's a un-visited space in our way, try another direction.
		Position here = this.boardPosition();
		Direction facing = this.currentDir;
		while (here != destination) {
			// What's the direction from here to destination?
			int distanceSouthY = destination.getY() - here.getY();
			int distanceEastX = destination.getX() - here.getX();

			Direction nextDirection = null;
			boolean lastStep = (Math.abs(distanceSouthY) + Math.abs(distanceEastX) == 1);

			// Try to find a good direction to move in that is into a visited
			// space and generally toward the goal.
			if (Math.abs(distanceSouthY) >= Math.abs(distanceEastX)) {
				// Try to go north/south toward the destination.
				if (distanceSouthY > 0 && isNextPosInDirectionOk(here, Direction.SOUTH, lastStep)) {
					// Go south; it's safe and (probably) the right way!
					nextDirection = Direction.SOUTH;
				} else if (distanceSouthY < 0 && isNextPosInDirectionOk(here, Direction.NORTH, lastStep)) {
					// Go north; it's safe and (probably) the right way!
					nextDirection = Direction.NORTH;
				}
			}

			if (nextDirection == null) {
				// Try to go north/south toward the destination.
				if (distanceEastX > 0 && isNextPosInDirectionOk(here, Direction.EAST, lastStep)) {
					// Go east; it's safe and (probably) the right way!
					nextDirection = Direction.EAST;
				} else if (distanceEastX < 0 && isNextPosInDirectionOk(here, Direction.WEST, lastStep)) {
					// Go west; it's safe and (probably) the right way!
					nextDirection = Direction.WEST;
				}
			}

			// Take a step in the next direction!
			outputActions.addAll(turnToDirection(facing, nextDirection));
			facing = nextDirection;
			outputActions.add(Action.MOVE_FORWARD);
			here = board.getNextPosInDirection(here, nextDirection);

		}

		outputActions.add(Action.SNIFF_AIR);
		return outputActions;
	}

	protected boolean isNextPosInDirectionOk(Position pos, Direction dir, boolean lastStep) {
		Position next = board.getNextPosInDirection(pos, dir);
		if (next == null) {
			return false;
		}
		if (lastStep || next.hasAttribute(Attribute.VISITED)) {
			return true;
		}
		return false;
	}

	// Takes a list of actions and appends turn actions to that list so that the
	// player will be facing the desired direction afterward.
	protected List<Action> turnToDirection(Direction st, Direction end) {
		List<Action> outputActions = new ArrayList<Action>();

		// Not the most elegant solution! Brute force, yay!
		// Cases are: Turn right, Turn Left, Turn Around, or Steady On.
		// 1.) Steady On
		if (st == end) {
			// Do nothing!
			// 2.) Turn Around
		} else if ((st == Direction.NORTH && end == Direction.SOUTH)
				|| (st == Direction.SOUTH && end == Direction.NORTH) || (st == Direction.EAST && end == Direction.WEST)
				|| (st == Direction.WEST && end == Direction.EAST)) {
			outputActions.add(Action.TURN_RIGHT);
			outputActions.add(Action.TURN_RIGHT);
			// 3.) Turn Right
		} else if ((st == Direction.NORTH && end == Direction.EAST) || (st == Direction.EAST && end == Direction.SOUTH)
				|| (st == Direction.SOUTH && end == Direction.WEST)
				|| (st == Direction.WEST && end == Direction.NORTH)) {
			outputActions.add(Action.TURN_RIGHT);
			// 4.) Turn Left
		} else if ((st == Direction.NORTH && end == Direction.WEST) || (st == Direction.WEST && end == Direction.SOUTH)
				|| (st == Direction.SOUTH && end == Direction.EAST)
				|| (st == Direction.EAST && end == Direction.NORTH)) {
			outputActions.add(Action.TURN_LEFT);
		} else {
			throw new IllegalArgumentException("The directions " + st + " and " + end + " defy logic!");
		}

		return outputActions;
	}

	protected List<Action> grabAndGo() {
		// Find the exit - we know where it is.
		Position exitPosition = board.getStartingPosition();

		List<Action> outputActions = new ArrayList<Action>();
		outputActions.add(Action.GRAB);
		outputActions.addAll(findPath(exitPosition));
		outputActions.add(Action.CLIMB);
		return outputActions;
	}

}
