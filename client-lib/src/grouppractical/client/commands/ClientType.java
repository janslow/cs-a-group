package grouppractical.client.commands;

public enum ClientType {
	/** Client which only sends commands to the robot */
	REMOTE(0x00),
	/** Client which sends commands to the robot and receives Map Updates */
	MAPPER(0x01),
	/** Kinect App which receives Robot Status updates and lock commands and
	 * sends map updates and unlock commands */
	KINECT(0x02);
	
	private int id;
	
	/**
	 * Gets the command type represented by a specified character
	 * @param c Character representing command type
	 * @return Command Type represented by the specified character, or null if a different one can not be found
	 */
	public static ClientType getByChar(char c) {
		for (ClientType t : values())
			if (t.toChar() == c) return t;
		//If the char does not match any command types, return null
		return null;
	}
	
	/**
	 * Constructs a command
	 * @param id Unique ID of the command
	 * @param size Number of chars in a serialized version of this type of command
	 */
	private ClientType(int id) {
		this.id = id; }
	
	/** Get ID of the command */
	public int getId() {
		return id; }
	/** Gets the character which represents the command */
	public char toChar() {
		return (char)id; }
}