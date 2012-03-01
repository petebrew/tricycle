/**
 * Copyright 2010 Daniel Murphy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Created on May 25, 2010, 8:19:22 PM
 */
package org.tridas.io.gui.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.enums.InputFormat;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.config.ConfigEvent;
import org.tridas.io.gui.control.fileList.AddFileEvent;
import org.tridas.io.gui.control.fileList.AddMultipleFilesEvent;
import org.tridas.io.gui.control.fileList.BrowseEvent;
import org.tridas.io.gui.control.fileList.FileListController;
import org.tridas.io.gui.control.fileList.RemoveSelectedEvent;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.util.FileDrop;
import org.tridas.io.util.IOUtils;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class FileListPanel extends JPanel {
	private static final Logger log = LoggerFactory
			.getLogger(FileListPanel.class);
	private static final String iconSize = "16x16";
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

	private final FileListModel model;
	private JComboBox cboTreatAs;
	private JLabel lblFormat;
	private JLabel lblFiles;
	private JLabel lblTreatFilesAs;
	private JPanel selectPanel;
	private JPanel panel;

	public FileListPanel(FileListModel argModel) {
		model = argModel;
		initComponents();
		populateLocale();
		linkModel();
		addListeners();
	}

	public void initComponents() {
		setLayout(new MigLayout("", "[85.00,right][0:0:0][264.00px,grow][][]",
				"[12.00px][][230px,grow,fill][]"));

		lblFormat = new JLabel(I18n.getText("view.options.input.format"));
		add(lblFormat, "cell 0 0");
		inputFormat = new JComboBox();
		add(inputFormat, "cell 2 0 3 1,alignx left");

		lblFiles = new JLabel(I18n.getText("view.main.file") + ":");
		add(lblFiles, "cell 0 1,aligny bottom");
		
		ImageIcon saIcon = null;
		ImageIcon snIcon = null;
		ImageIcon rsIcon = null;
		ImageIcon raIcon = null;
		
		try{
		 saIcon = new ImageIcon(IOUtils.getFileInJarURL("icons/"
				+ iconSize + "/selectall.png"));
		 snIcon = new ImageIcon(IOUtils.getFileInJarURL("icons/"
				+ iconSize + "/selectnone.png"));
		 rsIcon = new ImageIcon(IOUtils.getFileInJarURL("icons/"
				+ iconSize + "/delete.png"));
		//ImageIcon isIcon = new ImageIcon(IOUtils.getFileInJarURL("icons/"
		//		+ iconSize + "/selectinvert.png"));
		 raIcon = new ImageIcon(IOUtils.getFileInJarURL("icons/"
				+ iconSize + "/delete2.png"));
		} catch (NullPointerException e)
		{
			log.error("Failed to find icon for icon size " +iconSize);
			e.printStackTrace();
		}

		fileField = new JTextField();
		add(fileField, "cell 2 1,growx,aligny bottom");
		browseButton = new JButton();
		add(browseButton, "cell 3 1");
		addButton = new JButton();
		add(addButton, "cell 4 1");

		panel = new JPanel();
		add(panel, "cell 1 2 4 1,grow");
		panel.setLayout(new MigLayout("", "[grow][]", "[grow,fill]"));
		fileList = new JList();
		scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 0,growx");

		fileList.setModel(new DefaultListModel());
		scrollPane.setViewportView(fileList);

		selectPanel = new JPanel();
		panel.add(selectPanel, "cell 1 0");
		selectPanel.setLayout(new MigLayout("", "[22px,left]",
				"[22px][][][grow][]"));

		selectAllButton = new JButton();
		selectPanel.add(selectAllButton, "cell 0 0,alignx left,aligny center");
		selectAllButton.setIcon(saIcon);
		selectAllButton.setPreferredSize(new Dimension(25, 25));
		selectAllButton.setMaximumSize(new Dimension(25, 25));
		selectAllButton.putClientProperty("JButton.buttonType",
				"segmentedTextured");
		selectAllButton.putClientProperty("JButton.segmentPosition", "middle");

		selectNoneButton = new JButton();
		selectPanel.add(selectNoneButton, "cell 0 1,alignx left,aligny center");
		selectNoneButton.setIcon(snIcon);
		selectNoneButton.setPreferredSize(new Dimension(25, 25));
		selectNoneButton.setMaximumSize(new Dimension(25, 25));
		selectNoneButton.putClientProperty("JButton.buttonType",
				"segmentedTextured");
		selectNoneButton.putClientProperty("JButton.segmentPosition", "last");

		removeSelectedButton = new JButton();
		selectPanel.add(removeSelectedButton,
				"cell 0 2,alignx left,aligny center");
		removeSelectedButton.setIcon(rsIcon);
		removeSelectedButton.setPreferredSize(new Dimension(25, 25));
		removeSelectedButton.setMaximumSize(new Dimension(25, 25));
		removeSelectedButton.putClientProperty("JButton.buttonType",
				"segmentedTextured");
		removeSelectedButton.putClientProperty("JButton.segmentPosition",
				"last");

		removeAll = new JButton();
		selectPanel.add(removeAll, "cell 0 4");
		removeAll.setIcon(raIcon);
		removeAll.setPreferredSize(new Dimension(25, 25));
		removeAll.setMaximumSize(new Dimension(25, 25));
		removeAll.putClientProperty("JButton.buttonType", "segmentedTextured");
		removeAll.putClientProperty("JButton.segmentPosition", "last");

		lblTreatFilesAs = new JLabel(I18n.getText("view.files.treatas"));
		add(lblTreatFilesAs, "cell 0 3,alignx trailing");

		cboTreatAs = new JComboBox();
		add(cboTreatAs, "cell 2 3 3 1,alignx left");
		cboTreatAs.setModel(new DefaultComboBoxModel(new String[] {
				I18n.getText("view.files.treatas.separate"),
				I18n.getText("view.files.treatas.oneproject"),
				I18n.getText("view.files.treatas.oneobject") }));
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
				ConfigEvent event = new ConfigEvent(
						ConfigController.SET_INPUT_FORMAT, input);
				event.dispatch();
			}
		});

		cboTreatAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String treatAs = cboTreatAs.getSelectedItem().toString();
				ConfigEvent event = new ConfigEvent(
						ConfigController.SET_TREAT_FILES_AS, treatAs);
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
			public void changedUpdate(DocumentEvent e) {
			}
		});

		ActionListener addFileListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddFileEvent event = new AddFileEvent(fileField.getText());
				event.dispatch();
			}
		};

		// Handle files being dropped onto the file list
		new FileDrop(fileList, new FileDrop.Listener() {
			public void filesDropped(java.io.File[] files) {
				AddMultipleFilesEvent event = new AddMultipleFilesEvent(files);
				event.dispatch();

			} // end filesDropped
		}); // end FileDrop.Listener

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
				MVCEvent event = new MVCEvent(FileListController.REMOVE_ALL);
				event.dispatch();
			}
		});
	}

	public void populateLocale() {
		// ImageIcon snIcon = new
		// ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/selectnone.png"));

		browseButton.setText(I18n.getText("view.files.browse"));
		addButton.setText(I18n.getText("view.files.add"));

		selectAllButton.setToolTipText(I18n.getText("view.files.selectAll"));
		selectNoneButton.setToolTipText(I18n.getText("view.files.selectNone"));
		this.removeAll.setToolTipText(I18n.getText("view.files.removeAll"));
		removeSelectedButton.setToolTipText(I18n
				.getText("view.files.removeSelected"));

		for (String s : InputFormat.getInputFormats()) {
			inputFormat.addItem(s);
		}
	}

	public void linkModel() {

		// Try to remember last used format
		if (TricycleModelLocator.getInstance().getLastUsedInputFormat() != null) {
			inputFormat.setSelectedItem(TricycleModelLocator.getInstance()
					.getLastUsedInputFormat());
			model.setInputFormat(TricycleModelLocator.getInstance()
					.getLastUsedInputFormat());
		} else {
			inputFormat.setSelectedItem(model.getInputFormat());
		}

		// first set all values from model
		DefaultListModel listModel = (DefaultListModel) fileList.getModel();
		ArrayList<String> files = model.getInputFiles();
		listModel.clear();

		// swing is doing weird stuff, so we space it out
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		for (String file : files) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			listModel.addElement(file);
		}
		fileField.setText(model.getFileField());

		// then listen for changes
		model.addPropertyChangeListener(new PropertyChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				log.debug("Property change event received: " + prop,
						evt.getNewValue());

				if (prop.equals("inputFiles")) {
					DefaultListModel model = (DefaultListModel) fileList
							.getModel();
					ArrayList<String> files = (ArrayList<String>) evt
							.getNewValue();
					model.clear();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					for (String file : files) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
						model.addElement(file);
					}
					// fileList.setModel(model);
				} else if (prop.equals("fileField")) {
					if (!fileField.getText().equals(
							evt.getNewValue().toString())) {
						fileField.setText(evt.getNewValue().toString());
					}
				} else if (prop.equals("inputFormat")) {
					inputFormat.setSelectedItem(evt.getNewValue());
				}
				repaint();
			}
		});

		TricycleModel mwm = TricycleModelLocator.getInstance()
				.getTricycleModel();
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
					} else {
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
