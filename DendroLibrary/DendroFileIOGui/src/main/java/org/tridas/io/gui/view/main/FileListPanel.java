/**
 * Created on May 25, 2010, 8:19:22 PM
 */
package org.tridas.io.gui.view.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.control.main.fileList.AddFileEvent;
import org.tridas.io.gui.control.main.fileList.BrowseEvent;
import org.tridas.io.gui.control.main.fileList.FileListController;
import org.tridas.io.gui.control.main.fileList.RemoveSelectedEvent;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.model.fileList.FileListModel;
import org.tridas.io.gui.mvc.control.events.ObjectEvent;

/**
 * @author Daniel
 */
public class FileListPanel extends JPanel implements PropertyChangeListener {
	private static final SimpleLogger log = new SimpleLogger(FileListPanel.class);

	private JPanel topPanel;
	private JLabel selectLabel;
	private JLabel inputFormatLabel;
	private JLabel fileFieldLabel;
	private JList fileList;
	private JComboBox inputFormat;
	private JButton addButton;
	private JButton browseButton;
	private JButton selectAllButton;
	private JButton selectNoneButton;
	private JButton removeSelectedButton;
	private JScrollPane scrollPane;
	private JPanel bottomPanel;
	private JTextField fileField;

	private FileListModel model = FileListModel.getInstance();

	public FileListPanel() {
		initComponents();
		addListeners();
		populateLocale();
		linkModel();
	}
	
	public void initComponents() {
		topPanel = new JPanel();
		addButton = new JButton();
		inputFormat = new JComboBox();
		inputFormatLabel = new JLabel();
		fileFieldLabel = new JLabel();
		fileField = new JTextField();
		browseButton = new JButton();
		fileList = new JList();
		removeSelectedButton = new JButton();
		selectLabel = new JLabel();
		selectAllButton = new JButton();
		selectNoneButton = new JButton();
		scrollPane = new JScrollPane();
		bottomPanel = new JPanel();

		JPanel toptopPanel = new JPanel();
		JPanel bottomtopPanel = new JPanel();
		
		setLayout(new BorderLayout());

		topPanel.setLayout(new GridLayout(2,1));
		
		toptopPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
		bottomtopPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

		toptopPanel.add(fileFieldLabel);
		
		fileField.setColumns(20);
		toptopPanel.add(fileField);
		toptopPanel.add(browseButton);
		toptopPanel.add(addButton);

		bottomtopPanel.add(inputFormatLabel);

		inputFormat.setEditable(true);
		bottomtopPanel.add(inputFormat);
		
		topPanel.add(toptopPanel);
		topPanel.add(bottomtopPanel);

		add(topPanel, java.awt.BorderLayout.PAGE_START);
		
		fileList.setModel(new DefaultListModel());
		scrollPane.setViewportView(fileList);

		add(scrollPane, java.awt.BorderLayout.CENTER);

		bottomPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

		bottomPanel.add(removeSelectedButton);

		selectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bottomPanel.add(selectLabel);
		bottomPanel.add(selectAllButton);
		bottomPanel.add(selectNoneButton);

		add(bottomPanel, java.awt.BorderLayout.PAGE_END);
	}
	
	private void addListeners() {
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		selectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultListModel model = (DefaultListModel) fileList.getModel();
				fileList.setSelectionInterval(0, model.getSize()-1);
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
				for(Object o :fileList.getSelectedValues()){
					set.add(o.toString());
				}
				RemoveSelectedEvent event = new RemoveSelectedEvent(set);
				event.dispatch();
			}
		});
		
		inputFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// skip event to controller
				model.setInputFormat(inputFormat.getSelectedItem().toString());
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
			public void changedUpdate(DocumentEvent e) {
				log.debug("change: "+fileField.getText());
			}
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
		fileFieldLabel.setText("Choose file/url:");
		browseButton.setText("Browse");
		addButton.setText("Add...");
		selectAllButton.setText("All");
		selectNoneButton.setText("None");
		removeSelectedButton.setText("Remove Selected");
		selectLabel.setText("Select:");
		inputFormatLabel.setText("Input Format:");
		
		inputFormat.addItem("automatic");
		for (String s : InputFormat.getInputFormats()) {
			inputFormat.addItem(s);
		}
	}

	public void linkModel() {
		model.addPropertyChangeListener(this);
		model.addInputFile("HELLOOO");
		model.addInputFile("HELLOOO1");
		model.addInputFile("HELLOOO2");
		model.addInputFile("HELLOOO3");
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		log.dbo(DebugLevel.L5_DEBUG, "Property change event received: "+prop, evt.getNewValue());
		
		if (prop.equals("inputFormat")) {
			inputFormat.setSelectedItem(evt.getNewValue());
		}
		else if (prop.equals("inputFiles")) {
			DefaultListModel model = (DefaultListModel) fileList.getModel();
			ArrayList<String> files = (ArrayList<String>) evt.getNewValue();
			model.clear();
			try {Thread.sleep(100);}
			catch (InterruptedException e) {}
			for (String file : files) {
				try {Thread.sleep(10);}
				catch (InterruptedException e) {}
				model.addElement(file);
			}
			//fileList.setModel(model);
		}
		else if(prop.equals("fileField")){
			if(!fileField.getText().equals(evt.getNewValue().toString())){
				fileField.setText(evt.getNewValue().toString());				
			}
		}
		repaint();
	}
}
