package grouppractical.client.commands;

/**
 * An enumeration of all CommandTypes that can be sent to the server.
 * Each command has a unique ID (which can be converted into a char)
 * @author jay
 *
 */
public enum CommandType {
	/** Stops the robot */
	RSTOP(0x00,1),
	/** Sets the speed of the robot */
	RSPEED(0x01,4),
	/** Orders the robot to move a specified distance */
	RDISTANCE(0x02,3),
	/** Rotates the robot by a specified angle */
	RROTATE(0x03,3);
	
	/** Unique ID of the commad */
	final int id, size;
	
	/**
	 * Gets the command type represented by a specified character
	 * @param c Character representing command type
	 * @return Command Type represented by the specified character, or null if a different one can not be found
	 */
	public static CommandType getByChar(char c) {
		for (CommandType t : values())
			if (t.toChar() == c) return t;
		//If the char does not match any command types, return null
		return null;
	}
	
	/**
	 * Constructs a command
	 * @param id Unique ID of the command
	 * @param size Number of chars in a serialized version of this type of command
	 */
	private CommandType(int id, int size) {
		this.id = id; this.size = size; }
	
	/** Get ID of the command */
	public int getId() {
		return id; }
	/** Gets the character which represents the command */
	public char toChar() {
		return (char)id; }
	/**
	 * Gets the size of the serialized command, in characters
	 * @return Number of chars in a serialized version of this type of command
	 */
	public int getSize() {
		return size; }
}