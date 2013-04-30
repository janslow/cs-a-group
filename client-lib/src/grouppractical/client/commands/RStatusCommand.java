package grouppractical.client.commands;

import grouppractical.client.Position;

/**
 * <p>Immutable class representing the status of the robot.</p>
 * <p><i>Note, the certainty and occupied values of the position are ignored by this command</i></p>
 * @author jay
 *
 */
public class RStatusCommand implements Command {
	public static final int MIN_X = -16384, MIN_Y = -16384, MAX_X = 16384, MAX_Y = 16384;
	public static final float MIN_BATT = 0f, MAX_BATT = 12f;
	
	private final Position position;
	private final float voltage;
	
	/**
	 * Constructs a new Status Command
	 * @param position Position of the robot
	 * @param voltage Battery Level of robot in volts
	 */
	public RStatusCommand(Position position, float voltage) {
		this.position = position;
		voltage = voltage > MAX_BATT ? MAX_BATT : voltage;
		voltage = voltage < MIN_BATT ? MIN_BATT : voltage;
		this.voltage = voltage;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.RSTATUS;
	}

	@Override
	public char[] serialize() {
		char[] bytes = new char[6];
		//Command Type
		bytes[0] = getCommandType().toChar();
		
		int x = Math.abs(position.getX()), y = Math.abs(position.getY());
		boolean x_negative = position.getX() < 0, y_negative = position.getY() < 0;
		
		float v = voltage > MAX_BATT ? MAX_BATT : (voltage < MIN_BATT ? MIN_BATT : voltage);
		v = v * 20;
		//X coordinate
		bytes[1] = (char) ((0xFE & (x >> 7)) ^ (x_negative ? 0x01 : 0x00));
		bytes[2] = (char) (0xFF & x);
		//Y coordinate
		bytes[3] = (char) ((0xFE & (y >> 7)) ^ (y_negative ? 0x01 : 0x00));
		bytes[4] = (char) (0xFF & y);
		//Battery Voltage
		bytes[5] = (char) (0xFF & (int)v);
		return bytes;
	}
	
	/**
	 * Gets the position of the robot
	 * @return Position of robot
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Gets the voltage of the robot
	 * @return Battery voltage of robot
	 */
	public float getVoltage() {
		return voltage;
	}
	
	@Override
	public String toString() {
		return String.format("RSTATUS(x: %d, y: %d, %fV)", position.getX(), position.getY(), voltage); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(RStatusCommand.class)) return false;
		RStatusCommand rthat = (RStatusCommand)that;
		return rthat.getPosition().equals(this.getPosition()) && rthat.getVoltage() == this.getVoltage();
	}
	
	@Override
	public int hashCode() {
		return getPosition().hashCode();
	}
}
