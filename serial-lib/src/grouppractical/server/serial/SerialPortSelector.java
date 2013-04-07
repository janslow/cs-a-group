package grouppractical.server.serial;

import static grouppractical.server.serial.Serial.getPorts;
import grouppractical.utils.ArrayOps;

import javax.swing.JFrame;

public class SerialPortSelector extends JFrame {
	/**
	 * UID of class
	 */
	private static final long serialVersionUID = -2630584138692159547L;
	
	private String[] ports;
	private String selected;
	
	public SerialPortSelector() {
		this(false);
	}
	public SerialPortSelector(boolean smart) {
		super("Serial Port Selector");
		
		// TODO: Create List Component
		
		// TODO: Create button ('Refresh') which calls fill(getPorts())
		
		// TODO: Create button ('Smart Select') which calls smartFind();
		
		// TODO: Create button ('Select') which calls select();
		
		if (smart)
			smartFind();
		else
			refresh();
	}
	
	private void refresh() {
		fill(getPorts());
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
		
		String[] initial = getPorts();
		// TODO: Display Message saying to plug in device or cancel
		if (false /* if cancel */) {
			refresh();
			return;
		}
		
		String[] diff = (String[]) ArrayOps.subtract(getPorts(), initial);
		
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
