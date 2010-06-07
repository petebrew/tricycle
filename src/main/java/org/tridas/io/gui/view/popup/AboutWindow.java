/**
 * Created on Jun 7, 2010, 12:12:08 AM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class AboutWindow extends JDialog {
	
	public AboutWindow(JFrame parent) {
		//super("About");
		initComponents();
		populateLocale();
		setLocationRelativeTo(parent);
		setModal(true);
	}
	
	/**
	 * 
	 */
	private void initComponents() {
		
		Box lbox = Box.createVerticalBox();
		lbox.add(new JLabel("Icon"));
		lbox.add(new JLabel("goes"));
		lbox.add(new JLabel("here"));
		
		Box rbox = Box.createVerticalBox();
		rbox.setAlignmentX(RIGHT_ALIGNMENT);
		rbox.setBorder(new LineBorder(Color.black, 1, true));
		rbox.add(new JLabel("TRiCYCLE"));
		rbox.add(new JLabel("copyright info"));
		rbox.add(new JLabel("website"));
		rbox.add(new JLabel("other stuff"));
		
		Box box = Box.createHorizontalBox();
		box.add(lbox);
		box.add(Box.createRigidArea(new Dimension(10, 10)));
		box.add(Box.createHorizontalGlue());
		box.add(rbox);
		
		add(box, "Center");
	}
	
	private void populateLocale() {
	// nothing for now
	}
}
