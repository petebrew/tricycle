/**
 * Created on May 25, 2010, 8:38:48 PM
 */
package org.tridas.io.gui.view.main;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 * @author Daniel
 */
public class ConvertPanel extends JPanel {

	private JPanel bottomPanel;

	private JScrollPane scrollPane;

	private JTree convertedTree;

	private JButton saveButton;

	private JButton convertButton;

	private JComboBox outputFormat;

	private JComboBox namingConvention;

	public ConvertPanel() {
		initializeComponents();
		populateLocale();
	}

	private void initializeComponents() {

		bottomPanel = new JPanel();
		namingConvention = new JComboBox();
		outputFormat = new JComboBox();
		convertButton = new JButton();
		saveButton = new JButton();
		scrollPane = new JScrollPane();
		convertedTree = new JTree();

		setLayout(new java.awt.BorderLayout());

		bottomPanel.setLayout(new FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

		bottomPanel.add(namingConvention);
		bottomPanel.add(outputFormat);
		bottomPanel.add(convertButton);
		bottomPanel.add(saveButton);

		add(bottomPanel, java.awt.BorderLayout.PAGE_END);

		scrollPane.setViewportView(convertedTree);

		add(scrollPane, java.awt.BorderLayout.CENTER);
	}

	private void populateLocale() {
		saveButton.setText("Save...");
		convertButton.setText("Convert");
	}
}
