/**
 * Created at Jun 20, 2010, 5:00:46 PM
 */
package org.tridas.io.gui.view.popup;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.charset.Charset;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.enums.NamingConvention;
import org.tridas.io.gui.enums.OutputFormat;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.MainWindowModel;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.StringEvent;

/**
 * @author daniel
 *
 */
public class OptionsWindow extends JDialog {
	private JComboBox readingCharset;
	private JButton readingDefaults;
	
	private JComboBox namingConvention;
	private JComboBox writingCharset;
	private JButton writingDefaults;
	private JButton cancelButton;
	private JButton okButton;
		
	private ConfigModel model = ConfigModel.getInstance();
	
	public OptionsWindow(JFrame argOwner) {
		super(argOwner, true);
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(argOwner);
	}
	
	/**
	 * 
	 */
	private void initializeComponents() {
		namingConvention = new JComboBox();
		writingCharset = new JComboBox();
		readingCharset = new JComboBox();
		readingDefaults = new JButton();
		writingDefaults = new JButton();
		cancelButton = new JButton();
		okButton = new JButton();
		
		Box panel = Box.createVerticalBox();
		
		JPanel readingPanel = new JPanel();
		readingPanel.setBorder(BorderFactory.createTitledBorder(I18n.getText("view.options.readingPanel")));
		readingPanel.setLayout(new GridLayout(0, 1, 5, 5));
				
		
		readingCharset.setEditable(false);
		Box rcBox = Box.createHorizontalBox();
		rcBox.add(new JLabel(I18n.getText("view.options.input.charset")));
		rcBox.add(readingCharset);
		
		readingPanel.add(rcBox);
		readingPanel.add(readingDefaults);
				
		JPanel writingPanel = new JPanel();
		writingPanel.setBorder(BorderFactory.createTitledBorder(I18n.getText("view.options.writerPanel")));
		writingPanel.setLayout(new GridLayout(0, 1, 5, 5));
		
		namingConvention.setEditable(false);
		Box ncBox = Box.createHorizontalBox();
		ncBox.add(new JLabel(I18n.getText("view.options.output.naming")));
		ncBox.add(namingConvention);
		
		writingCharset.setEditable(false);
		Box ocBox = Box.createHorizontalBox();
		ocBox.add(new JLabel(I18n.getText("view.options.output.charset")));
		ocBox.add(writingCharset);
		
		writingPanel.add(ncBox);
		writingPanel.add(ocBox);
		writingPanel.add(writingDefaults);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		panel.add(readingPanel);
		panel.add(writingPanel);
		panel.add(Box.createVerticalGlue());
		panel.add(buttonPanel);
		add(panel, "Center");
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		namingConvention.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				String naming = namingConvention.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_NAMING_CONVENTION, naming);
				event.dispatch();
			}
		});
		
		
		writingCharset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				String charset = writingCharset.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_WRITING_CHARSET, charset);
				event.dispatch();
			}
		});
		
		readingCharset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				String charset = readingCharset.getSelectedItem().toString();
				StringEvent event = new StringEvent(ConfigController.SET_READING_CHARSET, charset);
				event.dispatch();
			}
		});
		
		readingDefaults.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				MVCEvent event = new MVCEvent(ConfigController.INPUT_DEFAULTS_PRESSED);
				event.dispatch();
			}
		});
		
		writingDefaults.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent argE) {
				getRootPane().putClientProperty("Window.documentModified", Boolean.TRUE);
				MVCEvent event = new MVCEvent(ConfigController.OUTPUT_DEFAULTS_PRESSED);
				event.dispatch();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.revertChanges();
				getRootPane().putClientProperty("Window.documentModified", Boolean.FALSE);
				setVisible(false);
			}
		});
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				model.saveChanges();
				getRootPane().putClientProperty("Window.documentModified", Boolean.FALSE);
				setVisible(false);
			}
		});
	}
	
	/**
	 * 
	 */
	private void populateLocale() {
		setTitle(I18n.getText("view.options.title"));
		readingDefaults.setText(I18n.getText("view.options.input.defaults"));
		writingDefaults.setText(I18n.getText("view.options.output.defaults"));
		okButton.setText(I18n.getText("view.options.ok"));
		cancelButton.setText(I18n.getText("view.options.cancel"));

		for (String conv : NamingConvention.getNamingConventions()) {
			namingConvention.addItem(conv);
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
		FileListModel fmodel = FileListModel.getInstance();
		ConvertModel cmodel = ConvertModel.getInstance();
		namingConvention.setSelectedItem(model.getNamingConvention());
		readingCharset.setSelectedItem(model.getReadingCharset());
		writingCharset.setSelectedItem(model.getWritingCharset());
		model.saveChanges();
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				String prop = argEvt.getPropertyName();
				
				if (prop.equals("namingConvention")) {
					namingConvention.setSelectedItem(argEvt.getNewValue());
				}
				else if (prop.equals("writingCharset")) {
					writingCharset.setSelectedItem(argEvt.getNewValue());
				}
				else if (prop.equals("readingCharset")) {
					readingCharset.setSelectedItem(argEvt.getNewValue());
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
						namingConvention.setEnabled(false);
						readingCharset.setEnabled(false);
						writingCharset.setEnabled(false);
						readingDefaults.setEnabled(false);
						writingDefaults.setEnabled(false);
					}
					else {
						namingConvention.setEnabled(true);
						readingCharset.setEnabled(true);
						writingCharset.setEnabled(true);
						readingDefaults.setEnabled(true);
						writingDefaults.setEnabled(true);
					}
				}
			}
		});
	}
}
