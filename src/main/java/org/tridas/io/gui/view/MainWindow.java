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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.TricycleController;
import org.tridas.io.gui.control.fileList.BrowseEvent;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.util.IOUtils;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.MVCArrayList;

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
	private JMenuItem fileOpenButton;
	private JMenuItem aboutMenuButton;
	private JMenuItem helpMenuButton;

	private JMenuItem logMenuButton;
	private JTabbedPane tabbedPane;
	public FileListPanel fileList;
	public ConvertPanel convertPanel;
	
	public final TricycleModel model;
	
	/** Creates new form NewJFrame */
	public MainWindow(TricycleModel argModel) {
		model = argModel;
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
		helpMenuButton = new JMenuItem();

		fileOpenButton = new JMenuItem();
		
		optionsMenuButton = new JMenuItem();
		logMenuButton = new JMenuItem();
		

		fileList = new FileListPanel(TricycleModelLocator.getInstance().getFileListModel());
		convertPanel = new ConvertPanel(TricycleModelLocator.getInstance().getConvertModel());

		
		tabbedPane.addTab(I18n.getText("view.main.fileListTab"), fileList);
		tabbedPane.addTab(I18n.getText("view.main.convertTab"), convertPanel);
		// set selected component to the file list
		tabbedPane.setSelectedComponent(fileList);
		
		add(tabbedPane, java.awt.BorderLayout.CENTER);
		
		fileOpenButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		fileMenu.add(fileOpenButton);
		fileMenu.add(optionsMenuButton);
		
		quitMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		fileMenu.addSeparator();
		fileMenu.add(quitMenuButton);
		
		menuBar.add(fileMenu);
		
		
		helpMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

		helpMenu.add(logMenuButton);
		helpMenu.add(aboutMenuButton);
		helpMenu.addSeparator();
		helpMenu.add(helpMenuButton);
		
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}
	
	private void populateLocale() {
		setIconImage(TricycleModelLocator.getInstance().getWindowIcon().getImage());
		fileOpenButton.setText(I18n.getText("view.files.open"));
		optionsMenuButton.setText(I18n.getText("view.files.options"));
		quitMenuButton.setText(I18n.getText("view.main.quit"));
		aboutMenuButton.setText(I18n.getText("view.main.about"));
		helpMenuButton.setText("Help");

		logMenuButton.setText(I18n.getText("view.main.log"));
		fileMenu.setText(I18n.getText("view.main.file"));
		helpMenu.setText(I18n.getText("view.main.help"));
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		fileOpenButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent argE) {
				BrowseEvent event = new BrowseEvent();
				event.dispatch();
			}
		});
		
		optionsMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				MVCEvent event = new MVCEvent(TricycleController.OPTIONS);
				event.dispatch();
			}
		});
		
		quitMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(TricycleController.QUIT);
				event.dispatch();
			}
		});
		aboutMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(TricycleController.ABOUT);
				event.dispatch();
			}
		});
		

		HelpSet hs;
		try {
		    hs = new HelpSet(null, IOUtils.getFileInJarURL("manual/jhelpset.hs"));
		} catch (Exception ee) {
		    System.out.println ("HelpSet not found");
		    return;
		}
		HelpBroker hb = hs.createHelpBroker();
		
		helpMenuButton.addActionListener(new CSH.DisplayHelpFromSource(hb));
	
		logMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				MVCEvent event = new MVCEvent(TricycleController.VIEW_LOG);
				event.dispatch();
			}
		});
	}
	
	private void linkModel() {
		final FileListModel model = TricycleModelLocator.getInstance().getFileListModel();
		if(model.getInputFiles().size() == 0){
			tabbedPane.setEnabledAt(1, false);
		}else{
			tabbedPane.setEnabledAt(1, true);
		}
		
		model.getInputFiles().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals(MVCArrayList.REMOVED)){
					if(model.getInputFiles().size() == 0){
						tabbedPane.setEnabledAt(1, false);
					}
				}else if(argEvt.getPropertyName().equals(MVCArrayList.ADDED)){
					tabbedPane.setEnabledAt(1, true);
				}
			}
		});
	}
}
