/**
 * Created on Jun 7, 2010, 12:12:08 AM
 */
package org.tridas.io.gui.view.popup;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.tridas.io.util.IOUtils;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class AboutWindow extends JDialog {
	
	private JTextArea info;
	private JFrame parent;
	public AboutWindow(JFrame argParent) {
		super(argParent, "About TRiCYCLE", true);
		parent = argParent;
	}
	
	/**
	 * @see javax.swing.JDialog#dialogInit()
	 */
	@Override
	protected void dialogInit() {
		initComponents();
		populateLocale();
		addListeners();
		pack();
		setLocationRelativeTo(parent);
		setResizable(false);
	}

	/**
	 * 
	 */
	private void initComponents() {
		
		JLabel logo = new JLabel(new ImageIcon(IOUtils.getFileInJarURL("icons/128x128/application.png")));
		info = new JTextArea();
		info.setEditable(false);
		add(logo, "West");
		add(info, "Center");
	
//		title = new JLabel();
//		title.setHorizontalAlignment(SwingConstants.CENTER);
//		title.setFont(title.getFont().deriveFont(20f));
//		
//		byLine1 = new JLabel();
//		byLine1.setFont(byLine1.getFont().deriveFont(10f));
//		
//		byLine2 = new JLabel();
//		byLine2.setFont(byLine2.getFont().deriveFont(10f));
//		
//		Box rbox = Box.createVerticalBox();
//		rbox.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
//		rbox.add(title);
//		rbox.add(byLine1);
//		rbox.add(byLine2);
//		rbox.add(new JLabel("Copyright stuff?"));
//		rbox.add(new JLabel("Link to project page?"));
//		rbox.add(new JLabel("TODO: choose a font"));
//		
//		JPanel rpanel = new JPanel();
//		rpanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
//		rpanel.add(rbox, "Center");
//		
//		Box box = Box.createHorizontalBox();
//		box.add(logo);
//		box.add(rpanel);
//		
//		add(box, "Center");
	}
	
	
	/**
	 * 
	 */
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			/**
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
	}
	
	private void closeWindow(){
		setVisible(false);
	}
	
	private void populateLocale() {
//		setTitle("About TRiCYCLE");
//		title.setText("TRiCYCLE");
//		byLine1.setText("Dendro IO Library by Peter Brewer, Daniel Murphy, and Esther Jansma");
//		byLine2.setText("Graphical interface by Daniel Murphy");
		info.setText("TRiCYCLE\n" +
					 "Version: 0.0.1\n" +
					 "\n" +
					 "Dendro IO Libary by Peter Brewer, Daniel Murphy, and Esther Jansma\n" +
					 "Graphical interface by Daniel Murphy\n" +
					 "\n" +
					 "Copyright info");
	}
}
