package grouppractical.client;

/**
 * Immutable class representing a position on the map
 * @author janslow
 *
 */
public class Position {
	/**
	 * Range of certainty values. min <= certainty < max
	 */
	public static short MIN_CERTAINTY = 0, MAX_CERTAINTY = 256;
	
	private final int x;
	private final int y;
	
	private final boolean occupied;
	private final short certainty;
	
	/**
	 * Constructs a new position object
	 * @param x x-coordinate of position
	 * @param y y-coordinate of position
	 * @param occupied Is the position occupied?
	 * @param certainty Certainty that the position is or isn't occupied
	 */
	public Position(int x, int y, boolean occupied, short certainty) {
		this.x = x; this.y = y;
		this.occupied = occupied;
		this.certainty = certainty;
	}
	
	/**
	 * Gets the x-coordinate of position
	 * @return x-coordinate (in pixels)
	 */
	public int getX() { return x; }
	/**
	 * Gets the y-coordinate of position
	 * @return y-coordinate (in pixels)
	 */
	public int getY() { return y; }
	
	/**
	 * Is the position occupied
	 * @return True if the position is occupied, otherwise false
	 */
	public boolean isOccupied() { return occupied; }
	/**
	 * Certainty that the position is or isn't occupied
	 * @return Certainty of the value of isOccupied()
	 */
	public int getCertainty() { return certainty; }
}
