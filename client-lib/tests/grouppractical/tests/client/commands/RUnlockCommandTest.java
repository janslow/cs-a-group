/**
 * 
 */
package grouppractical.tests.client.commands;

import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.RStopCommand;

import org.junit.Test;

/**
 * @author jay
 *
 */
public class RUnlockCommandTest extends CommandTest {
	public RUnlockCommandTest() {
		super(new RStopCommand(), CommandType.RUNLOCK, "RUNLOCK()",true);
	}
	
	@Test
	public void testEquals() {
		super.testEquals(new RStopCommand(),Integer.valueOf(0));
	}
}
