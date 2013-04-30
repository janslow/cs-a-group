/**
 * 
 */
package grouppractical.tests.client.commands;

import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.MInitializeCommand;

import org.junit.Test;

/**
 * @author jay
 *
 */
public class MInitializeCommandTest extends CommandTest {
	public MInitializeCommandTest() {
		super(new MInitializeCommand(), CommandType.MINITIALIZE, "MINITIALIZE()",true);
	}
	
	@Test
	public void testEquals() {
		super.testEquals(new MInitializeCommand(),Integer.valueOf(0));
	}
}
