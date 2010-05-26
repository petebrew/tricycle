/**
 * Created on May 25, 2010, 8:19:22 PM
 */
package org.tridas.io.gui.view.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.SwingConstants;

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.control.main.fileList.FileListController;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.model.fileList.FileListModel;
import org.tridas.io.gui.mvc.control.events.ObjectEvent;

/**
 * @author Daniel
 */
public class FileListPanel extends JPanel implements PropertyChangeListener {
	private static final SimpleLogger log = new SimpleLogger(FileListPanel.class);
	
	private static final String INPUT_FORMAT_LABEL = "Input Format...";

	private JPanel topPanel;
	private JLabel selectLabel;
	private JLabel inputFormatLabel;
	private JList fileList;
	private JComboBox inputFormat;
	private JButton addButton;
	private JButton selectAllButton;
	private JButton selectNoneButton;
	private JButton removeSelectedButton;
	private JScrollPane scrollPane;
	private JPanel bottomPanel;

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
		fileList = new JList();
		removeSelectedButton = new JButton();
		selectLabel = new JLabel();
		selectAllButton = new JButton();
		selectNoneButton = new JButton();
		scrollPane = new JScrollPane();
		bottomPanel = new JPanel();

		setLayout(new BorderLayout());

		topPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

		topPanel.add(addButton);

		topPanel.add(inputFormatLabel);

		inputFormat.setEditable(true);
		topPanel.add(inputFormat);

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
	
	/**
	 * 
	 */
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
				HashSet<String> list = new HashSet<String>();
				for(Object o :fileList.getSelectedValues()){
					list.add(o.toString());
				}
				ObjectEvent<Set<String>> event = new ObjectEvent<Set<String>>(FileListController.REMOVE_SELECTED, list);
				//event.dispatch();
			}
		});
	}

	public void populateLocale() {
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
		String prop = evt.getPropertyName().toLowerCase();
		log.dbo(DebugLevel.L5_DEBUG, prop, evt.getNewValue());
		
		if (prop.equals("inputformat")) {
			inputFormat.setSelectedItem(evt.getNewValue());
		}
		else if (prop.equals("inputfiles")) {
			DefaultListModel model = (DefaultListModel) fileList.getModel();
			ArrayList<String> files = (ArrayList<String>) evt.getNewValue();
			model.clear();
			for (String file : files) {
				model.addElement(file);
			}
		}
	}
}
