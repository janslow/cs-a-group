package grouppractical.client.commands;

import grouppractical.server.CommandListener;
import grouppractical.utils.Console;

public class CommandConsole extends Console implements CommandListener {
	private static final long serialVersionUID = 1026795042813649075L;

	public CommandConsole(String title) {
		super(title);
	}

	@Override
	public void enqueueCommand(Command cmd) {
		super.println(cmd.toString());
	}
}
