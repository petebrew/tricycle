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
import java.util.prefs.Preferences;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger log = LoggerFactory.getLogger(TricycleModelLocator.class);
	
	private static final TricycleModelLocator ml = new TricycleModelLocator();
	private static final String PROPERTIES_LOCATION = "TRiCYCLE-properties.xml";
	private static final String LAST_DIRECTORY = "LastDirectory";
	private static final String LAST_INPUT_FORMAT = "LastInputFormat";
	private static final String LAST_OUTPUT_FORMAT = "LastOutputFormat";
	private static final String LAST_LOCALE_COUNTRY = "LastLocaleCountry";
	private static final String LAST_LOCALE_LANGUAGE = "LastLocaleLanguage";
	private static final String TRACKING = "Tracking";
	private static final String DONT_ASK_TRACKING = "DontAskTracking";
	private static final String AUTOUPDATE = "AutoUpdate";
	private static final String WARN_ABOUT_MATRIX_STYLE = "WarnAboutMatrixStyle";
	private static final String LAST_NAMING_CONVENTION = "LastNamingConvention";
	
	
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
	private Preferences prefs = Preferences.userNodeForPackage(TricycleModelLocator.class);
	
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
	
	public Preferences getPreferences(){
		return prefs;
	}
	
	public boolean isTracking(){
		return prefs.getBoolean(TRACKING, false);
	}
	
	public boolean isAutoUpdate(){
		return prefs.getBoolean(AUTOUPDATE, true);
	}
	
	public boolean isDontAskTracking(){
		return prefs.getBoolean(DONT_ASK_TRACKING, false);
	}
	
	public boolean isWarnAboutMatrixStyle(){
		return prefs.getBoolean(WARN_ABOUT_MATRIX_STYLE, true);
	}
	
	public void setAutoUpdate(boolean argBool){
		prefs.putBoolean(AUTOUPDATE, argBool);
	}
	
	public void setTracking(boolean argBool){
		prefs.putBoolean(TRACKING, argBool);
	}
	
	public void setDontAskTracking(boolean argBool){
		prefs.putBoolean(DONT_ASK_TRACKING, argBool);
	}
	
	public void setWarnAboutMatrixStyle(boolean argBool){
		prefs.putBoolean(WARN_ABOUT_MATRIX_STYLE, argBool);
	}	
	
	public File getLastDirectory(){
		String dir = prefs.get(LAST_DIRECTORY, null);
		if(dir != null){
			return new File(dir);
		}
		return null;
	}
		
	public void setLastDirectory(File argLastDirectory){
		String path = argLastDirectory.getAbsolutePath();
		String lastDir = prefs.get(LAST_DIRECTORY, null);
		if(lastDir == null || !path.equals(lastDir)){
			prefs.put(LAST_DIRECTORY, path);
		}
	}
	
	public String getLastUsedInputFormat(){
		return prefs.get(LAST_INPUT_FORMAT, null);				
	}
	
	public void setLastUsedInputFormat(String format){
		prefs.put(LAST_INPUT_FORMAT, format);
	}
	
	public String getLastUsedOutputFormat(){
		return prefs.get(LAST_OUTPUT_FORMAT, null);				
	}
	
	public void setLastUsedOutputFormat(String format){
		prefs.put(LAST_OUTPUT_FORMAT, format);
	}
	
	public String getLastNamingConvention(){
		return prefs.get(LAST_NAMING_CONVENTION, null);				
	}
	
	public void setLastNamingConvention(String format){
		prefs.put(LAST_NAMING_CONVENTION, format);
	}
	
	public String getLastLocaleCountry(){
		return prefs.get(LAST_LOCALE_COUNTRY, null);				
	}
	
	public void setLastLocaleCountry(String locale){
		prefs.put(LAST_LOCALE_COUNTRY, locale);
	}
	
	public String getLastLocaleLanguage(){
		return prefs.get(LAST_LOCALE_LANGUAGE, null);				
	}
	
	public void setLastLocaleLanguage(String locale){
		prefs.put(LAST_LOCALE_LANGUAGE, locale);
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
