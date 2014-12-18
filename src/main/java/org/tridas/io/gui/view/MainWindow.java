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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import org.tridas.io.gui.I18n;
import org.tridas.io.gui.control.CheckForUpdateEvent;
import org.tridas.io.gui.control.TricycleController;
import org.tridas.io.gui.control.fileList.BrowseEvent;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.util.IOUtils;

import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.model.MVCArrayList;
import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

import java.awt.BorderLayout;

import javax.swing.JToolBar;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;

/**
 * @author daniel
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener{
	
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuBar menuBar;
	private JMenuItem menuExit;
	private JMenuItem menuOptions;
	private JMenuItem menuFileOpen;
	private JMenuItem menuFileSave;
	private JMenuItem menuAbout;
	private JMenuItem menuCheckForUpdates;
	private JMenuItem menuGuessFileFormat;
	private JMenuItem menuViewLog;
	
	protected JTabbedPane tabbedPane;
	public FileListPanel fileList;
	public ConvertPanel convertPanel;
	
	public final TricycleModel model;
	private JToolBar toolBar;
	private JToolbarButton btnOpen;
	private JToolbarButton btnOptions;
	private JToolbarButton btnConvert;
	private JToolbarButton btnIdentify;
	private JToolbarButton btnSave;
	private JToolbarButton btnPreview;
	
	private ImageIcon icnFileOpen;
	private ImageIcon icnOptions;
	private ImageIcon icnExit;
	private ImageIcon icnSave;
	private ImageIcon icnIdentify;
	private ImageIcon icnConvert;
	private ImageIcon icnPreview;
	
	/** Creates new form NewJFrame */
	public MainWindow(TricycleModel argModel) {
		model = argModel;
		loadIcons();
		constructComponents();
		populateLocale();
		linkModel();
		addListeners();
		pack();
		convertPanel.toggleSplitPane(JSplitPane.BOTTOM, false);
		setLocationRelativeTo(null);
		setTitle("TRiCYCLE");
		this.setMinimumSize(new Dimension(700, 400));
		

		
	}
	
	private void loadIcons()
	{
		icnFileOpen = null;
		icnOptions = null;	
		icnExit = null;
		icnSave = null;
		icnIdentify = null;
		icnConvert = null;
		icnPreview = null;
		
		try{
			icnFileOpen = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/fileopen.png"));
			icnOptions = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/settings.png"));
			icnExit = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/exit.png"));
			icnSave = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/filesave.png"));
			icnIdentify = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/question.png"));
			icnPreview = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/preview.png"));
			icnConvert = new ImageIcon(IOUtils.getFileInJarURL("icons/22x22/run.png"));

		} catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}
	
	private void constructComponents() {
		
		
		
		tabbedPane = new JTabbedPane();
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		menuExit = new JMenuItem();
		helpMenu = new JMenu();
		menuAbout = new JMenuItem();
		menuCheckForUpdates = new JMenuItem();
		
		menuGuessFileFormat = new JMenuItem();
		menuGuessFileFormat.setIcon(icnIdentify);
		
		menuFileOpen = new JMenuItem();
		menuFileOpen.setIcon(icnFileOpen);
		
		menuOptions = new JMenuItem();
		menuOptions.setIcon(icnOptions);
		
		
		menuViewLog = new JMenuItem();
		getContentPane().setLayout(new MigLayout("", "[696px,grow,fill]", "[38px][515px,grow,fill]"));
		

		fileList = new FileListPanel(TricycleModelLocator.getInstance().getFileListModel(), this);
		convertPanel = new ConvertPanel(TricycleModelLocator.getInstance().getConvertModel());
		

		
		tabbedPane.addTab("Convert files", fileList);
		tabbedPane.addTab("Results", convertPanel);
		// set selected component to the file list
		tabbedPane.setSelectedComponent(fileList);
		
		getContentPane().add(tabbedPane, "cell 0 1,alignx left,aligny top");
		
		
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, "cell 0 0,growx,aligny top");
		
		btnOpen = new JToolbarButton("FileOpen", icnFileOpen);
		toolBar.add(btnOpen);
		
		btnSave = new JToolbarButton(convertPanel.actionSaveAll);
		btnSave.setText("");
		toolBar.add(btnSave);
		
		btnOptions = new JToolbarButton("Options", icnOptions);	
		toolBar.add(btnOptions);
		
		btnConvert = new JToolbarButton("Convert", icnConvert);
		toolBar.add(btnConvert);
		
		btnIdentify = new JToolbarButton("Identify", icnIdentify);
		toolBar.add(btnIdentify);
		
		btnPreview = new JToolbarButton(convertPanel.actionPreviewFile);
		toolBar.add(btnPreview);
		
		
		menuFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(menuFileOpen);
		menuFileSave = new JMenuItem();
		menuFileSave.setAction(convertPanel.actionSaveAll);
		fileMenu.add(menuFileSave);
		fileMenu.addSeparator();
		fileMenu.add(menuOptions);
		
		menuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		if(!System.getProperty("os.name").startsWith("Mac")){
			fileMenu.addSeparator();
			fileMenu.add(menuExit);
		}
		
		menuBar.add(fileMenu);
		
		
		//helpMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

		helpMenu.add(menuViewLog);
		helpMenu.add(menuCheckForUpdates);
		helpMenu.add(menuGuessFileFormat);
		//helpMenu.add(helpMenuButton);
		if(!System.getProperty("os.name").startsWith("Mac")){
			helpMenu.addSeparator();
			helpMenu.add(menuAbout);
		}
		
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}
	
	private void populateLocale() {
		setIconImage(TricycleModelLocator.getInstance().getWindowIcon().getImage());
		
		menuFileOpen.setText(I18n.getText("general.open")+"...");
		btnOpen.setToolTipText(I18n.getText("general.open")+"...");
		
		menuOptions.setText(I18n.getText("view.files.options"));
		btnOptions.setToolTipText(I18n.getText("view.files.options"));
		
		menuExit.setText(I18n.getText("view.main.quit"));
		menuAbout.setText(I18n.getText("view.main.about"));
		
		menuGuessFileFormat.setText(I18n.getText("view.main.guessFormat"));
		btnIdentify.setToolTipText(I18n.getText("view.main.guessFormat"));
		
		btnConvert.setToolTipText(I18n.getText("view.convert.do"));
		
		menuCheckForUpdates.setText(I18n.getText("view.main.checkForUpdates"));
		menuViewLog.setText(I18n.getText("view.main.log"));
		fileMenu.setText(I18n.getText("view.main.file"));
		helpMenu.setText(I18n.getText("view.main.help"));
	}
	
	/**
	 * 
	 */
	private void addListeners() {
		
		// Toolbar buttons
		btnOpen.addActionListener(this);
		btnOptions.addActionListener(this);
		btnConvert.addActionListener(this);
		
		// Menu buttons
		menuOptions.addActionListener(this);
		menuOptions.setActionCommand("Options");
		menuFileOpen.addActionListener(this);
		menuFileOpen.setActionCommand("FileOpen");


		
		menuExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(TricycleController.QUIT);
				event.dispatch();
			}
		});
		menuAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MVCEvent event = new MVCEvent(TricycleController.ABOUT);
				event.dispatch();
			}
		});
		menuCheckForUpdates.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckForUpdateEvent event = new CheckForUpdateEvent(true);
				event.dispatch();
			}
		});
		
		menuGuessFileFormat.setActionCommand("Identify");
		menuGuessFileFormat.addActionListener(this);
		btnIdentify.setActionCommand("Identify");
		btnIdentify.addActionListener(this);

		menuViewLog.addActionListener(new ActionListener() {
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

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("FileOpen"))
		{
			BrowseEvent event = new BrowseEvent();
			event.dispatch();
		}
		if(evt.getActionCommand().equals("Options"))
		{
			MVCEvent event = new MVCEvent(TricycleController.OPTIONS);
			event.dispatch();
		}
		if(evt.getActionCommand().equals("Identify"))
		{
			MVCEvent event = new MVCEvent(TricycleController.GUESS_FORMAT);
			event.dispatch();
		}
		if(evt.getActionCommand().equals("Convert"))
		{
			fileList.doConversion();
		}

	}
}
