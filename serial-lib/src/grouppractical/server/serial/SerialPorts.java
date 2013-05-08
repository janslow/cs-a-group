package grouppractical.server.serial;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

public class SerialPorts {
	protected static PortNode getNode() {
		final PortNode head = new PortNode(null);
		PortNode curr = head;
		
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			curr = curr.next = new PortNode(curr,currPortId);
		}
		
		return curr;
	}
	
	public static CommPortIdentifier[] getPorts() {
		PortNode end = getNode();
		
		CommPortIdentifier[] ports = new CommPortIdentifier[end.count];
		
		PortNode curr = end.head;
		int i = 0;
		while (curr.next != null) {
			curr = curr.next;
			ports[i++] = curr.port;
		}
		
		return ports;
	}
	
	public static String[] getPortNames() {
		PortNode end = getNode();
		
		String[] ports = new String[end.count];
		
		PortNode curr = end.head;
		int i = 0;
		while (curr.next != null) {
			curr = curr.next;
			ports[i++] = curr.port.getName();
		}
		
		return ports;
	}
	
	public static CommPortIdentifier getPort(String name) {
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(name))
				return currPortId;
		}
		
		return null;
	}
	
	static class PortNode {
		public final CommPortIdentifier port;
		public final int count;
		public final PortNode head;
		
		public PortNode next;
		
		public PortNode(PortNode prev, CommPortIdentifier portName) {
			this.port = portName;
			this.count = prev.count + 1;
			prev.next = this;
			this.head = prev.head;
		}
		public PortNode(CommPortIdentifier portName) {
			this.port = portName;
			this.count = 0;
			this.head = this;
		}
	}
}
