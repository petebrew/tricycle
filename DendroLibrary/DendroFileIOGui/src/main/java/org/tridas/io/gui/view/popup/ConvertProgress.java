/**
 * Created on Jun 7, 2010, 2:51:29 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.ModelLocator;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class ConvertProgress extends JDialog {
	private JLabel convertingLabel;
	private JProgressBar progress;
	
	private ConvertModel model = ConvertModel.getInstance();
	
	public ConvertProgress(JFrame parent) {
		super(parent, true);
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(parent);
	}
	
	private void initializeComponents() {
		convertingLabel = new JLabel();
		progress = new JProgressBar();
		
		setLayout(new GridLayout(0, 1));
		
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		
		add(convertingLabel);
		add(progress);
		setPreferredSize(new Dimension(350, 75));
	}
	
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}
	
	private void populateLocale() {
		setIconImage(ModelLocator.getInstance().getWindowIcon().getImage());
		setTitle(I18n.getText("view.popup.convert.title"));
		setConvertingFilename("");
	}
	
	private void setConvertingFilename(String argFilename) {
		if (argFilename == null) {
			argFilename = "";
		}
		convertingLabel.setText(I18n.getText("view.popup.convert.label", argFilename));
	}
	
	private void linkModel() {
		if (model.getConvertingFilename() != null) {
			setConvertingFilename(model.getConvertingFilename());
		}
		
		progress.setValue(model.getConvertingPercent());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("convertingPercent")) {
					int percent = (Integer) evt.getNewValue();
					progress.setValue(percent);
				}
				else if (prop.equals("convertingFilename")) {
					setConvertingFilename(evt.getNewValue().toString());
				}
			}
		});
	}
}
