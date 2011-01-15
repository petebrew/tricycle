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
package org.tridas.io.gui.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLog;
import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.control.TricycleController;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.control.fileList.FileListController;
import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.gui.view.popup.ConvertProgress;
import org.tridas.io.gui.view.popup.SavingProgress;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;

@SuppressWarnings("unused")
public class TricycleModelLocator {
	private static final SimpleLogger log = new SimpleLogger(TricycleModelLocator.class);
	
	private static final TricycleModelLocator ml = new TricycleModelLocator();
	private static final String PROPERTIES_LOCATION = "TRiCYCLE-properties.xml";
	private static final String LAST_DIRECTORY = "LastDirectory";
	
	private final ConfigModel configModel = new ConfigModel();
	private final FileListModel fileListModel = new FileListModel();
	private final ConvertModel convertModel = new ConvertModel();
	private final TricycleModel tricycleModel = new TricycleModel();
	
	private TricycleController mainWindowController = new TricycleController();
	private FileListController fileListController = new FileListController();
	private ConvertController convertController = new ConvertController();
	private ConfigController configController = new ConfigController(configModel);
	
	private ImageIcon windowIcon;
	private MainWindow view = null;
	private Properties properties = null;
	
	private SavingProgress savingProgress = null;
	private ConvertProgress convertProgress = null;
	

	private TricycleModelLocator() {
		URL windowIconURL = IOUtils.getFileInJarURL("icons/64x64/application.png");
		if(windowIconURL != null){
			windowIcon = new ImageIcon( windowIconURL);
		}else{
			windowIcon = new ImageIcon();
		}
	}
	
	public TricycleModel getTricycleModel() {
		return tricycleModel;
	}
	
	public ConfigModel getConfigModel(){
		return configModel;
	}
	
	public FileListModel getFileListModel(){
		return fileListModel;
	}
	
	public ConvertModel getConvertModel(){
		return convertModel;
	}
	
	public Properties getProperties(){
		if(properties == null){
			properties = new Properties();
			FileHelper fh = new FileHelper();
			InputStream is = fh.createInput(PROPERTIES_LOCATION);
			if(is != null){
				try {
					properties.loadFromXML(fh.createInput(PROPERTIES_LOCATION));
				} catch (InvalidPropertiesFormatException e) {
					log.error("Could not load properties.");
					log.dbe(DebugLevel.L2_ERROR, e);
				} catch (IOException e) {
					log.error("Could not load properties.");
					log.dbe(DebugLevel.L2_ERROR, e);
				}
			}
		}
		return properties;
	}
	
	public void saveProperties(){
		if(properties == null){
			getProperties();
		}
		FileHelper fh = new FileHelper();
		try {
			properties.storeToXML(fh.createOutput(PROPERTIES_LOCATION), "Saved on "+DateFormat.getDateTimeInstance().format(new Date()));
		} catch (IOException e) {
			log.error("Could not save properties.");
			log.dbe(DebugLevel.L2_ERROR, e);
		}
	}
	
	public File getLastDirectory(){
		Properties p = getProperties();
		if(p.containsKey(LAST_DIRECTORY)){
			return new File(p.getProperty(LAST_DIRECTORY));
		}
		return null;
	}
	
	public void setLastDirectory(File argLastDirectory){
		Properties p = getProperties();
		String path = p.getProperty(LAST_DIRECTORY);
		if(path == null || !path.equals(argLastDirectory.getAbsolutePath())){
			p.setProperty(LAST_DIRECTORY, argLastDirectory.getAbsolutePath());
			saveProperties();
		}
	}
	
	public ImageIcon getWindowIcon(){
		return windowIcon;
	}
	
	public void setMainWindow(MainWindow argWindow) {
		view = argWindow;
	}
	
	public MainWindow getMainWindow() {
		return view;
	}
	
	/**
	 * @return the savingProgress
	 */
	public SavingProgress getSavingProgress() {
		return savingProgress;
	}

	/**
	 * @param argSavingProgress the savingProgress to set
	 */
	public void setSavingProgress(SavingProgress argSavingProgress) {
		savingProgress = argSavingProgress;
	}

	/**
	 * @return the convertProgress
	 */
	public ConvertProgress getConvertProgress() {
		return convertProgress;
	}

	/**
	 * @param argConvertProgress the convertProgress to set
	 */
	public void setConvertProgress(ConvertProgress argConvertProgress) {
		convertProgress = argConvertProgress;
	}

	public static TricycleModelLocator getInstance() {
		return ml;
	}
}
