/**
 * 
 */
package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import grouppractical.client.commands.ClientType;
import grouppractical.client.commands.CommandType;
import grouppractical.client.commands.ConnectCommand;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ConnectCommandTest extends CommandTest {
	private final ConnectCommand cmd;
	private final ClientType clientType;
	
	public ConnectCommandTest(ClientType clientType, boolean isValid) {
		super(new ConnectCommand(clientType), CommandType.CONNECT, String.format("CONNECT(%s)",clientType.toString()),isValid);
		
		this.cmd = (ConnectCommand)super.cmd;
		this.clientType = clientType;
	}
	
	/**
	 * Generates test data (both valid and invalid)
	 * @return Test Data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		ClientType[] clientTypes = ClientType.values();
		
		//Constructs list of (Command,Bool), which is the position and whether the position is valid
		Object[][] data = new Object[clientTypes.length][2];
		
		//Creates all positions
		int i = 0;
		for (ClientType clientType : clientTypes) {
			data[i][0] = clientType;
			data[i++][1] = true;
		}
		return Arrays.asList(data);
	}
	
	@Test
	public void testGetClientType() {
		assertEquals(clientType,cmd.getClientType()); 
	}
	
	@Test
	public void testEquals() {
		ClientType diffType = clientType == ClientType.KINECT ? ClientType.MAPPER : ClientType.KINECT;
		super.testEquals(new ConnectCommand(clientType),new ConnectCommand(diffType));
	}
}