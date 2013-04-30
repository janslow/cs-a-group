package grouppractical.client.commands;

/**
 * <p>Immutable class used to register a client to the server.</p>
 * @author jay
 *
 */
public class ConnectCommand implements Command {
	private final ClientType clientType;
	
	public ConnectCommand(ClientType clientType) {
		this.clientType = clientType;
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.CONNECT; }
	
	/**
	 * Gets the type of the connected client
	 * @return Client Type
	 */
	public ClientType getClientType() {
		return clientType; }
	
	@Override
	public char[] serialize() {
		char[] bytes = new char[2];
		//Command Type
		bytes[0] = getCommandType().toChar();
		//Client Type
		bytes[1] = clientType.toChar();
		return bytes;
	}
	
	@Override
	public String toString() {
		return String.format("CONNECT(%s)",clientType); }
	
	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if (that == null || !that.getClass().equals(ConnectCommand.class)) return false;
		ConnectCommand cthat = (ConnectCommand)that;
		return cthat.getClientType().equals(clientType);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
