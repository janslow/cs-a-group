package grouppractical.client;

import java.util.EventListener;

/**
 * Interface to be implemented by any class which needs updates about the map
 * @author janslow
 *
 */
public interface MapListener extends EventListener {
	/**
	 * Called when a position on the map is updated
	 * @param x X coordinate of updated position
	 * @param y Y coordinate of updated position
	 * @param occupied True if the position is now occupied, otherwise false
	 * @param certainty Certainty that the value of 'occupied' is correct
	 */
	public void updatePosition(int x, int y, boolean occupied, int certainty);
}