/**
 * Created on Jun 5, 2010, 10:46:50 PM
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
import org.tridas.io.gui.model.popup.SavingDialogModel;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class SavingProgress extends JDialog {
	
	private JLabel savingLabel;
	private JProgressBar progress;
	
	private final SavingDialogModel model;
	
	public SavingProgress(JFrame parent, SavingDialogModel argModel) {
		super(parent, true);
		model = argModel;
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(parent);
	}
	
	/**
	 * 
	 */
	private void initializeComponents() {
		savingLabel = new JLabel();
		progress = new JProgressBar();
		
		setLayout(new GridLayout(0, 1));
		
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		
		add(savingLabel);
		add(progress);
		setPreferredSize(new Dimension(350, 75));
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}
	
	/**
	 * 
	 */
	private void populateLocale() {
		setIconImage(ModelLocator.getInstance().getWindowIcon().getImage());
		setTitle(I18n.getText("view.popup.save.title"));
		setSavingFilename("");
	}
	
	private void setSavingFilename(String argFilename) {
		if (argFilename == null) {
			argFilename = "?";
		}
		savingLabel.setText(I18n.getText("view.popup.save.label", argFilename));
	}
	
	/**
	 * 
	 */
	private void linkModel() {
		if (model.getSavingFilename() != null) {
			setSavingFilename(model.getSavingFilename());
		}
		
		progress.setValue(model.getSavingPercent());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("savingPercent")) {
					int percent = (Integer) evt.getNewValue();
					progress.setValue(percent);
				}
				else if (prop.equals("savingFilename")) {
					setSavingFilename(evt.getNewValue().toString());
				}
			}
		});
	}
}
