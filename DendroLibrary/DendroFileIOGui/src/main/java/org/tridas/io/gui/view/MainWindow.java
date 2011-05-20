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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.CheckForUpdateEvent;
import org.tridas.io.gui.control.TricycleController;
import org.tridas.io.gui.control.fileList.BrowseEvent;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;

import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.MVCArrayList;
import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

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
	//private JMenuItem helpMenuButton;
	private JMenuItem checkForUpdatesButton;
	private JMenuItem guessFileFormatButton;
	
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
		//helpMenuButton = new JMenuItem();
		checkForUpdatesButton = new JMenuItem();
		guessFileFormatButton = new JMenuItem();
		
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
		
		
		fileOpenButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, getAccelerator()));
		fileMenu.add(fileOpenButton);
		fileMenu.add(optionsMenuButton);
		
		quitMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, getAccelerator()));
		
		if(!System.getProperty("os.name").startsWith("Mac")){
			fileMenu.addSeparator();
			fileMenu.add(quitMenuButton);
		}
		
		menuBar.add(fileMenu);
		
		
		//helpMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

		helpMenu.add(logMenuButton);
		helpMenu.add(checkForUpdatesButton);
		helpMenu.add(guessFileFormatButton);
		//helpMenu.add(helpMenuButton);
		if(!System.getProperty("os.name").startsWith("Mac")){
			helpMenu.addSeparator();
			helpMenu.add(aboutMenuButton);
		}
		
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}
	
	public static int getAccelerator()
	{
		if(!System.getProperty("os.name").startsWith("Mac")){
			return InputEvent.CTRL_MASK;
		}
		else
		{
			return InputEvent.META_MASK;
		}
	}
	
	private void populateLocale() {
		setIconImage(TricycleModelLocator.getInstance().getWindowIcon().getImage());
		fileOpenButton.setText(I18n.getText("general.open")+"...");
		optionsMenuButton.setText(I18n.getText("view.files.options"));
		quitMenuButton.setText(I18n.getText("view.main.quit"));
		aboutMenuButton.setText(I18n.getText("view.main.about"));
		guessFileFormatButton.setText(I18n.getText("view.main.guessFormat"));
		//helpMenuButton.setText("Help");
		checkForUpdatesButton.setText(I18n.getText("view.main.checkForUpdates"));

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
		checkForUpdatesButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckForUpdateEvent event = new CheckForUpdateEvent(true);
				event.dispatch();
			}
		});
		
		guessFileFormatButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(TricycleController.GUESS_FORMAT);
				event.dispatch();
			}
		});

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
		
		TricycleModel tm = TricycleModelLocator.getInstance().getTricycleModel();
		tm.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				String name = argEvt.getPropertyName();
				if(name.equals("tracking")){
					if(MVC.getTracker() == null){
						AnalyticsConfigData config = new AnalyticsConfigData(TricycleModel.ANALYTICS_CODE);
						JGoogleAnalyticsTracker t = new JGoogleAnalyticsTracker(config, GoogleAnalyticsVersion.V_4_7_2);
						MVC.setTracker(t);
					}
					
					boolean tracking = (Boolean)argEvt.getNewValue();
					MVC.getTracker().setEnabled(tracking);
					TricycleModelLocator.getInstance().setTracking(tracking);
				}
				else if (name.equals("autoupdate"))
				{
					boolean autoupdate = (Boolean)argEvt.getNewValue();
					TricycleModelLocator.getInstance().setAutoUpdate(autoupdate);
				}
			}
		});
	}
}
