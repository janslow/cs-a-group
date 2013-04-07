package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.MListenerCommand;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MListenerCommandTest extends CommandTest {
	protected final boolean updates;
	protected final boolean isValid;
	
	protected final MListenerCommand cmd;
	
	/**
	 * Constructor for the test
	 * @param updates 'updates?' value of MListenerCommand
	 * @param isValid Is this a valid command
	 */
	public MListenerCommandTest(boolean updates, boolean isValid) {
		//Calls super constructor
		super(new MListenerCommand(updates), CommandType.MLISTENER,
				String.format("MLISTENER(%s)",updates ? "updates" : "no updates"), isValid);
		
		//Gets MListenerCommand from super
		this.cmd = (MListenerCommand)super.cmd;
		this.updates = updates;
		this.isValid = isValid;
	}
	
	/**
	 * Generates test data (both valid and invalid)
	 * @return Test Data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ true, true },
				{ false, true }
		};
		return Arrays.asList(data); 
	}
	@Override
	public void testSerialize() {
		//Calls basic test
		super.testSerialize();
		char[] actual = cmd.serialize();
		if (isValid)
			if (updates)
				assertEquals("Updates? is not correct",0x01,actual[1] & 0x01);
			else
				assertEquals("Updates? is not correct",0x00,actual[1] & 0x01);
	}
	
	@Test
	public void testGetRegisterUpdates() {
		//If dist is invalid, cmd.getRegisterUpdates() does not need to be correct
		if (isValid)
			assertEquals(updates,cmd.getRegisterUpdates());
	}
	
	@Override
	public void testEquals() {
		super.testEquals(new MListenerCommand(updates), new MListenerCommand(!updates));
	}
}
