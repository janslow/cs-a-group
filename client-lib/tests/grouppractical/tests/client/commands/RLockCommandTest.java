/**
 * 
 */
package grouppractical.tests.client.commands;

import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.RLockCommand;

import org.junit.Test;

/**
 * @author jay
 *
 */
public class RLockCommandTest extends CommandTest {
	public RLockCommandTest() {
		super(new RLockCommand(), CommandType.RLOCK, "RLOCK()",true);
	}
	
	@Test
	public void testEquals() {
		super.testEquals(new RLockCommand(),Integer.valueOf(0));
	}
}
