/**
 * 
 */
package grouppractical.tests.client.commands;

import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.RUnlockCommand;

import org.junit.Test;

/**
 * @author jay
 *
 */
public class RUnlockCommandTest extends CommandTest {
	public RUnlockCommandTest() {
		super(new RUnlockCommand(), CommandType.RUNLOCK, "RUNLOCK()",true);
	}
	
	@Test
	public void testEquals() {
		super.testEquals(new RUnlockCommand(),Integer.valueOf(0));
	}
}
