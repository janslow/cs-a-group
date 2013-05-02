package grouppractical.client.simple;

import grouppractical.client.ClientConnectionThread;
import grouppractical.client.commands.ClientType;
import grouppractical.client.commands.Command;
import grouppractical.client.commands.RDistanceCommand;
import grouppractical.client.commands.RLockCommand;
import grouppractical.client.commands.RRotateCommand;
import grouppractical.client.commands.RStopCommand;
import grouppractical.client.commands.RUnlockCommand;
import grouppractical.server.CommandListener;
import grouppractical.utils.Console;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Simple Client Application, which allows sending of commands to the server
 * 
 * @author jay
 *
 */
public class SimpleApplication implements KeyListener, CommandListener {
	public static void main(String[] args) {
		String host = args.length > 0 ? args[0] : "127.0.0.1";
		new SimpleApplication(host,"Simple Client");
	}
	
	protected final Console console;
	protected final ClientConnectionThread conn;
	
	public SimpleApplication(String host, String name) {
		this(host,name,ClientType.REMOTE);
	}
	/**
	 * Constructs new client application
	 * @param host Host name of server
	 * @param name Friendly name of this client
	 * @param clientType Type of client
	 */
	protected SimpleApplication(String host, String name, ClientType clientType) {
		//Constructs new log window
		console = new Console(name + " Console");
		console.pack();
		console.setVisible(true);
		console.addKeyListener(this);
		console.println(name + " Console Created");
		
		//Attempts to connect to server
		ClientConnectionThread conn = null;
		try {
			conn = new ClientConnectionThread(host,clientType);
		} catch (UnknownHostException e) {
			System.err.println(e.toString());
			console.println("Error - Unknown Hostname");
		} catch (IOException e) {
			System.err.println(e.toString());
			console.println("Error - I/O Exception whilst opening connection to server");
		}
		this.conn = conn;
		
		conn.start();
	}
	
	@Override
	public void enqueueCommand(Command cmd) {
		if (conn != null) {
			conn.enqueueCommand(cmd);
			console.println("Command Sent: " + cmd.toString());
		}
	}

	/**
	 * <p>Processes user input via keyboard.</p>
	 * <dl>
	 * 	<dt>Up and down</dt>
	 * 	<dd>Moves robot forward or backward 5cm, respectively</dd>
	 * 	<dt>Left and right</dt>
	 * 	<dd>Turns robot left or right 30¡, respectively</dd>
	 * 	<dt>Escape</dt>
	 * 	<dd>Stops the robot</dd>
	 * </dl>  
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			enqueueCommand(new RDistanceCommand((short) 5));
			break;
		case KeyEvent.VK_DOWN:
			enqueueCommand(new RDistanceCommand((short) -5));
			break;
		case KeyEvent.VK_LEFT:
			enqueueCommand(RRotateCommand.constructFromDegrees(-30.0));
			break;
		case KeyEvent.VK_RIGHT:
			enqueueCommand(RRotateCommand.constructFromDegrees(30.0));
			break;
		case KeyEvent.VK_ESCAPE:
			enqueueCommand(new RStopCommand());
			break;
		case KeyEvent.VK_L:
			enqueueCommand(new RLockCommand());
			break;
		case KeyEvent.VK_U:
			enqueueCommand(new RUnlockCommand());
			break;
		}
	}
	
	/**
	 * Not Implemented
	 */
	@Override
	public void keyReleased(KeyEvent arg0) { }

	/**
	 * Not Implemented
	 */
	@Override
	public void keyTyped(KeyEvent arg0) { }
}
