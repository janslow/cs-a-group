package grouppractical.remote.java;

import grouppractical.client.commands.Command;

public class Application {
	public static void main(String[] args) {
		new Application();
	}
	
	public Application() {
		Console con = new Console(this);
		con.setVisible(true);
	}
	
	public void sendCommand(Command cmd) {
		System.out.println(cmd);
	}
}
