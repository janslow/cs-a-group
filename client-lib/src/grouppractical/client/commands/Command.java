package grouppractical.client.commands;

/**
 * An interface for all commands that are sent to the robot
 * @author jay
 *
 */
public interface Command {
	/**
	 * Gets the type of the command
	 * @return Command Type Enumeration
	 */
	public CommandType getCommandType();
	/**
	 * Serializes the command object into an array of characters 
	 * @return An array of characters representing the command
	 */
	public char[] serialize();
}
