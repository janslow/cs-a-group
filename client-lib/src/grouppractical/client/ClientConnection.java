package grouppractical.client;

import grouppractical.client.commands.Command;
import grouppractical.server.MultiServerThread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.event.EventListenerList;

/**
 * Class to open a connection to the server to send commands to
 * @author janslow
 *
 */
public class ClientConnection {
	private final Socket socket;
	private final PrintWriter out;
	private final EventListenerList listeners;
	private final ServerListenerThread serverListener;
	
	/**
	 * Constructs a new ClientConnection
	 * @param host Host name of server to connect to
	 * @param name Friendly name of this client
	 * @throws UnknownHostException Thrown if the specified server host name can't be found
	 * @throws IOException Thrown if there was an error connecting to server port at the specified host
	 */
	public ClientConnection(String host, String name) throws UnknownHostException, IOException {
		socket = new Socket(host, MultiServerThread.PORT);
		out = new PrintWriter(socket.getOutputStream(), true);
		listeners = new EventListenerList();
		serverListener = new ServerListenerThread(listeners, socket, this);
		serverListener.start();
	}
	
	// throws??
	/**
	 * Closes the exception to the server
	 * @throws IOException Thrown if there was an error closing the socket to the server
	 */
	public void close() {
		out.close();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a serialized command to the server
	 * @param cmd Command to serialize and send
	 */
	public void sendCommand(Command cmd) {
		if (cmd != null)
			out.println(cmd.serialize());
	}
	
	/**
	 * Adds a RobotListener, which will be called when the robot is updated
	 * @param l RobotListener to add
	 */
	public void addRobotListener(RobotListener l) {
		listeners.add(RobotListener.class, l);
	}
	/**
	 * Removes a RobotListener
	 * @param l RobotListener to remove
	 */
	public void removeRobotListener(RobotListener l) {
		listeners.remove(RobotListener.class, l);
	}
	/**
	 * Adds a MapListener, which will be called when the map is updated
	 * @param l MapListener to add
	 */
	public void addMapListener(MapListener l) {
		listeners.add(MapListener.class, l);
	}
	/**
	 * Removes a MapListener
	 * @param l MapListener to remove
	 */
	public void removeMapListener(MapListener l) {
		listeners.remove(MapListener.class, l);
	}
}
