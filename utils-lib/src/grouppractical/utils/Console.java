package grouppractical.utils;

import java.awt.GridBagLayout;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Class to construct and display a window with a non-editable text area, which can be used to display lines of text
 * @author janslow
 *
 */
public class Console extends JFrame {
	/**
	 *	UID of class
	 */
	private static final long serialVersionUID = 7314486690127933986L;
	private final JPanel panel;
	private final JTextArea text;

	/**
	 * Constructs new output console
	 * @param title Title of window
	 */
	public Console(String title) {
		//Constructs JFrame
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Constructs JPanel
		panel = new JPanel(new GridBagLayout());
		
		//Constructs JTextArea (of size 20 rows by 50 columns) which is not editable
		text = new JTextArea(20, 50);
		text.setEditable(false);
		
		//Wraps JTextArea in a scroll pane then adds it to JPanel, then the JPanel to the JFrame
		JScrollPane scrollPane = new JScrollPane(text);
	    panel.add(scrollPane);
	    this.add(panel);
	    
	    //Packs and displays JFrame
	    this.pack();
	    this.setVisible(true);
	}
	
	@Override
	public void addKeyListener(KeyListener k) {
		super.addKeyListener(k);
		text.addKeyListener(k);
	}

	/**
	 * Prints a string, followed by a new line character, to the console
	 * @param s String to print
	 */
	public void println(String s) {
		text.append(s);
		text.append("\n");
		text.setCaretPosition(text.getDocument().getLength());
	}
	
	/**
	 * Prints a string to the console
	 * @param s String to print
	 */
	public synchronized void print(String s) {
		text.append(s);
		text.setCaretPosition(text.getDocument().getLength());
	}
}