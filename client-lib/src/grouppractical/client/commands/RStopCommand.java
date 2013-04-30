package grouppractical.client.commands;

/**
 * <p>Immutable class representing the command to stop the robot.</p>
 * <p>This command is different to setting the speed to zero, as the robot will use electrical braking when it
 * receives this command, increasing the rate of loss of momentum.</p>
 * @author jay
 *
 */
public class RStopCommand implements Command {
	@Override
	public CommandType getCommandType() {
		return CommandType.RSTOP; }
	
	@Override
	public char[] serialize() {
		char[] bytes = new char[1];
		//Command Type
		bytes[0] = getCommandType().toChar();
		return bytes;
	}
	
	@Override
	public String toString() {
		return "RSTOP()"; }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		return that != null && that.getClass().equals(RStopCommand.class);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
