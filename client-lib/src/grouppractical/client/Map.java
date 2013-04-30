package grouppractical.client;

/**
 * Read-only interface representing a map
 * @author janslow
 *
 */
public interface Map extends Iterable<Position> {
	/**
	 * Gets the height of the map
	 * @return Size in the y-direction in pixels
	 */
	public int getHeight();
	/**
	 * Gets the width of the map
	 * @return Size in the x-direction in pixels
	 */
	public int getWidth();
	/**
	 * Gets a position on a map
	 * @return Position object representing the specified coordinates
	 */
	public Position getPosition(int x, int y);
	/**
	 * Sets a position on a map
	 * @param p Position object representing a coordinate
	 */
	public void setPosition(Position p);
}