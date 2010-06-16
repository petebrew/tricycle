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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.enums.NamingConvention;
import org.tridas.io.gui.enums.OutputFormat;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.MainWindowModel;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.StringEvent;

/**
 * @author Daniel Murphy
 */
@SuppressWarnings("serial")
public class ConfigPanel extends JPanel {
	
	private JComboBox inputFormat;
	private JComboBox readingCharset;
	private JButton readingDefaults;
	
	private JComboBox outputFormat;
	private JComboBox namingConvention;
	private JComboBox writingCharset;
	private JButton writingDefaults;
		
	private ConfigModel model = ConfigModel.getInstance();
	
	public ConfigPanel() {
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
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
		readingDefaults = new JButton();
		writingDefaults = new JButton();
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5,5));
		
		JPanel readingPanel = new JPanel();
		readingPanel.setBorder(BorderFactory.createTitledBorder(I18n.getText("view.config.readingPanel")));
		readingPanel.setLayout(new GridLayout(0, 1, 5, 5));
		
		Box rpBox = Box.createVerticalBox();
		
		// TODO locale all of these
		inputFormat.setEditable(false);
		Box ifBox = Box.createHorizontalBox();
		ifBox.add(new JLabel(I18n.getText("view.config.input.format")));
		ifBox.add(inputFormat);
		
		readingCharset.setEditable(false);
		Box rcBox = Box.createHorizontalBox();
		rcBox.add(new JLabel(I18n.getText("view.config.input.charset")));
		rcBox.add(readingCharset);
		
		readingPanel.add(ifBox);
		readingPanel.add(rcBox);
		readingPanel.add(readingDefaults);
				
		JPanel writingPanel = new JPanel();
		writingPanel.setBorder(BorderFactory.createTitledBorder(I18n.getText("view.config.writerPanel")));
		writingPanel.setLayout(new GridLayout(0, 1, 5, 5));
		
		namingConvention.setEditable(false);
		Box ncBox = Box.createHorizontalBox();
		ncBox.add(new JLabel(I18n.getText("view.config.output.naming")));
		ncBox.add(namingConvention);
		
		outputFormat.setEditable(false);
		Box ofBox = Box.createHorizontalBox();
		ofBox.add(new JLabel(I18n.getText("view.config.output.format")));
		ofBox.add(outputFormat);
		
		writingCharset.setEditable(false);
		Box ocBox = Box.createHorizontalBox();
		ocBox.add(new JLabel(I18n.getText("view.config.output.charset")));
		ocBox.add(writingCharset);
		
		writingPanel.add(ofBox);
		writingPanel.add(ncBox);
		writingPanel.add(ocBox);
		writingPanel.add(writingDefaults);
		
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
		
		readingDefaults.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				MVCEvent event = new MVCEvent(ConfigController.INPUT_DEFAULTS_PRESSED);
				event.dispatch();
			}
		});
		
		writingDefaults.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				MVCEvent event = new MVCEvent(ConfigController.OUTPUT_DEFAULTS_PRESSED);
				event.dispatch();
			}
		});
	}
	
	/**
	 * 
	 */
	private void populateLocale() {
		
		readingDefaults.setText(I18n.getText("view.config.input.defaults"));
		writingDefaults.setText(I18n.getText("view.config.output.defaults"));
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
		for (String s : Charsets.getReadingCharsets()) {
			if (s.equals(Charset.defaultCharset().displayName())) {
				continue;
			}
			readingCharset.addItem(s);
		}
		
		for (String s : Charsets.getWritingCharsets()) {
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
				else if (prop.equals("writingCharset")) {
					writingCharset.setSelectedItem(evt.getNewValue());
				}
				else if (prop.equals("readingCharset")) {
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
