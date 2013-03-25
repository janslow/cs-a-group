package grouppractical.server;

import grouppractical.client.commands.Command;

/**
 * Wrapper class for Commands from clients which contains client information as well
 * @author janslow
 *
 */
public class ServerCommandRequest {
	private final Command cmd;
	private final int client;
	
	/**
	 * Constructs a new ServerCommandRequest object
	 * @param client ID of client who sent the request
	 * @param cmd Command sent by client
	 */
	public ServerCommandRequest(int client, Command cmd) {
		this.client = client;
		this.cmd = cmd;
	}
	
	/**
	 * Gets the command sent by client
	 * @return Command to execute
	 */
	public Command getCommand() { return cmd; }
	/**
	 * Gets the ID of the client that sent the request
	 * @return Client ID
	 */
	public int getClient() { return client; }
	
	@Override
	public String toString() {
		return String.format("Client %d : %s",client, cmd.toString());
	}
}
