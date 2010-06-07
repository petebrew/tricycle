/**
 * Created on Jun 7, 2010, 12:12:08 AM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import org.tridas.io.util.IOUtils;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class AboutWindow extends JFrame {
	
	private JLabel title;
	private JLabel byLine1;
	private JLabel byLine2;

	public AboutWindow(JFrame parent) {
		initComponents();
		populateLocale();
		setLocationRelativeTo(parent);
		pack();
		setResizable(false);
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		
		JLabel logo = new JLabel(new ImageIcon(IOUtils.getFileInJarURL("icons/128x128/application.png")));
		
		title = new JLabel();
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont( title.getFont().deriveFont(20f));
		
		byLine1 = new JLabel();
		byLine1.setFont(byLine1.getFont().deriveFont(10f));
		
		byLine2 = new JLabel();
		byLine2.setFont(byLine2.getFont().deriveFont(10f));
		
		
		Box rbox = Box.createVerticalBox();
		rbox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		rbox.add(title);
		rbox.add(byLine1);
		rbox.add(byLine2);
		rbox.add(new JLabel("Copyright stuff?"));
		rbox.add(new JLabel("Link to project page?"));
		rbox.add(new JLabel("TODO: choose a font"));
		
		JPanel rpanel = new JPanel();
		rpanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
		rpanel.add(rbox, "Center");
		
		Box box = Box.createHorizontalBox();
		box.add(logo);
		box.add(rpanel);
		
		add(box, "Center");
	}
	
	private void populateLocale() {
		setTitle("About");
		title.setText("TRiCYCLE");
		byLine1.setText("Dendro IO Library by Peter Brewer, Daniel Murphy, and Esther Jansma");
		byLine2.setText("Graphical interface by Daniel Murphy");
	}
}
