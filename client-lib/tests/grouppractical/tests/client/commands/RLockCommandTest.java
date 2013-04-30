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
public class RLockCommandTest extends CommandTest {
	public RLockCommandTest() {
		super(new RStopCommand(), CommandType.RLOCK, "RLOCK()",true);
	}
	
	@Test
	public void testEquals() {
		super.testEquals(new RStopCommand(),Integer.valueOf(0));
	}
}
