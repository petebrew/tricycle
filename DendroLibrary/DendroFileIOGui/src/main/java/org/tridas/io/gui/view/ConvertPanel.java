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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.tridas.io.gui.control.convert.ConvertEvent;
import org.tridas.io.gui.control.convert.SaveEvent;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.MainWindowModel;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class ConvertPanel extends JPanel {
	
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JScrollPane scrollPane;
	private JTree convertedTree;
	private JButton saveButton;
	private JButton convertButton;
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Convertion Data");
	
	private ConvertModel model = ConvertModel.getInstance();
	
	public ConvertPanel() {
		initializeComponents();
		populateLocale();
		linkModel();
		addListeners();
	}
	
	private void initializeComponents() {
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		convertButton = new JButton();
		saveButton = new JButton();
		scrollPane = new JScrollPane();
		convertedTree = new JTree();
		
		setLayout(new java.awt.BorderLayout());
		
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		
		topPanel.add(convertButton);
		bottomPanel.add(saveButton);
		
		add(topPanel, BorderLayout.NORTH);
		
		add(bottomPanel, java.awt.BorderLayout.PAGE_END);
		
		DefaultTreeModel model = new DefaultTreeModel(rootNode, false);
		convertedTree.setModel(model);
		convertedTree.setRootVisible(true);
		convertedTree.expandRow(0);
		scrollPane.setViewportView(convertedTree);
		
		add(scrollPane, java.awt.BorderLayout.CENTER);
	}
	
	private void addListeners() {
		
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigModel config = ConfigModel.getInstance();
				ConvertEvent event = new ConvertEvent(config.getOutputFormat(), config.getNamingConvention());
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
		
	}
	
	private void linkModel() {
		
		DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
		for (DefaultMutableTreeNode node : model.getNodes()) {
			treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
		}
		expandAll();
		
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
					expandAll();
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
						convertButton.setEnabled(false);
						saveButton.setEnabled(false);
					}
					else {
						convertButton.setEnabled(true);
						saveButton.setEnabled(true);
					}
				}
			}
		});
	}
	
	private void expandAll() {
		int row = 0;
		while (row < convertedTree.getRowCount()) {
			convertedTree.expandRow(row);
			row++;
		}
	}
}
