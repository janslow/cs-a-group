package grouppractical.client.commands;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class CommandParser {
	private final Queue<Command> q;
	
	/** Array of chars that have been received already */
	private char[] chars;
	private CommandType cmdType;
	private int i;
	private final Semaphore mutex;
	
	public CommandParser() {
		//TODO: Construct Queue
		//TODO: Construct Semaphores
		mutex = null;
		q = null;
	}
	/**
	 * Gets whether the CommandParser has a command waiting to be read
	 * @return True if a command is ready, otherwise false
	 */
	public boolean isEmpty() {
		return q.size() > 0;
	}
	/**
	 * 'Pops' the currently generated command off of the parser
	 * @return
	 * @throws InterruptedException 
	 */
	public Command pop() throws InterruptedException {
		//TODO: Check documentation for java.util.Queue
		return q.poll();
	}
	public void addChar(char c) {
		// Forces the char to be an 8-bit char
		c = validateChar(c);
		/* If there is not an array of chars which have already been read, 
		 * then this char should represent the CommandType */
		if (chars == null) {
			// Get the command type represented by the char
			cmdType = CommandType.getByChar(c);
			// If the char does not represent a valid command type, ignore it
			if (cmdType == null) return;
			/* Construct a new array of chars to hold the next values, only containing
			 * this character */
			chars = new char[cmdType.getSize()];
			chars[0] = c;
			i = 1;
		// Otherwise, the character is part of another command
		} else
			chars[i++] = c;
		
		// If the array is full, i.e., all chars for this command have been read, generate the command
		
		if (i > chars.length) {
			if (chars.length == CommandType.RDISTANCE.getSize() && chars[0] == CommandType.RDISTANCE.toChar()) {
				switch (cmdType) {
				case RDISTANCE:
					q.add(parseRDistance(chars));
					break;
				case RROTATE:
					q.add(parseRRotate(chars));
					break;
				case RSPEED:
					q.add(parseRSpeed(chars));
					break;
				case RSTOP:
					q.add(new RStopCommand());
					break;
				}
			}
			chars = null;
		}
	}
	private char validateChar(char c) {
		if (c < 0) return 0;
		else if (c > 255) return 255;
		else return c;
	}
	/**
	 * Constructs a RRotateCommand from an array of chars
	 * @param chars An array of chars representing a rotate command
	 * @return A rotate command, or null if the char array is invalid
	 */
	private RRotateCommand parseRRotate(char[] chars) {
		//Generates the serialized angle from the MSB and the LSB
		int x = (chars[1] << 8) ^ chars[2];
		return RRotateCommand.constructByInteger(x);
	}
	/**
	 * Constructs a RDistanceCommand from an array of chars
	 * @param chars An array of chars representing a distance command
	 * @return A distance command, or null if the char array is invalid
	 */
	private RDistanceCommand parseRDistance(char[] chars) {
		//Generates the distance from the MSB and the LSB
		short x = (short) ((chars[1] << 8) ^ chars[2]);
		return new RDistanceCommand(x);
	}
	/**
	 * Constructs a RSpeedCommand from an array of chars
	 * @param chars An array of chars representing a speed command
	 * @return A speed command, or null if the char array is invalid
	 */
	private RSpeedCommand parseRSpeed(char[] chars) {
		//Generates the speeds from the char array
		int l = (chars[1] << 3) ^ (chars[3] >> 5); if ((chars[3] & 0x02) == 0) l = -l;
		int r = (chars[2] << 3) ^ ((chars[3] & 0x1C) >> 2); if ((chars[3] & 0x01) == 0) r = -r;
		return new RSpeedCommand((short)l,(short)r);
	}
}
