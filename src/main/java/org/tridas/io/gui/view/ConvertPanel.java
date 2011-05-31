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
 * Created on May 25, 2010, 8:38:48 PM
 */
package org.tridas.io.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.command.ConvertCommand.DendroWrapper;
import org.tridas.io.gui.components.CustomTreeCellRenderer;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.config.ConfigEvent;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.control.convert.ConvertEvent;
import org.tridas.io.gui.control.convert.SaveEvent;
import org.tridas.io.gui.enums.OutputFormat;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.util.IOUtils;

import com.dmurph.mvc.ObjectEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class ConvertPanel extends JPanel {
	
	private static final String iconSize = "16x16";
	private JScrollPane scrollPane;
	private JTree convertedTree;
	private JButton saveButton;
	private JButton convertButton;
	private JButton expandAll;
	private JButton collapseAll;
	private JLabel results;
	private JComboBox outputFormat;
	private JButton previewButton;

	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Convertion Data");
	
	private final ConvertModel model;
	
	public ConvertPanel(ConvertModel argModel) {
		model = argModel;
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
	}
	
	private void initializeComponents() {
		convertButton = new JButton();
		saveButton = new JButton();
		scrollPane = new JScrollPane();
		convertedTree = new JTree();
		results = new JLabel();
		expandAll = new JButton();
		collapseAll = new JButton();
		outputFormat = new JComboBox();
		previewButton = new JButton();
		
		setLayout(new java.awt.BorderLayout());
		
		Box top = Box.createHorizontalBox();
		top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		top.add(outputFormat);
		top.add(convertButton);
		top.add(previewButton);
		top.add(saveButton);
		top.add(Box.createHorizontalGlue());

		collapseAll.putClientProperty( "JButton.buttonType", "segmentedTextured" );
		collapseAll.putClientProperty( "JButton.segmentPosition", "first" );
		expandAll.putClientProperty( "JButton.buttonType", "segmentedTextured" );
		expandAll.putClientProperty( "JButton.segmentPosition", "last" );
		
		Box bottom = Box.createHorizontalBox();
		bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		bottom.add(results);
		bottom.add(Box.createHorizontalGlue());
		bottom.add(collapseAll);
		bottom.add(expandAll);
		
		add(top, BorderLayout.NORTH);
		
		add(bottom, java.awt.BorderLayout.PAGE_END);
		
		ImageIcon ficon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/fail.png"));
		ImageIcon wicon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/warning.png"));
		ImageIcon sicon     = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/success.png"));
		ImageIcon infoicon  = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/info.png"));
		ImageIcon filewicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/filewarning.png"));
		ImageIcon filesicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/filesuccess.png"));

		CustomTreeCellRenderer renderer = new CustomTreeCellRenderer(sicon, wicon, ficon, filesicon, filewicon, infoicon);
		
		DefaultTreeModel model = new DefaultTreeModel(rootNode, false);
        ToolTipManager.sharedInstance().registerComponent(convertedTree);
		convertedTree.setCellRenderer(renderer);
		convertedTree.setModel(model);
		convertedTree.setRootVisible(false);
		convertedTree.setShowsRootHandles(true);
		convertedTree.expandRow(0);
		scrollPane.setViewportView(convertedTree);
		
		add(scrollPane, java.awt.BorderLayout.CENTER);
	}
	
	private void addListeners() {
		
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigModel config = TricycleModelLocator.getInstance().getConfigModel();
				FileListModel flmodel = TricycleModelLocator.getInstance().getFileListModel();
				
				ConvertEvent event = new ConvertEvent(flmodel.getInputFormat(),
													  model.getOutputFormat(),
													  config.getNamingConvention(),
													  config.getReaderDefaults(),
													  config.getWriterDefaults());
				event.dispatch();
			}
		});

		outputFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String output = outputFormat.getSelectedItem().toString();
				ConfigEvent event = new ConfigEvent(ConfigController.SET_OUTPUT_FORMAT, output);
				event.dispatch();
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveEvent event = new SaveEvent();
				event.dispatch();
			}
		});
		
		expandAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// no need to go to controller
				expandAll();
			}
		});
		
		collapseAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// no need to go to controller
				collapseAll();
			}
		});
		
		convertedTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selRow = convertedTree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = convertedTree.getPathForLocation(e.getX(), e.getY());
				if (selRow > 0) {
					if (e.getClickCount() == 2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
						if(node.getUserObject() instanceof DendroWrapper){ // means it's a file node
							ObjectEvent<DefaultMutableTreeNode> event = new ObjectEvent<DefaultMutableTreeNode>(
									ConvertController.PREVIEW, node);
							event.dispatch();
						}
					}
				}
			}
		});
		
		convertedTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent argE) {
				
				// BAD PRACTICE this should be an event to the controller
				if(argE != null && argE.getNewLeadSelectionPath() != null && argE.getNewLeadSelectionPath().getLastPathComponent() != null){
					model.setSelectedNode((DefaultMutableTreeNode) argE.getNewLeadSelectionPath().getLastPathComponent());
				}else{
					model.setSelectedNode(null);
				}
			}
		});
		
		previewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argE) {
				if(model.getSelectedNode() == null){
					return;
				}
				ObjectEvent<DefaultMutableTreeNode> event = new ObjectEvent<DefaultMutableTreeNode>(
						ConvertController.PREVIEW, model.getSelectedNode());
				event.dispatch();
			}
		});
	}
	
	private void populateLocale() {
		saveButton.setText(I18n.getText("view.convert.saveAll"));
		convertButton.setText(I18n.getText("view.convert.convert"));
		collapseAll.setText(I18n.getText("view.convert.collapse"));
		expandAll.setText(I18n.getText("view.convert.expand"));
		previewButton.setText(I18n.getText("view.convert.previewFile"));
		
		for (String out : OutputFormat.getOutputFormats()) {
			outputFormat.addItem(out);
		}
	}
	
	private void linkModel() {
		setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
		DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
		for (DefaultMutableTreeNode node : model.getNodes()) {
			treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
		}
		expandToFiles();
		
		// Try to remember last used format
		if(TricycleModelLocator.getInstance().getLastUsedOutputFormat()!=null)
		{
			outputFormat.setSelectedItem(TricycleModelLocator.getInstance().getLastUsedOutputFormat());
			model.setOutputFormat(TricycleModelLocator.getInstance().getLastUsedOutputFormat());
		}
		else
		{
			outputFormat.setSelectedItem(model.getOutputFormat());
		}
		
		if(model.getConvertedList().isEmpty()){
			saveButton.setEnabled(false); // for issue 233
		}
		
		if(model.getSelectedNode() == null){
			previewButton.setEnabled(false);
		}
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if (prop.equals("nodes")) {
					DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
					rootNode.removeAllChildren();
					for (DefaultMutableTreeNode node : model.getNodes()) {
						rootNode.add(node);
					}
					treeModel.setRoot(rootNode);
					expandToFiles();
				}
				else if (prop.equals("processed")) {
					setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
				}
				else if (prop.equals("failed")) {
					setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
				}
				else if (prop.equals("convWithWarnings")) {
					setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
				}else if (prop.equals("outputFormat")) {
					outputFormat.setSelectedItem(evt.getNewValue());
				}else if(prop.equals("selectedNode")){
					DefaultMutableTreeNode node = model.getSelectedNode();
					if(node == null){
						previewButton.setEnabled(false);
					}
					else if(node.getUserObject() instanceof DendroWrapper){ // means it's a file
						previewButton.setEnabled(true);
					}else{
						previewButton.setEnabled(false);
					}
				}
			}
		});
		
		TricycleModel mwm = TricycleModelLocator.getInstance().getTricycleModel();
		mwm.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				
				if (evt.getPropertyName().equals("lock")) {
					boolean lock = (Boolean) evt.getNewValue();
					if (lock) {
						convertButton.setEnabled(false);
						saveButton.setEnabled(false);
					}
					else {
						convertButton.setEnabled(true);
						if(!model.getConvertedList().isEmpty()){
							saveButton.setEnabled(true); // for issue 233
						}else{
							saveButton.setEnabled(false);
						}
					}
				}
			}
		});
	}
	
	private void expandToFiles() {
		int row = 0;
		while (row < convertedTree.getRowCount()) {
			if (convertedTree.getPathForRow(row).getPathCount() < 3) {
				convertedTree.expandRow(row);
			}
			row++;
		}
	}
	
	private void setStatus(int argProcessed, int argFailed, int argConvWithWarnings) {
		results.setText(I18n.getText("view.convert.status", argProcessed+"", argFailed+"", argConvWithWarnings+""));
	}
	
	private void expandAll() {
		for (int i = 0; i < convertedTree.getRowCount(); i++) {
			convertedTree.expandRow(i);
		}
	}
	
	private void collapseAll() {
		for (int i = convertedTree.getRowCount() - 1; i >= 0; i--) {
			convertedTree.collapseRow(i);
		}
	}
}
