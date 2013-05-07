package grouppractical.server;

import grouppractical.client.commands.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.event.EventListenerList;

/**
 * <p>Thread which opens a port to accept a finite number of clients, spawning off child threads to handle
 * each client.</p>
 * <p>Also receives parsed commands from the children, allowing other threads to read them from a queue</p> 
 * @author janslow
 *
 */
public class MultiServerThread extends Thread implements CommandListener {
	/** Port which the server opens for communication */
	public static final int PORT = 2003;
	/** Maximum number of clients allowed at any one time by the server */
	public static final int MAX_CLIENTS = 10;
	
	public final ThreadGroup group;
	private final ClientThread[] clients;
	private final String host;
	private final EventListenerList listeners;
	
	private ServerSocket serverSocket;
	private boolean listening = true;
	
	/**
	 * Constructs new Server Thread
	 * @param group ThreadGroup which both this thread and all child threads should belong to
	 * @throws IOException Thrown if the thread was unable to open a port
	 */
	public MultiServerThread(ThreadGroup group) throws IOException {
		super(group,"MultiServerThread");
		this.group = group;
		this.clients = new ClientThread[MAX_CLIENTS];
		this.serverSocket = new ServerSocket(PORT);
		this.host = serverSocket.getInetAddress().getHostAddress();
		this.listeners = new EventListenerList();
		attachShutDownHook();
	}
	
	/**
	 * Listens for new clients and spawns off child threads to handle them
	 */
	public void run() {
    	while (listening) {
    		//TODO Add smart assignment of IDs and enforce client limit
    		try {
    			Socket socket = serverSocket.accept();
    			int i = 0;
    			//Find an unused ID
    			while (i < MAX_CLIENTS && clients[i] != null) i++;
    			if (i < MAX_CLIENTS) {
					clients[i] = new ClientThread(this,group,socket,i);
					clients[i].start();
					addCommandListener(clients[i]);
    			} else
    				socket.close();
			} catch (IOException e) {
				close();
			}
    	}
    }
	
	/**
	 * Closes all clients then closes the server port
	 */
	public void close() {
		//If the server is not already being closed, close
		if (!listening) return;
		listening = false;
		//Closes each client/child thread
		for (int i = 0; i < clients.length; i++)
			closeClient(i);
		//Tries to close server port
		try {
			serverSocket.close();
		} catch (IOException e) { }
		
	}
	
	/**
	 * Closes the connection to the client with the specified ID
	 * @param id
	 */
	public void closeClient(int id) {
		if (id >= 0 && id < clients.length && clients[id] != null) {
			removeCommandListener(clients[id]);
			clients[id].close();
			clients[id] = null;
		}
	}
	/**
	 * Gets whether the server is listening for new connections
	 * @return True if the server is listening, otherwise false
	 */
	public boolean isListening() {
		return listening;
	}
	/**
	 * Gets the host name which is bound to the open port
	 * @return Host name of server
	 */
	public String getHost() {
		return host;
	}

	@Override
	public void enqueueCommand(Command cmd) {
		for (CommandListener l : listeners.getListeners(CommandListener.class))
			l.enqueueCommand(cmd);
	}
	
	/**
	 * Adds a CommandListener, which will be called when the robot's status changes
	 * @param l CommandListener to add
	 */
	public void addCommandListener(CommandListener l) {
		listeners.add(CommandListener.class, l);
	}
	/**
	 * Removes a CommandListener
	 * @param l CommandListener to remove
	 */
	public void removeCommandListener(CommandListener l) {
		listeners.remove(CommandListener.class, l);
	}
	
	public void attachShutDownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				close();
				System.out.println("Shutdown Network Server");
			}
		});
		System.out.println("Network Server Shutdown Hook Attached.");
	}
}