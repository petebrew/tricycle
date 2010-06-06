/**
 * Created on Jun 5, 2010, 10:46:50 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.tridas.io.gui.model.ConvertModel;

/**
 * @author Daniel Murphy
 *
 */
public class SavingProgress extends JFrame{

	private JLabel savingLabel;
	private JProgressBar progress;
	
	private ConvertModel model = ConvertModel.getInstance();
	
	public SavingProgress(){
		super("Saving...");
		initializeComponents();
		addListeners();
		populateLocale();
		linkModel();
		pack();
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
		// none
	}

	/**
	 * 
	 */
	private void populateLocale() {
		savingLabel.setText("Saving: ");
	}
	
	private void setSavingFilename(String argFilename){
		if(argFilename == null){
			argFilename = "";
		}
		savingLabel.setText("Saving: "+argFilename);
	}

	/**
	 * 
	 */
	private void linkModel() {
		if(model.getSavingFilename() != null){
			savingLabel.setText("Saving: "+model.getSavingFilename());
		}
		
		progress.setValue(model.getSavingPercent());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if(prop.equals("savingPercent")){
					int percent = (Integer) evt.getNewValue();
					progress.setValue(percent);
				}else if(prop.equals("savingFilename")){
					setSavingFilename(evt.getNewValue().toString());
				}
			}
		});
	}
}
