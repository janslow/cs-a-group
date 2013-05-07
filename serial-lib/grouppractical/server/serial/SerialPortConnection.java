package grouppractical.server.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import grouppractical.client.commands.Command;
import grouppractical.client.commands.RDistanceCommand;
import grouppractical.client.commands.RRotateCommand;
import grouppractical.robot.RobotController;
import grouppractical.server.CommandListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class SerialPortConnection implements CommandListener, SerialPortEventListener {
	private static final byte FORWARD = 0x00, BACKWARD = 0x01, CLOCKWISE = 0x02, ANTICLOCKWISE = 0x03, STOP = 0x05;
	
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	private final SerialPort serialPort;
	private final InputStream input;
	private final OutputStream output;
	private final RobotController controller;
	
	public SerialPortConnection(CommPortIdentifier port, RobotController controller) throws PortInUseException, IOException {
		serialPort = (SerialPort) port.open(this.getClass().getName(), TIME_OUT);
		
		try {
			serialPort.setSerialPortParams(DATA_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			// This should not happen (uses enums)
			e.printStackTrace();
		}
		
		input = serialPort.getInputStream();
		output = serialPort.getOutputStream();
		
		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			// This should not happen (only one listener)
			e.printStackTrace();
		}
		serialPort.notifyOnDataAvailable(true);
		
		this.controller = controller;
		
		attachShutDownHook();
	}
	@Override
	public void enqueueCommand(Command cmd) {
		switch (cmd.getCommandType()) {
		case RDISTANCE:
			if (((RDistanceCommand)cmd).getDistance() > 0)
				write(FORWARD);
			else if (((RDistanceCommand)cmd).getDistance() < 0)
				write(BACKWARD);
			break;
		case RROTATE:
			if (((RRotateCommand)cmd).getAngleDegrees() > 0)
				write(CLOCKWISE);
			else if (((RRotateCommand)cmd).getAngleDegrees() < 0)
				write(ANTICLOCKWISE);
			break;
		case RSTOP:
			write(STOP);
		}
	}
	
	private void write(byte b) {
		try {
			output.write(b);
			System.out.println("Written: " + Integer.toHexString(b));
		} catch (IOException e) { }
	}
	
	public void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent e) {
		switch (e.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE:
			try {
				if (input.available() > 0) {
					float voltage = input.read();
					voltage = voltage / 16.84f;
					controller.setVoltage(voltage);
				}
			} catch (IOException e1) { }
		}
	}
	
	public void attachShutDownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				close();
				System.out.println("Shutdown Serial Connection");
			}
		});
		System.out.println("Serial Connection Shutdown Hook Attached.");
	}
}
