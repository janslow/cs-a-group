package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.RStatusCommand;
import grouppractical.utils.map.Position;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static grouppractical.utils.StandardOps.convert;

@RunWith(Parameterized.class)
public class RStatusCommandTest extends CommandTest {
	protected final Position position;
	protected final float voltage;
	protected final short angle;
	protected final boolean isValid;
	
	protected final RStatusCommand cmd;
	
	/**
	 * Constructor for the test
	 * @param position Postion of the RStatusCommand
	 * @param voltage Voltage of the RStatusCommand
	 * @param isValid Is this a valid command
	 */
	public RStatusCommandTest(Position position, float voltage, short angle, boolean isValid) {
		//Calls super constructor
		super(new RStatusCommand(position,voltage,angle), CommandType.RSTATUS,
				String.format("RSTATUS(x: %d, y: %d, a: %f¡, %fV)", position.getX(), position.getY(), 
						convert(angle,RStatusCommand.MIN_INT,RStatusCommand.MAX_INT,RStatusCommand.MIN_DEGREES,
								RStatusCommand.MAX_DEGREES),
						voltage),
				isValid);
		
		//Gets MPositionCommand from super
		this.cmd = (RStatusCommand)super.cmd;
		this.position = position;
		this.voltage = voltage;
		this.angle = angle;
		this.isValid = isValid;
	}
	
	/**
	 * Generates test data (both valid and invalid)
	 * @return Test Data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		int[] xs = new int[] { RStatusCommand.MIN_X - 1, RStatusCommand.MIN_X,
				(RStatusCommand.MIN_X + RStatusCommand.MAX_X) / 2, RStatusCommand.MAX_X,
				RStatusCommand.MAX_X + 1 },
			ys = new int[] { RStatusCommand.MIN_Y - 1, RStatusCommand.MIN_Y,
				(RStatusCommand.MIN_Y + RStatusCommand.MAX_Y) / 2, RStatusCommand.MAX_Y,
				RStatusCommand.MAX_Y + 1 };
		float[] volts = new float[] { RStatusCommand.MIN_BATT - 1, RStatusCommand.MIN_BATT,
				RStatusCommand.MAX_BATT, RStatusCommand.MAX_BATT + 1 };
		short[] angles = new short[] { RStatusCommand.MIN_INT - 1, RStatusCommand.MIN_INT,
				(RStatusCommand.MIN_INT + RStatusCommand.MAX_INT) / 2, RStatusCommand.MAX_INT,
				(short) (RStatusCommand.MAX_INT + 1)};
		
		//Constructs list of (Command,Bool), which is the position and whether the position is valid
		Object[][] data = new Object[xs.length * ys.length * volts.length * angles.length][4];
		
		//Creates all positions
		int i = 0;
		for (int x : xs)
			for (int y : ys)
				for (float volt : volts)
					for (short angle : angles) {
						Position p = new Position(x, y, false, (short) 0);
						data[i][0] = p;
						data[i][1] = volt;
						data[i][2] = angle;
						data[i++][3] = isValid(p,volt);
					}
		return Arrays.asList(data);
	}
	
	/**
	 * Checks if position is valid or not
	 * @param p Position to check
	 * @return True if position is valid, otherwise false
	 */
	private static boolean isValid(Position p, float v) {
		return
			p.getX() >= RStatusCommand.MIN_X && p.getX() <= RStatusCommand.MAX_X &&
			p.getY() >= RStatusCommand.MIN_Y && p.getY() <= RStatusCommand.MAX_Y &&
			v >= RStatusCommand.MIN_BATT && v <= RStatusCommand.MAX_BATT;
	}
	
	@Override
	public void testSerialize() {
		//Calls basic test
		super.testSerialize();
		char[] actual = cmd.serialize();
		
		int ax = ((actual[1] & 0xFE) << 7) ^ actual[2];
		if ((actual[1] & 0x01) > 0) ax *= -1;
		int ay = ((actual[3] & 0xFE) << 7) ^ actual[4];
		if ((actual[3] & 0x01) > 0) ay *= -1;
		
		float av = (float)actual[5] / 20;
		
		if (isValid) {
			assertEquals("X Coordinate is not correct",position.getX(),ax);
			assertEquals("Y Coordinate is not correct",position.getY(),ay);
			assertEquals("Certainty is not correct",voltage,av, 0.1f);
		}
	}
	
	@Test
	public void testGetPosition() {
		//If dist is invalid, cmd.getRegisterUpdates() does not need to be correct
		if (isValid) {
			assertEquals(position, cmd.getPosition());
		}
	}
	
	@Test
	public void testGetVoltage() {
		//If dist is invalid, cmd.getRegisterUpdates() does not need to be correct
		if (isValid) {
			assertEquals(voltage, cmd.getVoltage(), 0.1f);
		}
	}
	
	@Override
	public void testEquals() {
		Position similarPosition = new Position(position.getX(), position.getY(), position.isOccupied(),
				position.getCertainty()),
			differentPosition = new Position(position.getX(), position.getY(), !position.isOccupied(),
				position.getCertainty());
		float differentVoltage = RStatusCommand.MAX_BATT - voltage;
		
		super.testEquals(new RStatusCommand(similarPosition, voltage, angle), new RStatusCommand(differentPosition, voltage, angle),
				new RStatusCommand(similarPosition, differentVoltage, angle), new RStatusCommand(differentPosition, differentVoltage, angle));
	}
}
