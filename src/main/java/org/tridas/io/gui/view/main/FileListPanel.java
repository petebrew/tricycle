/**
 * Created on May 25, 2010, 8:19:22 PM
 */
package org.tridas.io.gui.view.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * @author Daniel
 *
 */
public class FileListPanel extends JPanel {

    private JPanel topPanel;
    private JLabel selectLabel;
    private JList fileList;
    private JComboBox inputFormat;
    private JButton addButton;
    private JButton selectAllButton;
    private JButton selectNoneButton;
    private JButton removeSelectedButton;
    private JScrollPane scrollPane;
    private JPanel bottomPanel;

	public FileListPanel(){
		initComponents();
		populateLocale();
	}
	
	public void initComponents(){
        topPanel = new JPanel();
        addButton = new JButton();
        inputFormat = new JComboBox();
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

        topPanel.add(inputFormat);

        add(topPanel, java.awt.BorderLayout.PAGE_START);
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
	
	public void populateLocale(){
    	addButton.setText("Add...");
    	selectAllButton.setText("All");
    	selectNoneButton.setText("None");
    	removeSelectedButton.setText("Remove Selected");
    	selectLabel.setText("Select:");
	}
}
