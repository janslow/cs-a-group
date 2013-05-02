package grouppractical.client;

import grouppractical.utils.map.Position;

import java.util.EventListener;

/**
 * Interface to be implemented by any class which needs to receive updates about the robot
 * @author janslow
 *
 */
public interface RobotListener extends EventListener {
	/**
	 * Called when the position of the robot changes
	 * @param position Current position of the robot
	 */
	public void updateRobotPosition(Position position);
	/**
	 * Called when the angle of the robot changes
	 * @param degrees Current angle of the robot, in degrees
	 * @param radians Current angle of the robot, in radians
	 */
	public void updateAngle(double degrees, double radians);
	/**
	 * Called when the voltage of the robot changes
	 * @param voltage Current battery voltage of the robot
	 */
	public void updateVoltage(float voltage);
	/**
	 * Called when the robot's movement is locked or unlocked
	 * @param locked True if locked, otherwise false
	 */
	public void updateLocked(boolean locked);
}
