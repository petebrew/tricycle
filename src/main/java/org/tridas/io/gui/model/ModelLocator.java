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
import java.net.URL;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.tridas.io.gui.control.MainWindowController;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.control.fileList.FileListController;
import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.util.IOUtils;

@SuppressWarnings("unused")
public class ModelLocator {
	private static final ModelLocator ml = new ModelLocator();
	
	private MainWindowController mainWindowController = new MainWindowController();
	private FileListController fileListController = new FileListController();
	private ConvertController convertController = new ConvertController();
	private ConfigController configController = new ConfigController();
	
	private ImageIcon windowIcon;
	private File lastDirectory = null;
	private MainWindow view = null;
	
	private LinkedList<JFrame> dependantPopups = new LinkedList<JFrame>();
	
	private ModelLocator() {
		URL windowIconURL = IOUtils.getFileInJarURL("icons/64x64/application.png");
		if(windowIconURL != null){
			windowIcon = new ImageIcon( windowIconURL);
		}else{
			windowIcon = new ImageIcon();
		}
	}
	
	public File getLastDirectory(){
		return lastDirectory;
	}
	
	public void setLastDirectory(File argLastDirectory){
		lastDirectory = argLastDirectory;
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
	
	public static ModelLocator getInstance() {
		return ml;
	}
}
