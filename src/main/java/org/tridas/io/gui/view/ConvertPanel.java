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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.components.CustomTreeCellRenderer;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.control.convert.ConvertEvent;
import org.tridas.io.gui.control.convert.SaveEvent;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.MainWindowModel;
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
	private JButton reset;
	private JLabel results;
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Convertion Data");
	
	private ConvertModel model = ConvertModel.getInstance();
	
	public ConvertPanel() {
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
		reset = new JButton();
		
		setLayout(new java.awt.BorderLayout());
		
		Box top = Box.createHorizontalBox();
		top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		top.add(convertButton);
		top.add(saveButton);
		top.add(Box.createHorizontalGlue());

		
		Box bottom = Box.createHorizontalBox();
		bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		bottom.add(results);
		bottom.add(Box.createHorizontalGlue());
		bottom.add(collapseAll);
		bottom.add(expandAll);
		bottom.add(reset);
		
		add(top, BorderLayout.NORTH);
		
		add(bottom, java.awt.BorderLayout.PAGE_END);
		
		ImageIcon ficon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/fail.png"));
		ImageIcon wicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/warning.png"));
		ImageIcon sicon = new ImageIcon(IOUtils.getFileInJarURL("icons/"+iconSize+"/success.png"));

		CustomTreeCellRenderer renderer = new CustomTreeCellRenderer(sicon, wicon, ficon);
		
		DefaultTreeModel model = new DefaultTreeModel(rootNode, false);
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
		
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// no need to go to controller
				collapseAll();
				expandToFiles();
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
						ObjectEvent<DefaultMutableTreeNode> event = new ObjectEvent<DefaultMutableTreeNode>(
								ConvertController.PREVIEW, node);
						event.dispatch();
					}
				}
			}
		});
	}
	
	private void populateLocale() {
		saveButton.setText(I18n.getText("view.convert.save"));
		convertButton.setText(I18n.getText("view.convert.convert"));
		collapseAll.setText(I18n.getText("view.convert.collapse"));
		expandAll.setText(I18n.getText("view.convert.expand"));
		reset.setText(I18n.getText("view.convert.reset"));
	}
	
	private void linkModel() {
		setStatus(model.getProcessed(), model.getFailed(), model.getConvWithWarnings());
		DefaultTreeModel treeModel = (DefaultTreeModel) convertedTree.getModel();
		for (DefaultMutableTreeNode node : model.getNodes()) {
			treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
		}
		expandToFiles();
		
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
