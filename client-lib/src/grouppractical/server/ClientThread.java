package grouppractical.server;

import grouppractical.client.commands.ClientType;
import grouppractical.client.commands.Command;
import grouppractical.client.commands.CommandParser;
import grouppractical.client.commands.ConnectCommand;
import grouppractical.client.commands.MInitialiseCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Thread which is bound to a connection to a client, which parses input from the client and sends it to
 * the main server thread
 * @author janslow
 *
 */
class ClientThread extends Thread implements CommandListener {
	/** Parent server */
	private final MultiServerThread server;
	/** Socket connection to client */
	private final Socket socket;
	/** Client ID */
	private final int id;
	private final Semaphore writeSem;
	
	private OutputStream out;
	private InputStream in;
	/** Is the thread running or has it been closed */
	private boolean running;
	private ClientType clientType;
	
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
		this.clientType = ClientType.REMOTE;
		this.writeSem = new Semaphore(1);
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
		if (in != null)
			try {
				in.close();
			} catch (IOException e1) { }
		if (out != null)
			try {
				out.close();
			} catch (IOException e1) { }
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
		running = true;
		try {
			out = socket.getOutputStream();
			in = socket.getInputStream();
		} catch (IOException e) {
			close();
		}
		
		CommandParser cmdparse = new CommandParser();
		while (running) {
			boolean word = false;
			while (!word) {
				try {
					while(running && in.available() <= 0);
					if (!running) return;
					byte b = (byte) in.read();
					cmdparse.enqueue(b);
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
					switch (cmd.getCommandType()) {
					//Connect should update the client type
					case CONNECT:
						clientType = ((ConnectCommand)cmd).getClientType();
						break;
					//MInitialize should cause the ClientThread to send the entire map
					//For now, sends MInitialiseCommand back, which is interpreted as map all black
					case MINITIALISE:
						sendCommand(new MInitialiseCommand());
						break;
					//The following commands should be passed to the main thread
					case MPOSITION:
					case RDISTANCE:
					case RLOCK:
					case RROTATE:
					case RSTOP:
					case RUNLOCK:
						server.enqueueCommand(cmd);
						break;
					//RStatus should be ignored
					case RSTATUS:
						break;
					}
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

	@Override
	public void enqueueCommand(Command cmd) {
		switch (clientType) {
		//Kinect client listens to RStatus and RLock commands
		case KINECT:
			switch (cmd.getCommandType()) {
			case RSTATUS:
			case RLOCK:
				sendCommand(cmd);
				break;
			}
			break;
		//Mapper clients listen to MPosition, RStatus, RLock, RUnlock
		case MAPPER:
			switch (cmd.getCommandType()) {
			case MPOSITION:
			case RSTATUS:
			case RLOCK:
			case RUNLOCK:
				sendCommand(cmd);
				break;
			}
			break;
		}
	}
	
	/**
	 * Sends a command to the client
	 * @param cmd Command to transmit
	 */
	private void sendCommand(Command cmd) {
		if (cmd != null) {
			writeSem.acquireUninterruptibly();
			char[] cs = cmd.serialize();
			for (char c : cs) {
				byte b = (byte) (c - 127);
				try {
					out.write(b);
				} catch (IOException e) { }
			}
			writeSem.release();
		}
	}
}
