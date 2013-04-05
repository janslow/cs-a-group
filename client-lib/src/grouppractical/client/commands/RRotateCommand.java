package grouppractical.client.commands;

/**
 * <p>Immutable class representing the command to rotate the robot clockwise.</p>
 * <p>A single command can turn the robot >= 360¡ counter-clockwise or <360¡ clockwise</p>
 * @author jay
 *
 */
public class RRotateCommand implements Command {
	/** Minimum rotation in degrees, radians and serialized integer */
	public static final double MIN_DEGREES = -359.98901, MIN_RADIANS = -6.28299;
	public static final short MIN_INT = Short.MIN_VALUE + 1;
	/** Maximum rotation in degrees, radians and serialized integer */
	public static final double MAX_DEGREES = 359.98901, MAX_RADIANS = 6.28299;
	public static final short MAX_INT = Short.MAX_VALUE;
	
	/** Angle of clockwise rotation as a serialized integer. Invariant is that MIN_INT <= angle <= MAX_INT */
	private final short angle;
	
	/**
	 * <p>Constructs a new RotateCommand object, with an angle as a serialized integer between MIN_INT and MAX_INT.</p>
	 * @param angle The angle as a serialized integer
	 */
	public RRotateCommand(short angle) {
		this.angle = angle;
	}
	public static RRotateCommand constructFromRadians(double angle) {
		short s = (short) convert(angle,MIN_RADIANS,MAX_RADIANS,MIN_INT,MAX_INT);
		return new RRotateCommand(s);
	}
	public static RRotateCommand constructFromDegrees(double angle) {
		short s = (short) convert(angle,MIN_DEGREES,MAX_DEGREES,MIN_INT,MAX_INT);
		return new RRotateCommand(s);
	}

	/**
	 * Converts an angle x (where minx <= x < maxx) to an angle y (where miny <= y <= maxy)
	 * @param x Angle x to convert
	 * @param minx Minimum (inclusive) value of x
	 * @param maxx Maximum (exclusive) value of x
	 * @param miny Minimum (inclusive) value of y
	 * @param maxy Maximum (exclusive) value of y
	 * @return Converted angle y
	 */
	public static double convert(double x, double minx, double maxx, double miny, double maxy) {
		//Converts x to intermediate value z (where 0 <= z < 1)
		double z = (x - minx) / (maxx - minx);
		//Converts z to final value y
		return (z * (maxy - miny)) + miny;
	}
	
	@Override
	public CommandType getCommandType() { return CommandType.RROTATE; }
	
	/**
	 * <p>Serializes the command into an array of characters</p>
	 * <p>The array is of length three, with the elements representing the following</p>
	 * <ol>
	 * 	<li>The command type (always RROTATE)</li>
	 * 	<li>The Most Significant Byte (MSB) of the angle</li>
	 * 	<li>The Least Significant Bits (LSB) of the angle (first 7 bits) and the direction (last bit)</li>
	 * </ol>
	 * @return An array of characters representing the command
	 */
	@Override
	public char[] serialize() {
		char[] bytes = new char[3];
		int serial = getAngleSerial();
		//Command Type
		bytes[0] = getCommandType().toChar();
		//MSB of distance
		bytes[1] = (char)((Math.abs(serial) & 0x7F80) >> 7);
		//LSB of distance
		bytes[2] = (char)((Math.abs(serial) & 0x007F) << 1);
		//Sets bit 8 of the LSB to be the direction
		if (serial > 0) bytes[2] = (char) (bytes[2] | 0x01);
		
		return bytes;
	}
	
	/**
	 * Gets the angle of rotation in degrees
	 * @return Double (between >= -360¡ and < 360¡) representing the clockwise angle of rotation
	 */
	public double getAngleDegrees() { return convert(angle,MIN_INT,MAX_INT,MIN_DEGREES,MAX_DEGREES); }
	/**
	 * Gets the angle of rotation in radians
	 * @return Double (between >= -2pi and < 2pi) representing the clockwise angle of rotation
	 */
	public double getAngleRadians() { return convert(angle,MIN_INT,MAX_INT,MIN_RADIANS,MAX_RADIANS); }
	
	/**
	 * Gets the angle of rotation as a serialized integer
	 * @return 16-bit integer between 0 and 65535
	 */
	public short getAngleSerial() { return angle; }
	
	@Override
	public String toString() {
		return String.format("RROTATE(%f¡ clockwise)", getAngleDegrees());
	}
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(RRotateCommand.class)) return false;
		RRotateCommand rthat = (RRotateCommand)that;
		return rthat.getAngleSerial() == this.getAngleSerial();
	}
	
	@Override
	public int hashCode() {
		return Short.valueOf(getAngleSerial()).hashCode();
	}
}