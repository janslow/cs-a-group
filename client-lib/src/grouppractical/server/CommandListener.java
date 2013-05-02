package grouppractical.server;

import java.util.EventListener;

import grouppractical.client.commands.Command;

/**
 * An interface for classes which need to receive commands
 * @author janslow
 *
 */
public interface CommandListener extends EventListener {
	/**
	 * Receive a command
	 * @param cmd New Command
	 */
	public void enqueueCommand(Command cmd);
}
