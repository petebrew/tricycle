/**
 * Created on Jun 7, 2010, 6:33:38 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.tridas.io.gui.model.PreviewModel;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class PreviewWindow extends JFrame {
	
	private final PreviewModel model;
	private JTextArea text;
	
	public PreviewWindow(JFrame argParent, int argWidth, int argHeight, PreviewModel argModel) {
		model = argModel;
		initComponents();
		populateLocale();
		linkModel();
		addListeners();
		
		setSize(argWidth, argHeight);
		setLocationRelativeTo(argParent);
	}
	
	private void initComponents() {
		text = new JTextArea();
		text.setEditable(false);
		Font font = new Font("Monospaced", Font.PLAIN, 11);
		
		text.setFont(font);
		add(new JScrollPane(text), "Center");
	}
	
	private void populateLocale() {
	// nothing to do here
	}
	
	private void setTitleName(String argFilename) {
		if (argFilename == null) {
			setTitle("Preview");
		}
		setTitle("Preview of " + argFilename);
	}
	
	private void linkModel() {
		text.setText(model.getFileString());
		setTitleName(model.getFilename());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String p = evt.getPropertyName();
				
				if (p.equals("fileString")) {
					text.setText(evt.getNewValue().toString());
				}
				else if (p.equals("filename")) {
					setTitleName(evt.getNewValue().toString());
				}
			}
		});
	}
	
	private void addListeners() {
	// nothing to do here
	}
}
