/**
 * Created on May 25, 2010, 8:19:22 PM
 */
package org.tridas.io.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.fileList.AddFileEvent;
import org.tridas.io.gui.control.fileList.BrowseEvent;
import org.tridas.io.gui.control.fileList.RemoveSelectedEvent;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.MainWindowModel;

import com.dmurph.mvc.StringEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class FileListPanel extends JPanel {
	private static final SimpleLogger log = new SimpleLogger(FileListPanel.class);
	
	private JLabel fileFieldLabel;
	private JList fileList;
	private JComboBox inputFormat;
	private JButton addButton;
	private JButton browseButton;
	private JButton selectAllButton;
	private JButton selectNoneButton;
	private JButton removeSelectedButton;
	private JButton removeAll;
	private JScrollPane scrollPane;
	private JTextField fileField;
	
	private FileListModel model = FileListModel.getInstance();
	
	public FileListPanel() {
		initComponents();
		populateLocale();
		linkModel();
		addListeners();
	}
	
	public void initComponents() {
		addButton = new JButton();
		fileFieldLabel = new JLabel();
		fileField = new JTextField();
		browseButton = new JButton();
		fileList = new JList();
		removeSelectedButton = new JButton();
		selectAllButton = new JButton();
		selectNoneButton = new JButton();
		removeAll = new JButton();
		scrollPane = new JScrollPane();
		inputFormat = new JComboBox();
		
		setLayout(new BorderLayout());
		
		Box topBox = Box.createHorizontalBox();
		topBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		topBox.add(inputFormat);
		topBox.add(fileFieldLabel);
		topBox.add(fileField);
		topBox.add(browseButton);
		topBox.add(addButton);
		
		add(topBox, java.awt.BorderLayout.PAGE_START);
		
		fileList.setModel(new DefaultListModel());
		scrollPane.setViewportView(fileList);
		
		add(scrollPane, java.awt.BorderLayout.CENTER);
		
		Box bottomBox = Box.createHorizontalBox();
		bottomBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
		selectAllButton.putClientProperty( "JButton.buttonType", "segmentedTextured" );
		selectAllButton.putClientProperty( "JButton.segmentPosition", "middle" );
		selectNoneButton.putClientProperty( "JButton.buttonType", "segmentedTextured" );
		selectNoneButton.putClientProperty( "JButton.segmentPosition", "last" );
		removeSelectedButton.putClientProperty( "JButton.buttonType", "segmentedTextured" );
		removeSelectedButton.putClientProperty( "JButton.segmentPosition", "first" );
		
		removeAll.putClientProperty("JButton.buttonType", "textured");
		
		bottomBox.add(removeSelectedButton);
		bottomBox.add(selectAllButton);
		bottomBox.add(selectNoneButton);
		bottomBox.add(Box.createHorizontalGlue());
		bottomBox.add(removeAll);
		
		add(bottomBox, java.awt.BorderLayout.PAGE_END);
	}
	
	private void addListeners() {
		
		selectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultListModel model = (DefaultListModel) fileList.getModel();
				fileList.setSelectionInterval(0, model.getSize() - 1);
			}
		});
		selectNoneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileList.clearSelection();
			}
		});
		removeSelectedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HashSet<String> set = new HashSet<String>();
				for (Object o : fileList.getSelectedValues()) {
					set.add(o.toString());
				}
				RemoveSelectedEvent event = new RemoveSelectedEvent(set);
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
		
		fileField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// skip event to controller
				model.setFileField(fileField.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// skip event to controller
				model.setFileField(fileField.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		
		ActionListener addFileListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddFileEvent event = new AddFileEvent(fileField.getText());
				event.dispatch();
			}
		};
		
		fileField.addActionListener(addFileListener);
		addButton.addActionListener(addFileListener);
		
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BrowseEvent event = new BrowseEvent();
				event.dispatch();
			}
		});
		
		removeAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				fileList.removeAll();
			}
		});
	}
	
	public void populateLocale() {
		fileFieldLabel.setText(I18n.getText("view.files.fileLabel"));
		browseButton.setText(I18n.getText("view.files.browse"));
		addButton.setText(I18n.getText("view.files.add"));
		selectAllButton.setText(I18n.getText("view.files.selectAll"));
		selectNoneButton.setText(I18n.getText("view.files.selectNone"));
		removeSelectedButton.setText(I18n.getText("view.files.removeSelected"));
		removeAll.setText(I18n.getText("view.files.removeAll"));
		
		for (String s : InputFormat.getInputFormats()) {
			inputFormat.addItem(s);
		}
	}
	
	public void linkModel() {
		inputFormat.setSelectedItem(model.getInputFormat());
		// first set all values from model
		DefaultListModel listModel = (DefaultListModel) fileList.getModel();
		ArrayList<String> files = model.getInputFiles();
		listModel.clear();
		// swing is doing weird stuff, so we spcae it out
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		for (String file : files) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
			listModel.addElement(file);
		}
		fileField.setText(model.getFileField());
		
		// then listen for changes
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				log.dbo(DebugLevel.L5_DEBUG, "Property change event received: " + prop, evt.getNewValue());
				
				if (prop.equals("inputFiles")) {
					DefaultListModel model = (DefaultListModel) fileList.getModel();
					ArrayList<String> files = (ArrayList<String>) evt.getNewValue();
					model.clear();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					for (String file : files) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {}
						model.addElement(file);
					}
					// fileList.setModel(model);
				}
				else if (prop.equals("fileField")) {
					if (!fileField.getText().equals(evt.getNewValue().toString())) {
						fileField.setText(evt.getNewValue().toString());
					}
				}else if (prop.equals("inputFormat")) {
					inputFormat.setSelectedItem(evt.getNewValue());
				}
				repaint();
			}
		});
		
		MainWindowModel mwm = MainWindowModel.getInstance();
		mwm.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if (evt.getPropertyName().equals("lock")) {
					boolean lock = (Boolean) evt.getNewValue();
					if (lock) {
						browseButton.setEnabled(false);
						addButton.setEnabled(false);
						fileField.setEnabled(false);
						removeSelectedButton.setEnabled(false);
						selectAllButton.setEnabled(false);
						selectNoneButton.setEnabled(false);
					}
					else {
						browseButton.setEnabled(true);
						addButton.setEnabled(true);
						fileField.setEnabled(true);
						removeSelectedButton.setEnabled(true);
						selectAllButton.setEnabled(true);
						selectNoneButton.setEnabled(true);
					}
				}
			}
		});
	}
	
}
