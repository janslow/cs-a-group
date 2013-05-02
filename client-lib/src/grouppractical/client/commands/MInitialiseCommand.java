package grouppractical.client.commands;


/**
 * <p>Immutable class representing the initial state of the map.</p>
 * <p>Position can be in the range ±32767.</p>
 * @author jay
 *
 */
public class MInitialiseCommand implements Command {
	@Override
	public CommandType getCommandType() {
		return CommandType.MINITIALISE;
	}

	@Override
	public char[] serialize() {
		char[] bytes = new char[1];
		//Command Type
		bytes[0] = getCommandType().toChar();
		
		return bytes;
	}
	
	@Override
	public String toString() {
		return String.format("MINITIALIZE()"); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		return that != null && that.getClass().equals(MInitialiseCommand.class);
	}
	
	@Override
	public int hashCode() {
		return getCommandType().hashCode();
	}
}
