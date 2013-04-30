package grouppractical.client;

import java.util.EventListener;

/**
 * Interface to be implemented by any class which needs to receive updates about the robot
 * @author janslow
 *
 */
public interface RobotListener extends EventListener {
	/**
	 * Called when the position of the robot changes
	 * @param x Current x-position of the robot
	 * @param y Current y-position of the robot
	 * @param angle Current angle of the robot
	 */
	public void updatePosition(int x, int y, float angle);
	/**
	 * Called when the voltage of the robot changes
	 * @param v Current battery voltage of the robot
	 */
	public void updateVoltage(double v);
}
