package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.RDistanceCommand;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RDistanceCommandTest extends CommandTest {
	protected final int dist;
	protected final boolean isValid;
	
	protected final RDistanceCommand cmd;
	
	/**
	 * Constructor for the test
	 * @param dist Distance of the RDistanceCommand
	 * @param isValid Is this a valid distance
	 */
	public RDistanceCommandTest(int dist, boolean isValid) {
		//Calls super constructor
		super(new RDistanceCommand((short) dist), CommandType.RDISTANCE,
				String.format("RDISTANCE(%d cm)",dist), isValid);
		
		//Gets RDistanceCommand from super
		this.cmd = (RDistanceCommand)super.cmd;
		this.dist = dist;
		this.isValid = isValid;
	}
	
	/**
	 * Generates test data (both valid and invalid)
	 * @return Test Data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		final int MIN = RDistanceCommand.MIN, MAX = RDistanceCommand.MAX;
		Object[][] data = new Object[][] {
				{ MIN - 1, false },
				{ MIN, true },
				{ 0, true },
				{ MAX, true },
				{ MAX + 1, false }
		};
		return Arrays.asList(data); 
	}
	@Override
	public void testSerialize() {
		//Calls basic test
		super.testSerialize();
		//Parses array, calculating distance from the char[]
		char[] actual = cmd.serialize();
		int adist = (actual[1] << 7) | ((actual[2] & 0xFE) >> 1);
		if ((actual[2] & 0x01) == 0) adist = -adist;
		//If dist is invalid, the parsed distance does not need to be correct
		if (isValid)
			assertEquals("Distance is not correct",dist,adist);
	}
	
	@Test
	public void testGetDistance() {
		//If dist is invalid, cmd.getDistance() does not need to be correct
		if (isValid)
			assertEquals(dist,cmd.getDistance());
	}
	
	@Override
	public void testEquals() {
		//Generates a different distance from dist, to use to create a different RDistanceCommand
		int diff = ((dist + 1 - RDistanceCommand.MIN) % (RDistanceCommand.MAX - RDistanceCommand.MIN + 1)) + RDistanceCommand.MIN;
		super.testEquals(new RDistanceCommand((short) dist), new RDistanceCommand((short) diff));
	}
}
