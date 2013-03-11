package grouppractical.client.commands;

/**
 * <p>Immutable class representing the command to rotate the robot clockwise.</p>
 * <p>A single command can turn the robot >= 360¡ counter-clockwise or <360¡ clockwise</p>
 * @author jay
 *
 */
public class RRotateCommand implements Command {
	/** Minimum rotation in degrees, radians and serialized integer */
	public static final double MIN_DEGREES = -360, MIN_RADIANS = -6.28319, MIN_INT = Short.MIN_VALUE;
	/** Maximum rotation in degrees, radians and serialized integer */
	public static final double MAX_DEGREES = 359.98901, MAX_RADIANS = 6.28299, MAX_INT = Short.MAX_VALUE;
	/** Angle of clockwise rotation in degrees. Invariant is that -360 <= angle < 360 */
	private final double angle;
	
	/**
	 * <p>Constructs a new RotateCommand object, with an angle in degrees.</p>
	 * <p>If the angle is out of the allowed range, the relevant bound is chosen (i.e., -370¡ becomes -360¡)
	 * @param angle The angle in degrees
	 */
	public RRotateCommand(double angle) {
		angle = MIN_DEGREES <= angle ? angle : MIN_DEGREES;
		angle = angle <= MAX_DEGREES ? angle : MAX_DEGREES;
		this.angle = angle;
	}
	public static RRotateCommand constructByRadian(double angle) {
		double d = convert(angle,MIN_DEGREES,MAX_DEGREES,MIN_RADIANS,MAX_RADIANS);
		return new RRotateCommand(d);
	}
	public static RRotateCommand constructByInteger(int angle) {
		double d = convert(angle,MIN_DEGREES,MAX_DEGREES,MIN_INT,MAX_INT);
		return new RRotateCommand(d);
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
	 * 	<li>The Least Significant Byte (LSB) of the angle</li>
	 * </ol>
	 * @return An array of characters representing the command
	 */
	@Override
	public char[] serialize() {
		char[] bytes = new char[3];
		//Command Type
		bytes[0] = getCommandType().toChar();
		int serialangle = getAngleSerial();
		//MSB of serialized angle
		bytes[1] = (char)(serialangle >> 8);
		//LSB of serialized angle
		bytes[2] = (char)(serialangle & 0xFF);
		return bytes;
	}
	
	/**
	 * Gets the angle of rotation in degrees
	 * @return Double (between >= -360¡ and < 360¡) representing the clockwise angle of rotation
	 */
	public double getAngleDegrees() { return angle; }
	/**
	 * Gets the angle of rotation in radians
	 * @return Double (between >= -2pi and < 2pi) representing the clockwise angle of rotation
	 */
	public double getAngleRadians() { return convert(angle,MIN_DEGREES,MAX_DEGREES,MIN_RADIANS,MAX_RADIANS); }
	
	/**
	 * Gets the angle of rotation as a serialized integer
	 * @return 16-bit integer between 0 and 65535
	 */
	public short getAngleSerial() { return (short)convert(angle,MIN_DEGREES,MAX_DEGREES,MIN_INT,MAX_INT); }
}
