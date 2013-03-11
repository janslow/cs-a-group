package grouppractical.client.commands;

/**
 * <p>Immutable class representing the command to move the robot forward/backward for a particular distance.</p>
 * <p>A single command can move the robot 327.67m forward or 327.68m backward</p>
 * @author jay
 *
 */
public class RDistanceCommand implements Command {
	public static final int MIN = Short.MIN_VALUE, MAX = Short.MAX_VALUE;
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
	 * 	<li>The Most Significant Byte (MSB) of the distance (in cm)</li>
	 * 	<li>The Least Significant Byte (LSB) of the distance (in cm)</li>
	 * </ol>
	 * @return An array of characters representing the command
	 */
	@Override
	public char[] serialize() {
		char[] bytes = new char[3];
		//Command Type
		bytes[0] = getCommandType().toChar();
		//MSB of distance
		bytes[1] = (char)(distance >> 8);
		//LSB of distance
		bytes[2] = (char)(distance & 0xFF);
		return bytes;
	}

	public short getDistance() { return distance; }
	
	@Override
	public String toString() {
		return String.format("RDISTANCE(%d cm)", distance); }
}
