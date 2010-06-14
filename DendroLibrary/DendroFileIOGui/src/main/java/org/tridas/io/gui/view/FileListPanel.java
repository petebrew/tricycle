/**
 * Created on May 25, 2010, 8:19:22 PM
 */
package org.tridas.io.gui.view;

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.fileList.AddFileEvent;
import org.tridas.io.gui.control.fileList.BrowseEvent;
import org.tridas.io.gui.control.fileList.RemoveSelectedEvent;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.MainWindowModel;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class FileListPanel extends JPanel {
	private static final SimpleLogger log = new SimpleLogger(FileListPanel.class);
	
	private JLabel selectLabel;
	private JLabel fileFieldLabel;
	private JList fileList;
	private JButton addButton;
	private JButton browseButton;
	private JButton selectAllButton;
	private JButton selectNoneButton;
	private JButton removeSelectedButton;
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
		selectLabel = new JLabel();
		selectAllButton = new JButton();
		selectNoneButton = new JButton();
		scrollPane = new JScrollPane();
		
		setLayout(new BorderLayout());
		
		Box topBox = Box.createHorizontalBox();
		topBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
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
		
		bottomBox.add(removeSelectedButton);
		bottomBox.add(Box.createRigidArea(new Dimension(10, 10)));
		bottomBox.add(selectLabel);
		bottomBox.add(Box.createRigidArea(new Dimension(10, 10)));
		bottomBox.add(selectAllButton);
		bottomBox.add(selectNoneButton);
		bottomBox.add(Box.createHorizontalGlue());
		
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
	}
	
	public void populateLocale() {
		fileFieldLabel.setText(I18n.getText("view.files.fileLabel"));
		browseButton.setText(I18n.getText("view.files.browse"));
		addButton.setText(I18n.getText("view.files.add"));
		selectAllButton.setText(I18n.getText("view.files.selectAll"));
		selectNoneButton.setText(I18n.getText("view.files.selectNone"));
		removeSelectedButton.setText(I18n.getText("view.files.removeSelected"));
		selectLabel.setText(I18n.getText("view.files.selectLabel"));
	}
	
	public void linkModel() {
		
		// first set all values from model
		DefaultListModel listModel = (DefaultListModel) fileList.getModel();
		ArrayList<String> files = model.getInputFiles();
		listModel.clear();
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
