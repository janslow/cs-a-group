package grouppractical.client;

import grouppractical.utils.map.Map;
import grouppractical.utils.map.Position;

import java.util.EventListener;

/**
 * Interface to be implemented by any class which needs updates about the map
 * @author janslow
 *
 */
public interface MapListener extends EventListener {
	/**
	 * Called when a position on the map is updated
	 * @param position Updated position object
	 */
	public void updateMapPosition(Position position);
	
	/**
	 * Called when the entire map is updated (including when the dimensions are changed)
	 * @param map New Map object
	 */
	public void updateMap(Map map);
}