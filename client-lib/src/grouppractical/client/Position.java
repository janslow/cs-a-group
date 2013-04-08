package grouppractical.client;


/**
 * Immutable class representing a position on the map
 * @author janslow
 *
 */
public class Position {
	/**
	 * Range of certainty values. min <= certainty <= max
	 */
	//Because the certainty is encoded to an 8-bit character, min >= 0 and max <= 256 
	public final static short MIN_CERTAINTY = 0, MAX_CERTAINTY = 127;
	
	/**
	 * Converts a certainty value into a percentage
	 * @param c Certainty value, where MIN_CERTAINTY <= c <= MAX_CERTAINTY
	 * @return Certainty value, where 0.0 <= percent <= 1.0
	 */
	public static float certaintyToPercent(short c) {
		if (c > MAX_CERTAINTY) c = MAX_CERTAINTY;
		if (c < MIN_CERTAINTY) c = 0;
		
		float x = c - MIN_CERTAINTY;
		float y = MAX_CERTAINTY - MIN_CERTAINTY;
		
		return x/y;
	}
	/**
	 * Converts a percentage certainty value into a certainty value
	 * @param c Certainty value, where 0.0 <= percent <= 1.0
	 * @return Certainty value, where MIN_CERTAINTY <= c <= MAX_CERTAINTY
	 */
	public static short percentToCertainty(float p) {
		float y = MAX_CERTAINTY - MIN_CERTAINTY;
		float x = p * y;
		
		short c = (short) (x + MIN_CERTAINTY);
		
		if (c > MAX_CERTAINTY) c = MAX_CERTAINTY;
		if (c < MIN_CERTAINTY) c = 0;
		
		return c;
	}
	
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
	public short getCertainty() { return certainty; }
	/**
	 * Certainty that the position is or isn't occupied
	 * @return Certainty of the value of isOccupied(), between 0 and 1
	 */
	public float getCertaintyPercent() { return certaintyToPercent(certainty); }
	
	@Override
	public String toString() {
		return String.format("position(x: %d, y: %d, %f % %s)", getX(), getY(),
				getCertaintyPercent(), isOccupied() ? "occupied" : "empty"); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(Position.class)) return false;
		Position pthat = (Position)that;
		return pthat.getX() == this.getX() && pthat.getY() == this.getY() &&
				pthat.getCertainty() == this.getCertainty() && pthat.isOccupied() == this.isOccupied();
	}
	
	@Override
	public int hashCode() {
		return (getX()+1) * (getY()+1) * (getCertainty()+1) * (isOccupied() ? 1 : 2);
	}
}
