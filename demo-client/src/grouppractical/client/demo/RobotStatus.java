package grouppractical.client.demo;

import grouppractical.client.RobotListener;
import grouppractical.utils.map.Position;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class to construct and display a window which updates with status information from the robot,
 * incorporating a RobotListener to listen to updates from a ClientConnection.
 * @author Joe Zammit
 *
 */
public class RobotStatus extends JFrame implements RobotListener {


	/**
	 * UID of class
	 */
	private static final long serialVersionUID = 8451014341062053110L;
	private final JPanel pane;
	private final JLabel batteryLevel;
	private final JLabel robotXposition;
	private final JLabel robotYposition;
	private final JLabel robotAngle;

	/**
	 * Constructs a new RobotStatus JFrame given a ClientConnection
	 * @param baseFontSize the largest size of font (pt) to be used in the window
	 * @param conn the ClientConnection from which to receive updates from the Robot
	 */
	public RobotStatus(int baseFontSize) {
		// construct JFrame
		super("Robot Status");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// construct window contents
		batteryLevel = new JLabel("");
		robotXposition = new JLabel("");
		robotYposition = new JLabel("");
		robotAngle = new JLabel("");
		JLabel headerLabel = new JLabel("Robot Status:");
		JLabel xLabel = new JLabel("x co-ordinate:");
		JLabel yLabel = new JLabel("y co-ordinate:");
		JLabel aLabel = new JLabel("robot angle:");
		JLabel batteryLabel = new JLabel("Battery level:");
		
		
		// set fonts
		Font fb = new Font(Font.MONOSPACED,Font.BOLD, baseFontSize);
		Font f = new Font(Font.MONOSPACED, Font.ITALIC, (int) (baseFontSize*0.8));
		headerLabel.setFont(fb);
		xLabel.setFont(fb);
		yLabel.setFont(fb);
		aLabel.setFont(fb);
		batteryLabel.setFont(fb);
		batteryLevel.setFont(f);
		robotXposition.setFont(f);
		robotYposition.setFont(f);
		robotAngle.setFont(f);
		
		
		// construct JPanel
		pane = new JPanel(new GridLayout(5,2,10,10));
		pane.add(headerLabel);
		pane.add(new JLabel());
		pane.add(xLabel);
		pane.add(robotXposition);
		pane.add(yLabel);
		pane.add(robotYposition);
		pane.add(aLabel);
		pane.add(robotAngle);
		pane.add(batteryLabel);
		pane.add(batteryLevel);
		
		// construct, pack, and display JFrame
		this.add(pane);
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void updateRobotPosition(Position position) {
		robotXposition.setText(Integer.toString(position.getX()));
		robotYposition.setText(Integer.toString(position.getY()));
	}

	@Override
	public void updateAngle(double degrees, double radians) {
		robotAngle.setText(Integer.toString((int) degrees) + "�");
	}

	@Override
	public void updateVoltage(float voltage) {
		batteryLevel.setText(String.format("%.2f", voltage));
	}

	@Override
	public void updateLocked(boolean locked) { }

}
