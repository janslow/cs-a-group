package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import grouppractical.client.Position;
import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.MPositionCommand;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MPositionCommandTest extends CommandTest {
	protected final Position position;
	protected final boolean isValid;
	
	protected final MPositionCommand cmd;
	
	/**
	 * Constructor for the test
	 * @param Postion of the MPositionCommand
	 * @param isValid Is this a valid command
	 */
	public MPositionCommandTest(Position position, boolean isValid) {
		//Calls super constructor
		super(new MPositionCommand(position), CommandType.MPOSITION,
				String.format("MPOSITION(x: %d, y: %d, %f %% %s)", position.getX(), position.getY(),
					position.getCertaintyPercent(), position.isOccupied() ? "occupied" : "empty"),
				isValid);
		
		//Gets MPositionCommand from super
		this.cmd = (MPositionCommand)super.cmd;
		this.position = position;
		this.isValid = isValid;
	}
	
	/**
	 * Generates test data (both valid and invalid)
	 * @return Test Data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		int[] xs = new int[] { MPositionCommand.MIN_X - 1, MPositionCommand.MIN_X,
				(MPositionCommand.MIN_X + MPositionCommand.MAX_X) / 2, MPositionCommand.MAX_X,
				MPositionCommand.MAX_X + 1 },
			ys = new int[] { MPositionCommand.MIN_Y - 1, MPositionCommand.MIN_Y,
				(MPositionCommand.MIN_Y + MPositionCommand.MAX_Y) / 2, MPositionCommand.MAX_Y,
				MPositionCommand.MAX_Y + 1 };
		short[] certs = new short[] { Position.MIN_CERTAINTY - 1, Position.MIN_CERTAINTY,
				(Position.MIN_CERTAINTY + Position.MAX_CERTAINTY) / 2, Position.MAX_CERTAINTY,
				Position.MAX_CERTAINTY + 1 };
		boolean[] occupieds = new boolean[] { true, false };
		
		//Constructs list of (Command,Bool), which is the position and whether the position is valid
		Object[][] data = new Object[xs.length * ys.length * certs.length * occupieds.length][2];
		
		//Creates all positions
		int i = 0;
		for (int x : xs)
			for (int y : ys)
				for (short cert : certs)
					for (boolean o : occupieds) {
						Position p = new Position(x, y, o, cert);
						data[i][0] = p;
						data[i++][1] = isValid(p);
					}
		return Arrays.asList(data);
	}
	
	/**
	 * Checks if position is valid or not
	 * @param p Position to check
	 * @return True if position is valid, otherwise false
	 */
	private static boolean isValid(Position p) {
		return
			p.getX() >= MPositionCommand.MIN_X && p.getX() <= MPositionCommand.MAX_X &&
			p.getY() >= MPositionCommand.MIN_Y && p.getY() <= MPositionCommand.MAX_Y &&
			p.getCertainty() >= Position.MIN_CERTAINTY && p.getCertainty() <= Position.MAX_CERTAINTY;
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
		
		int ac = (actual[5] & 0xFE) >> 1;
		boolean ao = (actual[5] & 0x01) > 0;
		
		if (isValid) {
			assertEquals("X Coordinate is not correct",position.getX(),ax);
			assertEquals("Y Coordinate is not correct",position.getY(),ay);
			assertEquals("Certainty is not correct",position.getCertainty(),ac);
			assertEquals("Is Occupied is not correct",position.isOccupied(),ao);
		}
	}
	
	@Test
	public void testGetPosition() {
		//If dist is invalid, cmd.getRegisterUpdates() does not need to be correct
		if (isValid) {
			assertEquals(position, cmd.getPosition());
		}
	}
	
	@Override
	public void testEquals() {
		Position similarPosition = new Position(position.getX(), position.getY(), position.isOccupied(),
				position.getCertainty()),
			differentPosition = new Position(position.getX(), position.getY(), !position.isOccupied(),
				position.getCertainty());
		
		super.testEquals(new MPositionCommand(similarPosition), new MPositionCommand(differentPosition));
	}
}
