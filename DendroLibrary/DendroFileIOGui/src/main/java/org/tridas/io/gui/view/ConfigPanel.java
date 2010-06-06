/**
 * Created on Jun 5, 2010, 4:25:18 PM
 */
package org.tridas.io.gui.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.enums.NamingConvention;
import org.tridas.io.gui.enums.OutputFormat;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.MainWindowModel;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class ConfigPanel extends JPanel {
	
	private JComboBox inputFormat;
	private JComboBox outputFormat;
	private JComboBox namingConvention;
	
	private ConfigModel model = ConfigModel.getInstance();
	
	public ConfigPanel() {
		initializeComponents();
		addListeners();
		populateLocale();
		linkModel();
	}

	/**
	 * 
	 */
	private void initializeComponents() {
		namingConvention = new JComboBox();
		outputFormat = new JComboBox();
		inputFormat = new JComboBox();

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		
		
		outputFormat.setEditable(true);
		JPanel outputFormatPanel = new JPanel();
		outputFormatPanel.add(new JLabel("Output Format:"));
		outputFormatPanel.add(outputFormat);

		inputFormat.setEditable(true);
		JPanel inputFormatPanel = new JPanel();
		inputFormatPanel.add(new JLabel("Input Format:"));
		inputFormatPanel.add(inputFormat);
		
		namingConvention.setEditable(false);
		JPanel namingConventionPanel = new JPanel();
		namingConventionPanel.add(new JLabel("Naming Convention:"));
		namingConventionPanel.add(namingConvention);
		
		panel.add(inputFormatPanel);
		panel.add(outputFormatPanel);
		panel.add(namingConventionPanel);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(panel);
	}

	/**
	 * 
	 */
	private void addListeners() {
		namingConvention.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// skip event to controller, implement that if needed
				model.setNamingConvention(namingConvention.getSelectedItem().toString());
			}
		});
		
		outputFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// skip event to controller, implement that if needed
				model.setOutputFormat(outputFormat.getSelectedItem().toString());
			}
		});
		
		inputFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// skip event to controller
				model.setInputFormat(inputFormat.getSelectedItem().toString());
			}
		});
	}

	/**
	 * 
	 */
	private void populateLocale() {
		
		
		inputFormat.addItem("automatic");
		for (String s : InputFormat.getInputFormats()) {
			inputFormat.addItem(s);
		}
		
		for(String conv : NamingConvention.getNamingConventions()){
			namingConvention.addItem(conv);
		}
		
		outputFormat.addItem("TRiDaS");
		for(String out : OutputFormat.getOutputFormats()){
			if(out.equals("TRiDaS")){
				continue;
			}
			outputFormat.addItem(out);
		}
	}

	/**
	 * 
	 */
	private void linkModel() {
		inputFormat.setSelectedItem(model.getInputFormat());
		namingConvention.setSelectedItem(model.getNamingConvention());
		outputFormat.setSelectedItem(model.getOutputFormat());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("inputFormat")) {
					inputFormat.setSelectedItem(evt.getNewValue());
				}else if(prop.equals("namingConvention")){
					namingConvention.setSelectedItem(evt.getNewValue());
				}else if(prop.equals("outputFormat")){
					outputFormat.setSelectedItem(evt.getNewValue());
				}
			}
		});
		
		MainWindowModel mwm = MainWindowModel.getInstance();
		mwm.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if(evt.getPropertyName().equals("lock")){
					boolean lock = (Boolean)evt.getNewValue();
					if(lock){
						inputFormat.setEnabled(false);
						namingConvention.setEnabled(false);
						outputFormat.setEnabled(false);
					}else{
						inputFormat.setEnabled(true);
						namingConvention.setEnabled(true);
						outputFormat.setEnabled(true);
					}
				}
			}
		});
	}
}
