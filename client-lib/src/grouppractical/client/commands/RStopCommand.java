package grouppractical.client.commands;

/**
 * <p>Immutable class representing the command to stop the robot.</p>
 * <p>This command is different to setting the speed to zero, as the robot will use electrical braking when it
 * receives this command, increasing the rate of loss of momentum.</p>
 * @author jay
 *
 */
public class RStopCommand implements Command {
	@Override
	public CommandType getCommandType() {
		return CommandType.RSTOP; }
	
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
		char[] bytes = new char[1];
		//Command Type
		bytes[0] = getCommandType().toChar();
		return bytes;
	}
}
