package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.RSpeedCommand;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RSpeedCommandTest extends CommandTest {
	private final short left, right;
	private final boolean isLeftValid, isRightValid;
	
	protected RSpeedCommand cmd;
	
	/**
	 * Constructs the test
	 * @param left Speed of left motor
	 * @param right Speed of right motor
	 * @param isLeftValid Is the left speed valid?
	 * @param isRightValid Is the right speed valid?
	 */
	public RSpeedCommandTest(short left, short right, boolean isLeftValid, boolean isRightValid){
		super(new RSpeedCommand(left,right), CommandType.RSPEED,
				String.format("RSPEED(%d left, %d right)", left, right), isLeftValid && isRightValid);
		
		cmd = (RSpeedCommand)super.cmd;
		this.left = left; this.right = right;
		this.isLeftValid = isLeftValid; this.isRightValid = isRightValid;
	}
	
	/**
	 * Generates test data (both valid and invalid)
	 * @return Test Data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		final int MIN = RSpeedCommand.MIN, MAX = RSpeedCommand.MAX;
		short[] values = new short[] { MIN-1, MIN, 0, MAX, MAX+1 };
		boolean[] validity = new boolean[] { false, true, true, true, false };
		Object[][] data = new Object[values.length * values.length][];
		
		for (int i = 0; i < values.length; i++)
			for (int j = 0; j < values.length; j++) {
				data[i*values.length+j] = new Object[]{ values[i], values[j], validity[i], validity[j] };
			}
		
		return Arrays.asList(data);
	}
	@Override
	public void testSerialize() {
		//Calls basic test
		super.testSerialize();
		//Calculates speeds from serialized char[]
		char[] actual = cmd.serialize();
		int aleft = actual[1] << 3 | (actual[3] & 0xE0) >> 5,
				aright = actual[2] << 3 | (actual[3] & 0x1C) >> 2;
		if ((actual[3] & 0x02) == 0) aleft = -aleft;
		if ((actual[3] & 0x01) == 0) aright = -aright;
		//If the speed is invalid, the calculated speed does not need to be correct
		if (isLeftValid)
			assertEquals("Left Speed is not correct",left,aleft);
		if (isRightValid)
			assertEquals("Right Speed is not correct",right,aright);
	}
	/**
	 * Tests cmd.getLeftSpeed()
	 */
	@Test
	public void testGetLeftSpeed() {
		if (isLeftValid)
			assertEquals("Left (valid) is not as expected",left,cmd.getLeftSpeed());
	}
	/**
	 * Tests cmd.getRightSpeed()
	 */
	@Test
	public void testGetRightSpeed() {
		if (isRightValid)
			assertEquals("Right (valid) is not as expected",right,cmd.getRightSpeed());
	}

	@Override
	public void testEquals() {
		//Generates different speeds from left and right, for generating different RSpeedCommands
		short diffLeft = (short) (((left + 1 - RSpeedCommand.MIN) % (RSpeedCommand.MAX - RSpeedCommand.MIN + 1))
				+ RSpeedCommand.MIN),
			diffRight = (short) (((right + 1 - RSpeedCommand.MIN) % (RSpeedCommand.MAX - RSpeedCommand.MIN + 1))
				+ RSpeedCommand.MIN);
		super.testEquals(new RSpeedCommand(left, right),
				new RSpeedCommand(left,diffRight),new RSpeedCommand(diffLeft,diffRight),
				new RSpeedCommand(diffLeft,right));
	}
}
