package grouppractical.remote.java;

import grouppractical.client.commands.RDistanceCommand;
import grouppractical.client.commands.RRotateCommand;
import grouppractical.client.commands.RStopCommand;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Console extends JFrame implements KeyListener {
	private final Application app;
	/**
	 * Constructs a new Console class
	 * @param app Parent Application
	 */
	public Console(Application app) {
		this.app = app;
		this.addKeyListener(this);
	}

	/**
	 * <p>Reads key pressed events and takes the following actions, depending on the key.</p>
	 * <dl>
	 * 	<dt>Up or Down</dt>
	 * 	<dd>Sends a RDistanceCommand (moving the robot forward or backward 5cm, respectively)</dd>
	 * 	<dt>Left or Right</dt>
	 * 	<dd>Sends a RRotateCommand (turning the robot 5¡ counter-clockwise or clockwise, respectively)</dd>
	 * 	<dt>Escape</dt>
	 * 	<dd>Sends a RStopCommand (stopping the robot)</dd>
	 * </dl>
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			app.sendCommand(new RDistanceCommand((short) 5));
			break;
		case KeyEvent.VK_DOWN:
			app.sendCommand(new RDistanceCommand((short) -5));
			break;
		case KeyEvent.VK_LEFT:
			app.sendCommand(new RRotateCommand(-30));
			break;
		case KeyEvent.VK_RIGHT:
			app.sendCommand(new RRotateCommand(30));
			break;
		case KeyEvent.VK_ESCAPE:
			app.sendCommand(new RStopCommand());
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }
	
	
}
