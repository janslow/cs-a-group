package grouppractical.client.commands;

/**
 * <p>Immutable class representing the command to set the speed of the robot wheels.</p>
 * <p>The robot </p>
 * @author jay
 *
 */
public class RSpeedCommand implements Command {
	/** Minimum and Maximum speed of robot (in an arbitrary unit) (12-bit signed integer) */
	public static final short MIN = -2047, MAX = 2047;
	/** Speed to set each side of the robot to (12-bit signed integer) */
	private final short left, right;
	
	/**
	 * Constructs a new SpeedCommand
	 * @param left Speed to set left motors of robot to
	 * @param right Speed to set right motors of robot to
	 */
	public RSpeedCommand(short left, short right) {
		//Forces values to be within the allowed range
		if (left > MAX) left = MAX; if (right > MAX) right = MAX;
		if (left < MIN) left = MIN; if (right < MIN) right = MIN;
		this.left = left;
		this.right = right;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.RSPEED; }
	
	/**
	 * <p>Serializes the command into an array of characters</p>
	 * <p>The array is of length three, with the elements representing the following</p>
	 * <ol>
	 * 	<li>The command type (always RDISTANCE)</li>
	 * 	<li>The Most Significant Byte (MSB) of the distance (in cm)</li>
	 * 	<li>The Least Significant Byte (MSB) of the distance (in cm)</li>
	 * </ol>
	 * <p>The last three bytes can be visualised as "LLLLLLL RRRRRRR lllrrrss" where "L" represents a bit in the Left MSB,
	 * "R" in the Right MSB, "l" and "r" are least significant bits in the left and right speeds, respectively. "ss" is the
	 * the sign/direction of the two speeds (left first, then right, 1 if forward, otherwise 0)</p>
	 * @return An array of characters representing the command
	 */
	@Override
	public char[] serialize() {
		char[] bytes = new char[4];
		//Command Type
		bytes[0] = getCommandType().toChar();
		//MSB of left speed
		bytes[1] = (char)((Math.abs(left) & 0x7F8) >> 3);
		//MSB of right speed
		bytes[2] = (char)((Math.abs(right) & 0x7F8) >> 3);
		
		//Calculate the least significant bits of 
		int left4 = (Math.abs(left) & 0x7) << 5, right4 = (Math.abs(right) & 0x7) << 2,
				leftDir = left > 0 ? 0x02 : 0x00, rightDir = right > 0 ? 0x01 : 0x00;
		//Use bitwise or to join each part of byte3 together
		bytes[3] = (char) (left4 | right4 | leftDir | rightDir);
		
		return bytes;
	}
	
	/**
	 * Gets the speed of the left motor
	 * @return Forward speed (11-bit + sign) of left motor
	 */
	public short getLeftSpeed() { return left; }
	/**
	 * Gets the speed of the right motor
	 * @return Forward speed (11-bit + sign) of right motor
	 */
	public short getRightSpeed() { return right; }
	
	@Override
	public String toString() {
		return String.format("RSPEED(%d left, %d right)", left, right); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(RSpeedCommand.class)) return false;
		RSpeedCommand rthat = (RSpeedCommand)that;
		return rthat.getLeftSpeed() == this.getLeftSpeed() && rthat.getRightSpeed() == this.getRightSpeed();
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(left ^ right).hashCode();
	}
}
