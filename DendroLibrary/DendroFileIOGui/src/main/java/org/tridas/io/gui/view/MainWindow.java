/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame.java
 *
 * Created on May 23, 2010, 7:36:32 PM
 */

package org.tridas.io.gui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.tridas.io.gui.control.MainWindowController;
import org.tridas.io.gui.model.MainWindowModel;

import com.dmurph.mvc.MVCEvent;

/**
 * @author daniel
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuBar menuBar;
	private JMenuItem quitMenuButton;
	private JMenuItem aboutMenuButton;
	private JTabbedPane tabbedPane;
	private FileListPanel fileList;
	private ConvertPanel convertPanel;
	private ConfigPanel config;
	
	public final MainWindowModel model = MainWindowModel.getInstance();
	
	/** Creates new form NewJFrame */
	public MainWindow() {
		constructComponents();
		populateLocale();
		addListeners();
		linkModel();
		pack();
	}
	
	private void constructComponents() {
		tabbedPane = new JTabbedPane();
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		// loadMenuButton = new JMenuItem();
		// saveMenuButton = new JMenuItem();
		quitMenuButton = new JMenuItem();
		helpMenu = new JMenu();
		aboutMenuButton = new JMenuItem();
		
		fileList = new FileListPanel();
		convertPanel = new ConvertPanel();
		config = new ConfigPanel();
		
		tabbedPane.addTab("Config", config);
		tabbedPane.addTab("File List", fileList); // NOI18N
		tabbedPane.addTab("Convert", convertPanel); // NOI18N
		
		add(tabbedPane, java.awt.BorderLayout.CENTER);
		
		// loadMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L,
		// java.awt.event.InputEvent.CTRL_MASK));
		// fileMenu.add(loadMenuButton);
		
		// saveMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
		// java.awt.event.InputEvent.CTRL_MASK));
		// fileMenu.add(saveMenuButton);
		
		quitMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,
				java.awt.event.InputEvent.CTRL_MASK));
		fileMenu.add(quitMenuButton);
		
		menuBar.add(fileMenu);
		
		aboutMenuButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		helpMenu.add(aboutMenuButton);
		
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}
	
	private void populateLocale() {
		
		// loadMenuButton.setText("Load File");
		quitMenuButton.setText("Quit");
		aboutMenuButton.setText("About");
		// saveMenuButton.setText("Save");
		fileMenu.setText("File");
		helpMenu.setText("Help");
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		quitMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(MainWindowController.QUIT);
				event.dispatch();
			}
		});
		aboutMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(MainWindowController.ABOUT);
				event.dispatch();
			}
		});
	}
	
	private void linkModel() {
	// nothing
	}
}
