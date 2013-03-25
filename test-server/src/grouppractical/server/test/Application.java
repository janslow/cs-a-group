package grouppractical.server.test;

import grouppractical.server.MultiServerThread;
import grouppractical.server.ServerCommandRequest;
import grouppractical.utils.Console;

import java.io.IOException;

public class Application {
	public static void main(String[] args) {
		new Application();
	}
	
	protected final Console con;
	protected final MultiServerThread server;
	
	/**
	 * Constructs the server 
	 */
	public Application() {
		//Constructs the output window
		con = new Console("Test Server");
		con.println("Output Window Running");
		
		//Attempts to start the server
		ThreadGroup tgroup = new ThreadGroup("Server");
		MultiServerThread server = null;
		try {
			server = new MultiServerThread(tgroup);
		} catch (IOException e) {
			System.err.println(e.toString());
			con.println("Error - I/O Exception whilst opening server port");
		}
		this.server = server;
		
		//Runs the server task
		run();
	}
	
	/**
	 * Whilst the server is still listening, calls the 'sendCommand' method with any received requests
	 */
	private void run() {
		if (server != null) { 
			server.start();
			con.println("Server Port Running on " + server.getHost() + ":" + MultiServerThread.PORT);
			
			while (server.isListening())
				try {
					sendCommand(server.dequeueBlock());
				} catch (InterruptedException e) {
					System.err.println(e.toString());
					con.println("Server was interrupted");
					server.close();
				}
		}
		con.println("Server Closed");
	}
	
	/**
	 * Prints request to output
	 * @param cmdreq Request to process
	 */
	public void sendCommand(ServerCommandRequest cmdreq) {
		if (cmdreq != null)
			con.println(cmdreq.toString());
	}
}
