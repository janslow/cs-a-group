/**
 * 
 */
package grouppractical.tests.client.commands;

import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.MInitialiseCommand;

import org.junit.Test;

/**
 * @author jay
 *
 */
public class MInitializeCommandTest extends CommandTest {
	public MInitializeCommandTest() {
		super(new MInitialiseCommand(), CommandType.MINITIALISE, "MINITIALIZE()",true);
	}
	
	@Test
	public void testEquals() {
		super.testEquals(new MInitialiseCommand(),Integer.valueOf(0));
	}
}
