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
import java.nio.charset.Charset;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.enums.NamingConvention;
import org.tridas.io.gui.enums.OutputFormat;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.MainWindowModel;

import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.StringEvent;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class ConfigPanel extends JPanel {
	
	private JComboBox inputFormat;
	private JComboBox readingCharset;
	
	private JComboBox outputFormat;
	private JComboBox namingConvention;
	private JComboBox writingCharset;
	
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
		writingCharset = new JComboBox();
		readingCharset = new JComboBox();
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0));
		
		JPanel readingPanel = new JPanel();
		readingPanel.setBorder(BorderFactory.createTitledBorder("Reader Config"));
		readingPanel.setLayout(new GridLayout(0,1,5,5));
		
		// TODO locale all of these
		inputFormat.setEditable(false);
		Box ifBox = Box.createHorizontalBox();
		ifBox.add(Box.createHorizontalGlue());
		ifBox.add(new JLabel("Input Format: "));
		ifBox.add(inputFormat);
		ifBox.add(Box.createHorizontalGlue());
		
		readingCharset.setEditable(false);
		Box rcBox = Box.createHorizontalBox();
		rcBox.add(Box.createHorizontalGlue());
		rcBox.add(new JLabel("Reading Charset: "));
		rcBox.add(readingCharset);
		rcBox.add(Box.createHorizontalGlue());
		
		readingPanel.add(ifBox);
		readingPanel.add(rcBox);
		
		JPanel writingPanel = new JPanel();
		writingPanel.setBorder(BorderFactory.createTitledBorder("Writer Config"));
		writingPanel.setLayout(new GridLayout(0,1,5,5));

		namingConvention.setEditable(false);
		Box ncBox = Box.createHorizontalBox();
		ncBox.add(Box.createHorizontalGlue());
		ncBox.add(new JLabel("Naming Convention: "));
		ncBox.add(namingConvention);
		ncBox.add(Box.createHorizontalGlue());

		outputFormat.setEditable(false);
		Box ofBox = Box.createHorizontalBox();
		ofBox.add(Box.createHorizontalGlue());
		ofBox.add(new JLabel("Output Format:"));
		ofBox.add(outputFormat);
		ofBox.add(Box.createHorizontalGlue());
		
		writingCharset.setEditable(false);
		Box ocBox = Box.createHorizontalBox();
		ocBox.add(Box.createHorizontalGlue());
		ocBox.add(new JLabel("Writing Charset: "));
		ocBox.add(writingCharset);
		ocBox.add(Box.createHorizontalGlue());
		
		writingPanel.add(ofBox);
		writingPanel.add(ncBox);
		writingPanel.add(ocBox);
		
		panel.add(readingPanel);
		panel.add(writingPanel);
		
		add(panel, "Center");
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		namingConvention.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String naming = namingConvention.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_NAMING_CONVENTION, naming);
				event.dispatch();
			}
		});
		
		outputFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String output = outputFormat.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_OUTPUT_FORMAT, output);
				event.dispatch();
			}
		});
		
		inputFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = inputFormat.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_INPUT_FORMAT, input);
				event.dispatch();
			}
		});
		
		writingCharset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String charset = writingCharset.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_WRITING_CHARSET, charset);
				event.dispatch();
			}
		});
		
		readingCharset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String charset = readingCharset.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_READING_CHARSET, charset);
				event.dispatch();
			}
		});
		
	}
	
	/**
	 * 
	 */
	private void populateLocale() {
		
		for (String s : InputFormat.getInputFormats()) {
			inputFormat.addItem(s);
		}
		
		for (String conv : NamingConvention.getNamingConventions()) {
			namingConvention.addItem(conv);
		}
		
		for (String out : OutputFormat.getOutputFormats()) {
			outputFormat.addItem(out);
		}
		
		// put default one on top, so the user will see
		// "automatic" right underneath
		readingCharset.addItem(Charset.defaultCharset().displayName());
		for(String s : Charsets.getReadingCharsets()){
			if(s.equals(Charset.defaultCharset().displayName())){
				continue;
			}
			readingCharset.addItem(s);
		}
		
		for(String s : Charsets.getWritingCharsets()){
			writingCharset.addItem(s);
		}
	}
	
	/**
	 * 
	 */
	private void linkModel() {
		inputFormat.setSelectedItem(model.getInputFormat());
		namingConvention.setSelectedItem(model.getNamingConvention());
		outputFormat.setSelectedItem(model.getOutputFormat());
		readingCharset.setSelectedItem(model.getReadingCharset());
		writingCharset.setSelectedItem(model.getWritingCharset());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("inputFormat")) {
					inputFormat.setSelectedItem(evt.getNewValue());
				}
				else if (prop.equals("namingConvention")) {
					namingConvention.setSelectedItem(evt.getNewValue());
				}
				else if (prop.equals("outputFormat")) {
					outputFormat.setSelectedItem(evt.getNewValue());
				}
				else if(prop.equals("writingCharset")){
					writingCharset.setSelectedItem(evt.getNewValue());
				}
				else if(prop.equals("readingCharset")){
					readingCharset.setSelectedItem(evt.getNewValue());
				}
			}
		});
		
		MainWindowModel mwm = MainWindowModel.getInstance();
		mwm.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if (evt.getPropertyName().equals("lock")) {
					boolean lock = (Boolean) evt.getNewValue();
					if (lock) {
						inputFormat.setEnabled(false);
						namingConvention.setEnabled(false);
						outputFormat.setEnabled(false);
						readingCharset.setEnabled(false);
						writingCharset.setEnabled(false);
					}
					else {
						inputFormat.setEnabled(true);
						namingConvention.setEnabled(true);
						outputFormat.setEnabled(true);
						readingCharset.setEnabled(true);
						writingCharset.setEnabled(true);
					}
				}
			}
		});
	}
}
