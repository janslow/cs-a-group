package grouppractical.server.serial;

import static grouppractical.server.serial.SerialPorts.getPortNames;
import grouppractical.utils.ArrayOps;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;


public class SerialPortSelector extends JFrame implements ActionListener {
	/**
	 * UID of class
	 */
	private static final long serialVersionUID = -2630584138692159547L;
	
	private final JList list;
	
	private String[] ports;
	private String selected;
	
	public SerialPortSelector() {
		this(false);
	}
	public SerialPortSelector(boolean smart) {
		super("Serial Port Selector");
		
		ports = getPortNames();
		list = new JList(ports);
		add(list, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		JButton btnRefresh, btnSmart, btnSelect;
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setActionCommand("refresh");
		btnRefresh.addActionListener(this);
		
		btnSmart = new JButton("Smart Select");
		btnRefresh.setActionCommand("smart_select");
		btnRefresh.addActionListener(this);
		
		btnRefresh = new JButton("Select");
		btnRefresh.setActionCommand("select");
		btnRefresh.addActionListener(this);
		
		// TODO: Create button ('Refresh') which calls fill(getPorts())
		
		// TODO: Create button ('Smart Select') which calls smartFind();
		
		// TODO: Create button ('Select') which calls select();
		
		add(panel, BorderLayout.SOUTH);
		
		if (smart)
			smartFind();
		else
			refresh();
	}
	
	private void refresh() {
		fill(getPortNames());
	}
	private void fill(String[] ss) {
		// TODO: Replace content of list component with ss
	}
	
	private void smartFind() {
		// TODO: Display Message saying to unplug the device or cancel
		if (false /* if cancel */) {
			refresh();
			return;
		}
		
		String[] initial = getPortNames();
		// TODO: Display Message saying to plug in device or cancel
		if (false /* if cancel */) {
			refresh();
			return;
		}
		
		String[] diff = (String[]) ArrayOps.subtract(getPortNames(), initial);
		
		if (diff.length == 0) {
			// TODO: Display Message "No new devices detected"
			refresh();
		} else if (diff.length == 1)
			select(diff[0]);
		else {
			// TODO: Display Message "Multiple new devices detected"
			fill(diff);
		}
	}
	
	private void select() {
		// TODO: Gets the selected name from the list and calls select(name)
	}
	
	private void select(String name) {
		// TODO: returns name to parent
	}
}
