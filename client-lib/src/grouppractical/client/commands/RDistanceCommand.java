package grouppractical.client.commands;

/**
 * <p>Immutable class representing the command to move the robot forward/backward for a particular distance.</p>
 * <p>A single command can move the robot 327.67m forward or 327.67m backward</p>
 * @author jay
 *
 */
public class RDistanceCommand implements Command {
	public static final short MIN = Short.MIN_VALUE + 1, MAX = Short.MAX_VALUE;
	/** Distance in metres to move the robot (forward is positive, backward is negative) */
	private final short distance;
	
	/**
	 * Constructs a new DistanceCommand
	 * @param distance Distance to move the robot
	 */
	public RDistanceCommand(short distance) {
		this.distance = distance;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.RDISTANCE; }
	
	/**
	 * <p>Serializes the command into an array of characters</p>
	 * <p>The array is of length three, with the elements representing the following</p>
	 * <ol>
	 * 	<li>The command type (always RDISTANCE)</li>
	 * 	<li>MSB of the distance (in cm)</li>
	 * 	<li>LSB of the distance (in cm) and the direction bit</li>
	 * </ol>
	 * @return An array of characters representing the command
	 */
	@Override
	public char[] serialize() {
		char[] bytes = new char[3];
		//Command Type
		bytes[0] = getCommandType().toChar();
		//MSB of distance
		bytes[1] = (char)((Math.abs(distance) & 0x7F80) >> 7);
		//LSB of distance
		bytes[2] = (char)((Math.abs(distance) & 0x007F) << 1);
		//Sets bit 8 of the LSB to be the direction
		if (distance > 0) bytes[2] = (char) (bytes[2] | 0x01);
		return bytes;
	}

	public short getDistance() { return distance; }
	
	@Override
	public String toString() {
		return String.format("RDISTANCE(%d cm)", distance); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(RDistanceCommand.class)) return false;
		RDistanceCommand rthat = (RDistanceCommand)that;
		return rthat.getDistance() == this.getDistance();
	}
	
	@Override
	public int hashCode() {
		return Short.valueOf(distance).hashCode();
	}
}
