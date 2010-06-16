/**
 * Created on Jun 7, 2010, 12:12:08 AM
 */
package org.tridas.io.gui.view.popup;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.tridas.io.gui.I18n;
import org.tridas.io.util.IOUtils;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class AboutWindow extends JDialog {
	
	private JTextArea info;
	
	public AboutWindow(JFrame argParent) {
		super(argParent, true);
		initComponents();
		populateLocale();
		addListeners();
		pack();
		setSize(600, 128);
		setResizable(false);
		setLocationRelativeTo(argParent);
	}

	/**
	 * 
	 */
	private void initComponents() {
		
		JLabel llogo = new JLabel(new ImageIcon(IOUtils.getFileInJarURL("icons/about/application-left.png")));
		JLabel rlogo = new JLabel(new ImageIcon(IOUtils.getFileInJarURL("icons/about/application-right.png")));
		info = new JTextArea();
		info.setEditable(false);
		
		Box box = Box.createHorizontalBox();
		add(box, "Center");
		box.add(llogo);
		box.add(new JScrollPane(info));
		box.add(rlogo);
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
		setTitle(I18n.getText("view.popup.about.title"));
//		title.setText("TRiCYCLE");
//		byLine1.setText("Dendro IO Library by Peter Brewer, Daniel Murphy, and Esther Jansma");
//		byLine2.setText("Graphical interface by Daniel Murphy");
		info.setText(I18n.getText("view.popup.about.text"));
	}
}
