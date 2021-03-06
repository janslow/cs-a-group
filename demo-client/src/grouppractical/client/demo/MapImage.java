package grouppractical.client.demo;

import grouppractical.client.ClientConnectionThread;
import grouppractical.client.commands.MInitialiseCommand;
import grouppractical.utils.Console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A JFrame that displays a MapPanel with the graphical representation of a Map,
 * as provided via a ClientConnection.
 * @author Joe Zammit
 * 
 */
public class MapImage extends JFrame {
	
	/**
	 * UID of class
	 */
	private static final long serialVersionUID = 3251840355742439048L;
	private final MapPanel mapPanel;
	
	
	/**
	 * Constructs a new MapImage JFrame
	 * @param outputFile a File to which a PNG image of the Map may be output
	 * @param conn the ClientConnection from which the map data will be received
	 * @param console a Console to which the class may output messages
	 */
	public MapImage(final File outputFile, final ClientConnectionThread conn, final Console console) {
		// construct JFrame
		super("Map");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Create save button and configure MapPane
		JPanel pane = new JPanel(new BorderLayout());
		int pix_width = 3;
		mapPanel = new MapPanel(pix_width);
		conn.addMapListener(mapPanel);
		conn.addRobotListener(mapPanel);
		conn.enqueueCommand(new MInitialiseCommand());
		JButton saveImage = new JButton("Save to PNG");
		saveImage.setPreferredSize(new Dimension(200,15));
		saveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage img = mapPanel.getMapImage();
				if (img != null) {
					if (!mapPanel.writeImgToFile(outputFile))
						console.println("Error writing image to PNG");
				}
				else {
					console.println("No image exists to save to PNG");
				}
			}
		});
		
		// Construct panel
		pane.add(mapPanel, BorderLayout.CENTER);
		pane.add(saveImage, BorderLayout.PAGE_END);
		
		// pack and make visible
		setContentPane(pane);
		this.pack();
		this.setVisible(true);
	}
	
}