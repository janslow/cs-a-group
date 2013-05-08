package grouppractical.server.serial;

import static grouppractical.server.serial.SerialPorts.getPorts;
import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

public class SerialPortSelectorDialog extends JDialog implements ActionListener {
	/**
	 * UID of class
	 */
	private static final long serialVersionUID = -2630584138692159547L;
	
	private final JList list;
	private final DefaultListModel listModel;
	private final JButton btnRefresh, btnSelect;
	
	private CommPortIdentifier selected;
	
	public SerialPortSelectorDialog(Frame owner) {
		super(owner, "Serial Port Selector", true);
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		add(list, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setActionCommand("refresh");
		btnRefresh.addActionListener(this);
		panel.add(btnRefresh);
		
		btnSelect = new JButton("Select");
		btnSelect.setActionCommand("select");
		btnSelect.addActionListener(this);
		panel.add(btnSelect);
		
		add(panel, BorderLayout.SOUTH);
		
		refresh();
	}
	
	public CommPortIdentifier getSelected() {
		return selected;
	}
	
	private void refresh() {
		fill(getPorts());
	}
	private void fill(CommPortIdentifier[] ps) {
		listModel.removeAllElements();
		for (CommPortIdentifier p : ps)
			listModel.addElement(new ListItem(p));
		pack();
	}
	
	private void select() {
		ListItem sel = (ListItem) list.getSelectedValue();
		select(sel.port);
	}
	
	private void select(CommPortIdentifier selected) {
		this.selected = selected;
		dispose();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(btnRefresh.getActionCommand()))
			refresh();
		else if (e.getActionCommand().equals(btnSelect.getActionCommand()))
			select();
	}
	
	private class ListItem {
		final CommPortIdentifier port;
		
		public ListItem(CommPortIdentifier port) {
			this.port = port;
		}
		public String toString() {
			return port.getName();
		}
	}
}
