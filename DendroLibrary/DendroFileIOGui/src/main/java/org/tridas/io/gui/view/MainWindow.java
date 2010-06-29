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

package org.tridas.io.gui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.MainWindowController;
import org.tridas.io.gui.model.MainWindowModel;
import org.tridas.io.gui.model.ModelLocator;

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
	private JMenuItem optionsMenuButton;
	private JMenuItem aboutMenuButton;
	private JMenuItem logMenuButton;
	private JTabbedPane tabbedPane;
	public FileListPanel fileList;
	public ConvertPanel convertPanel;
	
	public final MainWindowModel model = MainWindowModel.getInstance();
	
	/** Creates new form NewJFrame */
	public MainWindow() {
		constructComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		setLocationRelativeTo(null);
		setTitle("TRiCYCLE");
	}
	
	private void constructComponents() {
		tabbedPane = new JTabbedPane();
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		quitMenuButton = new JMenuItem();
		helpMenu = new JMenu();
		aboutMenuButton = new JMenuItem();
		optionsMenuButton = new JMenuItem();
		logMenuButton = new JMenuItem();
		
		fileList = new FileListPanel();
		convertPanel = new ConvertPanel();
		
		tabbedPane.addTab(I18n.getText("view.main.fileListTab"), fileList);
		tabbedPane.addTab(I18n.getText("view.main.convertTab"), convertPanel);
		// set selected component to the file list
		tabbedPane.setSelectedComponent(fileList);
		
		add(tabbedPane, java.awt.BorderLayout.CENTER);
		
		optionsMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		fileMenu.add(optionsMenuButton);
		
		quitMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		fileMenu.add(quitMenuButton);
		
		menuBar.add(fileMenu);
		
		aboutMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMenu.add(logMenuButton);
		helpMenu.add(aboutMenuButton);
		
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}
	
	private void populateLocale() {
		setIconImage(ModelLocator.getInstance().getWindowIcon().getImage());
		optionsMenuButton.setText("Options");
		quitMenuButton.setText(I18n.getText("view.main.quit"));
		aboutMenuButton.setText(I18n.getText("view.main.about"));
		logMenuButton.setText(I18n.getText("view.main.log"));
		fileMenu.setText(I18n.getText("view.main.file"));
		helpMenu.setText(I18n.getText("view.main.help"));
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		optionsMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				MVCEvent event = new MVCEvent(MainWindowController.OPTIONS);
				event.dispatch();
			}
		});
		
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
		logMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				MVCEvent event = new MVCEvent(MainWindowController.VIEW_LOG);
				event.dispatch();
			}
		});
	}
	
	private void linkModel() {
	// nothing
	}
}
