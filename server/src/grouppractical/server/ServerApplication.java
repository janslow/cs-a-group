package grouppractical.server;

import gnu.io.PortInUseException;
import grouppractical.client.commands.CommandConsole;
import grouppractical.robot.RobotController;
import grouppractical.server.serial.SerialPortConnection;
import grouppractical.server.serial.SerialPortSelectorDialog;

import java.io.IOException;

public class ServerApplication {
	public static void main(String[] args) {
		new ServerApplication();
	}
	
	protected final CommandConsole con;
	protected final MultiServerThread server;
	protected final RobotController rbotController;
	protected final SerialPortConnection serial;
	
	/**
	 * Constructs the server 
	 */
	public ServerApplication() {
		//Constructs the output window
		con = new CommandConsole("Test Server");
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
		this.rbotController = new RobotController();
		
		SerialPortSelectorDialog serialDialog = new SerialPortSelectorDialog(con);
		serialDialog.setVisible(true);
		SerialPortConnection serial = null;
		if (serialDialog.getSelected() != null)
			try {
				serial = new SerialPortConnection(serialDialog.getSelected(), rbotController);
			} catch (PortInUseException e) {
				con.println("Serial Port is already in use");
			} catch (IOException e) {
				con.println("Unable to connect to Serial Port");
				e.printStackTrace();
			}
		this.serial = serial;
		
		//Runs the server task
		run();
	}
	
	/**
	 * Whilst the server is still listening, calls the 'sendCommand' method with any received requests
	 */
	private void run() {
		if (server != null) {
			server.addCommandListener(con);
			server.addCommandListener(rbotController);
			server.addCommandListener(serial);
			rbotController.addCommandListener(server);
			
			server.start();
			con.println("Server Port Running on " + server.getHost() + ":" + MultiServerThread.PORT);
		}
	}
}