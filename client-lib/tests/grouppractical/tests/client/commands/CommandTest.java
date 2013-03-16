/**
 * 
 */
package grouppractical.tests.client.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import grouppractical.client.commands.Command;
import grouppractical.client.commands.CommandType;

import org.junit.Test;

/**
 * Abstract class to act as a base for test cases for all objects implementing the Command interface
 * @author jay
 *
 */
public abstract class CommandTest {
	protected final Command cmd;
	
	protected CommandType e_type;
	protected String e_string;
	
	protected boolean e_valid;
	
	/**
	 * Constructs a new CommandTest object
	 * @param cmd Object (that implements the Command interface) to test
	 * @param e_type Expected CommandType of cmd
	 * @param e_string Expected result of cmd.toString()
	 * @param e_valid Is cmd valid? If not, testToString() is ignored
	 */
	public CommandTest(Command cmd, CommandType e_type, String e_string, boolean e_valid) {
		this.cmd = cmd;
		this.e_type = e_type; this.e_string = e_string;
		this.e_valid = e_valid;
	}
	
	/**
	 * Test that cmd.getCommandType() is equal to the expected type
	 */
	@Test
	public void testGetCommandType() {
		//This should always be correct, even if the command as a whole is invalid
		assertEquals(e_type, cmd.getCommandType());
	}
	
	/**
	 * <p>Test that cmd.serialize() returns an array of the correct length (according to the expected Command Type) and
	 * that the first element is equal to the char representation of the expected Command Type</p>
	 * <p>These two conditions must always hold, even if cmd is invalid.</p> 
	 */
	@Test
	public void testSerialize() {
		char[] actual = cmd.serialize();
		
		assertEquals("Array Length is not correct",e_type.getSize(),actual.length);
		
		assertEquals("Array[0] - Type is not correct", e_type.toChar(),actual[0]);
	}
	
	/**
	 * <p>Test that cmd.toString() is equal to expected value (as declared in constructor).</p>
	 * <p>If cmd is invalid, the equality is not required.</p>
	 */
	@Test
	public void testToString() {
		if (e_valid)
			assertEquals(e_string, cmd.toString());
	}

	/**
	 * <p>Tests cmd.equals(Object o) method.</p>
	 * <p><i>It is advisable to use the testEquals(Command same, Object... diff) method in your implementation</i></p>
	 */
	@Test
	public abstract void testEquals();
	
	/**
	 * Checks that cmd.equals(o) is true for o is { cmd, same } and is false for o is { null, 0 } u diff
	 * @param same An object which is equal to cmd
	 * @param diff An array/params of objects which are not equal to cmd
	 */
	public void testEquals(Command same, Object... diff) {
		assertFalse("Not Equal to null",cmd.equals(null));
		assertFalse("Not Equal to wrong type",cmd.equals(Integer.valueOf(0)));
		assertTrue("Equal to same",cmd.equals(cmd));
		assertTrue("Equal to similar",cmd.equals(same));
		for (int i = 0; i < diff.length; i++)
			assertFalse("Not Equal to different-" + i,cmd.equals(diff));
	}
}
