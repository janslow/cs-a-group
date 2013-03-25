package grouppractical.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>Thread which opens a port to accept a finite number of clients, spawning off child threads to handle
 * each client.</p>
 * <p>Also receives parsed commands from the children, allowing other threads to read them from a queue</p> 
 * @author janslow
 *
 */
public class MultiServerThread extends Thread {
	/** Port which the server opens for communication */
	public static final int PORT = 2003;
	/** Maximum number of clients allowed at any one time by the server */
	public static final int MAX_CLIENTS = 10;
	
	public final ThreadGroup group;
	private final ClientThread[] clients;
	private final ConcurrentLinkedQueue<ServerCommandRequest> queue;
	private final String host;
	
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
		this.queue = new ConcurrentLinkedQueue<ServerCommandRequest>();
		this.serverSocket = new ServerSocket(PORT);
		this.host = serverSocket.getInetAddress().getHostAddress();
	}
	
	/**
	 * Listens for new clients and spawns off child threads to handle them
	 */
	public void run() {
    	int i = 0;
    	while (listening) {
    		//TODO Add smart assignment of IDs and enforce client limit
    		try {
				new ClientThread(this,group,serverSocket.accept(),i++).start();
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
		if (id >= 0 && id < clients.length && clients[id] != null)
			clients[id].close();
	}
	
	/**
	 * Adds a received request (from a child thread) to the queue, and notifies any waiting threads
	 * @param cmdreq Request to enqueue
	 */
	synchronized void enqueueRequest(ServerCommandRequest cmdreq) {
		this.notify();
		queue.add(cmdreq);
	}
	/**
	 * Gets whether there are any requests waiting to be dequeued
	 * @return True if there are no requests, otherwise false
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	/**
	 * Gets the next request in the queue
	 * @return Next request, or null if there are no queued requests
	 */
	public synchronized ServerCommandRequest dequeue() {
		return queue.poll();
	}
	/**
	 * Gets the next request in the queue, blocking if necessary
	 * @return Next request
	 */
	public synchronized ServerCommandRequest dequeueBlock() throws InterruptedException {
		while (isEmpty() && listening) this.wait();
		return dequeue();
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
}