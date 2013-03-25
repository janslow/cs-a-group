package grouppractical.server;

import grouppractical.client.commands.Command;
import grouppractical.client.commands.CommandParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Thread which is bound to a connection to a client, which parses input from the client and sends it to
 * the main server thread
 * @author janslow
 *
 */
class ClientThread extends Thread {
	/** Parent server */
	private final MultiServerThread server;
	/** Socket connection to client */
	private final Socket socket;
	/** Client ID */
	private final int id;
	
	private PrintWriter pw;
	private BufferedReader br;
	/** Is the thread running or has it been closed */
	private boolean running;
	
	/**
	 * Constructs a new thread to communicate with client
	 * @param server Server object to send finished responses to
	 * @param group
	 * @param socket
	 * @param id
	 */
	public ClientThread(MultiServerThread server, ThreadGroup group, Socket socket, int id) {
		super(group, String.format("ClientThread(%d)",id));
		this.server = server;
		this.socket = socket;
		this.id = id;
	}
	
	/**
	 * Closes the thread and the connection to the client
	 */
	public void close() {
		if (!running) return;

		running = false;
		
		//Interrupts the thread (to break from the run loop)
		this.interrupt();
		//Closes the input and output streams
		if (pw != null) pw.close();
		if (br != null)
			try {
				br.close();
			} catch (IOException e) { }
		try {
			socket.close();
		} catch (IOException e) { }
		
		//Tells the main thread that this client is closed
		server.closeClient(id);
	}
	
	/**
	 * Accepts input from the client and attempts to parse it into commands, sending the parsed commands
	 * to the main server thread
	 */
	public void run() {
		try {
			pw = new PrintWriter(socket.getOutputStream(), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			close();
		}
		
		boolean loop = true;
		CommandParser cmdparse = new CommandParser();
		while (loop) {
			boolean word = false;
			while (!word) {
				try {
					cmdparse.enqueue((char)br.read());
				} catch (IOException e) {
					System.err.println(e.toString());
					close();
					return;
				} catch (InterruptedException e) {
					close();
					return;
				}
				
				while (!cmdparse.isOutputEmpty()) {
					Command cmd = cmdparse.dequeue();
					ServerCommandRequest cmdreq = new ServerCommandRequest(id, cmd);
					server.enqueueRequest(cmdreq);
				}
			}
		}
	}
	
	/**
	 * Gets whether the thread is running
	 * @return True if running, otherwise false
	 */
	public boolean isRunning() {
		return running;
	}
}
