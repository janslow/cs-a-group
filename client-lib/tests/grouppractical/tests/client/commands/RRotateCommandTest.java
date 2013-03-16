package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.RRotateCommand;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RRotateCommandTest extends CommandTest {
	protected final int serial;
	protected final double degrees;
	protected final boolean isValid;
	
	protected final RRotateCommand cmd;
	
	/**
	 * Constructor for the test
	 * @param serial Angle of the RRotateCommand as a serialized integer
	 * @param degrees Angle of the RRotateCommand in degrees
	 * @param isValid Is this a valid angle
	 */
	public RRotateCommandTest(int serial, double degrees, boolean isValid) {
		super(new RRotateCommand((short) serial), CommandType.RROTATE, "", isValid);
		
		this.cmd = (RRotateCommand)super.cmd;
		this.serial = serial; this.degrees = degrees;
		this.isValid = isValid;
		
		super.e_string = String.format("RROTATE(%f¡ clockwise)",cmd.getAngleDegrees());
	}
	
	/**
	 * Generates test data (both valid and invalid)
	 * @return Test Data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		final int MIN = RRotateCommand.MIN_INT, MAX = RRotateCommand.MAX_INT;
		final double MIN_DEG = RRotateCommand.MIN_DEGREES, MAX_DEG = RRotateCommand.MAX_DEGREES;
		Object[][] data = new Object[][] {
				{ MIN - 1, MIN_DEG - 0.02, false },
				{ MIN, MIN_DEG, true },
				{ 0, 0, true },
				{ MAX, MAX_DEG, true },
				{ MAX + 1, MAX_DEG + 0.02, false }
		};
		return Arrays.asList(data);
	}
	
	@Override
	public void testSerialize() {
		//Calls basic tests
		super.testSerialize();
		//Calculates angle from serialized data
		char[] actual = cmd.serialize();
		int aserial = (actual[1] << 7) | ((actual[2] & 0xFE) >> 1);
		if ((actual[2] & 0x01) == 0) aserial = -aserial;
		//If the angle is not valid, 'actual' does not need to be correct
		if (isValid)
			assertEquals("Angle (Serialised) is not correct",serial,aserial);
	}
	
	/**
	 * Tests cmd.getAngleSerial()
	 */
	@Test
	public void testGetAngleSerial() {
		if (isValid)
			assertEquals(serial,cmd.getAngleSerial());
	}
	/**
	 * Tests cmd.getAngleDegrees(), within 0.015¡
	 */
	@Test
	public void testGetAngleDegrees() {
		if (isValid)
			assertEquals(degrees,cmd.getAngleDegrees(), 0.015);
	}
	/**
	 * Tests cmd.getAngleRadians(), within 0.00026 radians
	 */
	@Test
	public void testGetAngleRadians() {
		if (isValid)
			assertEquals(Math.toRadians(degrees),cmd.getAngleRadians(), 0.00026);
	}

	@Override
	public void testEquals() {
		//Generates different angle, to create a different RRotateCommand
		int diff = ((serial + 1 - RRotateCommand.MIN_INT) % (RRotateCommand.MAX_INT - RRotateCommand.MIN_INT + 1))
				+ RRotateCommand.MIN_INT;
		super.testEquals(new RRotateCommand((short) serial), new RRotateCommand((short) diff));
	}
}
