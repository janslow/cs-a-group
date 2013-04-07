package grouppractical.client.commands;

public class MListenerCommand implements Command {
	private final boolean updates;
	
	/**
	 * Constructs a new MapListenerCommand
	 * @param updates Whether to register for map updates
	 */
	public MListenerCommand(boolean updates) {
		this.updates = updates;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.MLISTENER;
	}

	@Override
	public char[] serialize() {
		char[] bytes = new char[2];
		//Command Type
		bytes[0] = getCommandType().toChar();
		//updates flag
		bytes[1] = (char) (updates ? 0x01 : 0x00);
		
		return bytes;
	}
	
	/**
	 * Gets whether this command will register the client for further updates
	 * @return True if server will register client for further updates, otherwise false
	 */
	public boolean getRegisterUpdates() { return updates; }
	
	@Override
	public String toString() {
		return String.format("MLISTENER(" + (updates ? "" : "no ") + "updates)"); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(MListenerCommand.class)) return false;
		MListenerCommand mthat = (MListenerCommand)that;
		return mthat.getRegisterUpdates() == this.getRegisterUpdates();
	}
	
	@Override
	public int hashCode() {
		return getCommandType().hashCode();
	}
}
