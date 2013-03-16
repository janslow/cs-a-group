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
public class RStopCommandTest extends CommandTest {
	public RStopCommandTest() {
		super(new RStopCommand(), CommandType.RSTOP, "RSTOP()",true);
	}
	
	@Test
	public void testEquals() {
		super.testEquals(new RStopCommand(),Integer.valueOf(0));
	}
}
