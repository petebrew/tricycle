/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame.java
 *
 * Created on May 23, 2010, 7:36:32 PM
 */

package org.tridas.io.gui.view.main;

import java.awt.Dimension;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.tridas.io.gui.model.main.MainWindowModel;

/**
 *
 * @author daniel
 */
public class MainView extends JFrame {

	public final MainWindowModel model = MainWindowModel.getInstance();
	
    /** Creates new form NewJFrame */
    public MainView() {
        constructComponents();
        populateLocale();
        linkModel();
        pack();
    }
    
    @SuppressWarnings("unchecked")
    private void constructComponents() {

        tabbedPane = new JTabbedPane();
        jPanel2 = new JPanel();
        jPanel4 = new JPanel();
        addButton = new JButton();
        inputFormat = new JComboBox();
        jScrollPane3 = new JScrollPane();
        fileList = new JList();
        jPanel3 = new JPanel();
        removeSelectedButton = new JButton();
        selectLabel = new JLabel();
        selectAllButton = new JButton();
        selectNoneButton = new JButton();
        jPanel5 = new JPanel();
        jPanel6 = new JPanel();
        namingConvention = new JComboBox();
        outputFormat = new JComboBox();
        convertButton = new JButton();
        saveButton = new JButton();
        jScrollPane1 = new JScrollPane();
        convertedTree = new JTree();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        loadMenuButton = new JMenuItem();
        saveMenuButton = new JMenuItem();
        quitMenuButton = new JMenuItem();
        helpMenu = new JMenu();
        aboutMenuButton = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jPanel4.add(addButton);

        jPanel4.add(inputFormat);

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);


        jScrollPane3.setViewportView(fileList);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));


        jPanel3.add(removeSelectedButton);

        selectLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel3.add(selectLabel);
        jPanel3.add(selectAllButton);
        jPanel3.add(selectNoneButton);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        tabbedPane.addTab("File List", jPanel2); // NOI18N

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jPanel6.add(namingConvention);
        jPanel6.add(outputFormat);
        jPanel6.add(convertButton);
        jPanel6.add(saveButton);

        jPanel5.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setViewportView(convertedTree);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Convert", jPanel5); // NOI18N

        add(tabbedPane, java.awt.BorderLayout.CENTER);
        
        loadMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        fileMenu.add(loadMenuButton);

        saveMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        fileMenu.add(saveMenuButton);

        quitMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        fileMenu.add(quitMenuButton);

        menuBar.add(fileMenu);

        aboutMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenu.add(aboutMenuButton);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }
    
    private void populateLocale(){
    	addButton.setText("Add...");
    	saveButton.setText("Save...");
    	convertButton.setText("Convert");
    	selectAllButton.setText("All");
    	selectNoneButton.setText("None");
    	removeSelectedButton.setText("Remove Selected");
    	selectLabel.setText("Select:");
    	loadMenuButton.setText("Load File");
    	quitMenuButton.setText("Quit");
    	aboutMenuButton.setText("About");
    	saveMenuButton.setText("Save");
    	fileMenu.setText("File");
    	helpMenu.setText("Help");
    }
    
    private void linkModel(){
    	
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
    	try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton addButton;
    private JButton saveButton;
    private JButton convertButton;
    private JButton selectAllButton;
    private JButton selectNoneButton;
    private JButton removeSelectedButton;
    private JComboBox inputFormat;
    private JComboBox outputFormat;
    private JComboBox namingConvention;
    private JLabel selectLabel;
    private JList fileList;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JMenuBar menuBar;
    private JMenuItem loadMenuButton;
    private JMenuItem quitMenuButton;
    private JMenuItem aboutMenuButton;
    private JMenuItem saveMenuButton;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane3;
    private JTabbedPane tabbedPane;
    private JTree convertedTree;
    // End of variables declaration//GEN-END:variables

}
