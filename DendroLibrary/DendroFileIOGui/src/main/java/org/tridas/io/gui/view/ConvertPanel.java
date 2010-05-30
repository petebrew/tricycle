/**
 * Created on May 25, 2010, 8:38:48 PM
 */
package org.tridas.io.gui.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.tridas.io.gui.control.main.convert.ConvertEvent;
import org.tridas.io.gui.control.main.convert.SaveEvent;
import org.tridas.io.gui.enums.NamingConvention;
import org.tridas.io.gui.enums.OutputFormat;
import org.tridas.io.gui.model.ConvertModel;

/**
 * @author Daniel
 */
public class ConvertPanel extends JPanel {

	private JPanel topPanel;
	private JPanel bottomPanel;
	private JScrollPane scrollPane;
	private JTree convertedTree;
	private JButton saveButton;
	private JButton convertButton;
	private JComboBox outputFormat;
	private JComboBox namingConvention;
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Convertion Data");

	private ConvertModel model = ConvertModel.getInstance();
	
	public ConvertPanel() {
		initializeComponents();
		addListeners();
		populateLocale();
		linkModel();
	}

	private void initializeComponents() {
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		namingConvention = new JComboBox();
		outputFormat = new JComboBox();
		convertButton = new JButton();
		saveButton = new JButton();
		scrollPane = new JScrollPane();
		convertedTree = new JTree();

		setLayout(new java.awt.BorderLayout());
		
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

		namingConvention.setEditable(false);
		topPanel.add(namingConvention);
		
		topPanel.add(outputFormat);
		topPanel.add(convertButton);
		bottomPanel.add(saveButton);

		add(topPanel, BorderLayout.NORTH);
		
		add(bottomPanel, java.awt.BorderLayout.PAGE_END);

		DefaultTreeModel model = new DefaultTreeModel(rootNode,false);
		convertedTree.setModel(model);
		convertedTree.setRootVisible(true);
		convertedTree.expandRow(0);
		scrollPane.setViewportView(convertedTree);

		add(scrollPane, java.awt.BorderLayout.CENTER);
	}
	
	private void addListeners(){
		namingConvention.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// skip event to controller, implement that if needed
				model.setNamingConvention(namingConvention.getSelectedItem().toString());
			}
		});
		
		outputFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// skip event to controller, implement that if needed
				model.setOutputFormat(outputFormat.getSelectedItem().toString());
			}
		});
		
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConvertEvent event = new ConvertEvent(model.getOutputFormat(), model.getNamingConvention());
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
	}

	private void populateLocale() {
		saveButton.setText("Save...");
		convertButton.setText("Convert");
		
		for(String conv : NamingConvention.getNamingConventions()){
			namingConvention.addItem(conv);
		}
		
		outputFormat.addItem("TRiDaS");
		for(String out : OutputFormat.getOutputFormats()){
			if(out.equals("TRiDaS")){
				continue;
			}
			outputFormat.addItem(out);
		}
	}
	
	private void linkModel() {
		namingConvention.setSelectedItem(model.getNamingConvention());
		outputFormat.setSelectedItem(model.getOutputFormat());
		
		DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
		for(DefaultMutableTreeNode node : model.getNodes()){
			treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
		}
		expandAll();
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
				if(prop.equals("namingConvention")){
					namingConvention.setSelectedItem(evt.getNewValue());
				}else if(prop.equals("outputFormat")){
					outputFormat.setSelectedItem(evt.getNewValue());
				}else if(prop.equals("nodes")){
					DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
					rootNode.removeAllChildren();
					for(DefaultMutableTreeNode node : model.getNodes()){
						rootNode.add(node);
					}
					treeModel.setRoot(rootNode);
					expandAll();
				}
			}
		});
	}
	
	private void expandAll(){
		int row = 0;
	    while (row < convertedTree.getRowCount()) {
	    	convertedTree.expandRow(row);
	      row++;
	     }
	}
}
