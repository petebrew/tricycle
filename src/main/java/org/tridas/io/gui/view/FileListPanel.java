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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.config.ConfigEvent;
import org.tridas.io.gui.control.fileList.AddFileEvent;
import org.tridas.io.gui.control.fileList.BrowseEvent;
import org.tridas.io.gui.control.fileList.FileListController;
import org.tridas.io.gui.control.fileList.RemoveSelectedEvent;
import org.tridas.io.gui.enums.InputFormat;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;

import com.dmurph.mvc.MVCEvent;
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
	private DropTarget dt;
	
	private final FileListModel model;
	
	public FileListPanel(FileListModel argModel) {
		model = argModel;
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
				ConfigEvent event = new ConfigEvent(ConfigController.SET_INPUT_FORMAT, input);
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
		
		
		// Handle files being dropped onto the file list 
		DropTargetListener dropListener = new DropTargetListener() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				setDropGui(false);
			    try {
			        // Ok, get the dropped object and try to figure out what it is
			        Transferable tr = dtde.getTransferable();
			        DataFlavor[] flavors = tr.getTransferDataFlavors();
			        for (int i = 0; i < flavors.length; i++) 
			        {
			        	
				        log.debug("Possible flavor: " + flavors[i].getMimeType());
					    // Check for file lists specifically
					    if (flavors[i].isFlavorJavaFileListType()) 
					    {
					      // Great!  Accept copy drops...
					      dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					      log.debug("Successful file list drop.\n\n");
					      
					      // And add the list of file names to our text area
					      List list = (List)tr.getTransferData(flavors[i]);
					      for (int j = 0; j < list.size(); j++) {
					    	log.warn(list.get(j) + "\n");
					    	try
					    	{
					    		File dragFile = (File) list.get(j);
					    		if(dragFile.isFile())
					    		{
					    			AddFileEvent event = new AddFileEvent(dragFile.getAbsolutePath());
					    			event.dispatch();
					    		}
					    	} catch (Exception e)
					    	{
					    		log.warn("Drag and drop failed");
					    		return;
					    	}
					      }
		
					      // If we made it this far, everything worked.
					      dtde.dropComplete(true);
					      return;
					    }
			    
			        }
			        
		        // Hmm, the user must not have dropped a file list
			    log.warn("Drop failed: " + dtde);
		        dtde.rejectDrop();
		      } catch (Exception e) {
		        e.printStackTrace();
		        dtde.rejectDrop();
		      }
			}
			
			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) { }
			
			@Override
			public void dragOver(DropTargetDragEvent dtde) { 
				
				Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		        setCursor(cursor);
		        Border border = BorderFactory.createLineBorder(Color.red);
		     
		        fileList.setBorder(border);

				
			}
			
			@Override
			public void dragExit(DropTargetEvent dte) {	
				setDropGui(false);
			}
			
			@Override
			public void dragEnter(DropTargetDragEvent dtde) { 
				setDropGui(true);
			}
		};
		
		dt = new DropTarget(fileList, dropListener);
		
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
	
	/**
	 * Change the GUI to show drag and drop of files.  If dropping is false
	 * then the GUI is reset. 
	 * 
	 * @param dropping
	 */
	private void setDropGui(Boolean dropping)
	{
		Cursor cursor;
		if(dropping)
		{
			cursor = new Cursor(Cursor.HAND_CURSOR);
	        fileList.setBorder(BorderFactory.createLineBorder(Color.red));
		}
		else
		{
			cursor = new Cursor(Cursor.DEFAULT_CURSOR);
	        fileList.setBorder(null);
		}
		
		setCursor(cursor);
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
		
		TricycleModel mwm = TricycleModelLocator.getInstance().getTricycleModel();
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
