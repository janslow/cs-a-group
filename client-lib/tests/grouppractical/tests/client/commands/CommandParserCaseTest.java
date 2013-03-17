package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import grouppractical.client.commands.Command;
import grouppractical.client.commands.CommandParser;
import grouppractical.client.commands.RDistanceCommand;
import grouppractical.client.commands.RRotateCommand;
import grouppractical.client.commands.RSpeedCommand;
import grouppractical.client.commands.RStopCommand;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CommandParserCaseTest {
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				//Test RDISTANCE(dist) command
				{ "RDISTANCE(MIN)", new RDistanceCommand(RDistanceCommand.MIN) },
				{ "RDISTANCE(0)", new RDistanceCommand((short) 0) },
				{ "RDISTANCE(MAX)", new RDistanceCommand(RDistanceCommand.MAX) },
				//Test RROTATE(rotate) command
				{ "RROTATE(MIN)", new RRotateCommand(RRotateCommand.MIN_INT) },
				{ "RROTATE(0)", new RRotateCommand((short) 0) },
				{ "RROTATE(MAX)", new RRotateCommand(RRotateCommand.MAX_INT) },
				//Test RSPEED(left,right) command
				{ "RSPEED(MIN,MIN)", new RSpeedCommand(RSpeedCommand.MIN,RSpeedCommand.MIN) },
				{ "RSPEED(0,MIN)", new RSpeedCommand((short) 0,RSpeedCommand.MIN) },
				{ "RSPEED(MAX,MIN)", new RSpeedCommand(RSpeedCommand.MAX,RSpeedCommand.MIN) },
				{ "RSPEED(MIN,0)", new RSpeedCommand(RSpeedCommand.MIN,(short) 0) },
				{ "RSPEED(0,0)", new RSpeedCommand((short) 0,(short) 0) },
				{ "RSPEED(MAX,0)", new RSpeedCommand(RSpeedCommand.MAX,(short) 0) },
				{ "RSPEED(MIN,MAX)", new RSpeedCommand(RSpeedCommand.MIN,RSpeedCommand.MAX) },
				{ "RSPEED(0,MAX)", new RSpeedCommand((short) 0,RSpeedCommand.MAX) },
				{ "RSPEED(MAX,MAX)", new RSpeedCommand(RSpeedCommand.MAX,RSpeedCommand.MAX) },
				//Test RSTOP() command
				{ "RSTOP()", new RStopCommand() }
		};
		return Arrays.asList(data);
	}
	
	protected final Command cmd;
	protected final String testname;
	
	public CommandParserCaseTest(String testname, Command cmd) {
		this.cmd = cmd;
		this.testname = testname;
	}
	
	@Test
	public void testParse() throws InterruptedException {
		char[] serialized = cmd.serialize();
		assertNotNull(testname + " - serialize method is invalid");
		
		CommandParser cmdparse = new CommandParser();
		cmdparse.enqueue(serialized);
		
		//Check that exactly all of the characters were parsed
		assertEquals(testname + " - parse was incomplete",1,cmdparse.outputSize());
		assertEquals(testname + " - parse had leftover input",0,cmdparse.inputSize());
		
		Command pcmd = cmdparse.dequeue();
		
		assertNotNull(testname + " - parsed command is null",pcmd);
		assertEquals(testname + " - parsed command is not equal to original",cmd,pcmd);
	}
}
