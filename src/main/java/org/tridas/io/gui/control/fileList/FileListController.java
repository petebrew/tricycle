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
/**
 * Created on May 26, 2010, 3:49:32 AM
 */
package org.tridas.io.gui.control.fileList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.JFileChooser;

import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModelLocator;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel
 */
public class FileListController extends FrontController {
	public static final String REMOVE_SELECTED = "FILE_LIST_REMOVE_SELECTED";
	public static final String REMOVE_ALL = "FILE_LIST_REMOVE_ALL";
	public static final String ADD_FILE = "FILE_LIST_ADD_FILE";
	public static final String ADD_MULTIPLE_FILES = "FILE_LIST_ADD_MULTIPLE_FILES";
	public static final String BROWSE = "FILE_LIST_BROWSE";
	
	public FileListController() {
		registerCommand(REMOVE_SELECTED, "removeSelected");
		registerCommand(REMOVE_ALL, "removeAll");
		registerCommand(ADD_FILE, "addFile");
		registerCommand(ADD_MULTIPLE_FILES, "addMultipleFiles");
		registerCommand(BROWSE, "browse");
	}
	
	public void removeAll(MVCEvent argEvent){
		FileListModel model = TricycleModelLocator.getInstance().getFileListModel();
		model.clearInputFiles();
	}
	
	public void removeSelected(MVCEvent argEvent) {
		RemoveSelectedEvent event = (RemoveSelectedEvent) argEvent;
		FileListModel model = TricycleModelLocator.getInstance().getFileListModel();
		
		model.removeInputFiles(event.getSelectedSet());
	}
	
	public void addFile(MVCEvent argEvent) {
		AddFileEvent event = (AddFileEvent) argEvent;
		if(event.getFile() == null || event.getFile().equals("")){
			return;
		}
		FileListModel model = TricycleModelLocator.getInstance().getFileListModel();
		model.addInputFile(event.getFile());
	}
	
	public void addMultipleFiles(MVCEvent argEvent) {
		AddMultipleFilesEvent event = (AddMultipleFilesEvent) argEvent;
		if(event.getFiles() == null || event.getFiles().length==0){
			return;
		}
		
		FileListModel model = TricycleModelLocator.getInstance().getFileListModel();
		model.addInputFiles(event.getFiles());
	}
	
	
	public void browse(MVCEvent argEvent) {
		
		// custom jfilechooser
		File[] files = null;
		String format = null;
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		FileListModel model = TricycleModelLocator.getInstance().getFileListModel();
		
		// Add 'all files' file filter and set to default
		fc.setAcceptAllFileFilterUsed(false);
		fc.setAcceptAllFileFilterUsed(true);
		fc.setFileFilter(fc.getAcceptAllFileFilter());
		
		// Loop through formats and create filters for each
		ArrayList<DendroFileFilter> filters = TridasIO.getFileReadingFilterArray();
		Collections.sort(filters);
		for(DendroFileFilter filter : filters)
		{
			fc.addChoosableFileFilter(filter);
			
			// If this is the format set in the main GUI then select it
			if(filter.getFormatName().equals(model.getInputFormat()))
			{
				fc.setFileFilter(filter);
			}
		}

		// Pick the last used directory by default
		try{
			File lastDirectory = TricycleModelLocator.getInstance().getLastDirectoryRead();
			if(lastDirectory != null){
				fc.setCurrentDirectory(lastDirectory);
			}
		} catch (Exception e)
		{
		}
		
		int retValue = fc.showOpenDialog(TricycleModelLocator.getInstance().getMainWindow());
		TricycleModelLocator.getInstance().setLastDirectoryRead(fc.getCurrentDirectory());
		if (retValue == JFileChooser.APPROVE_OPTION) {
			files = fc.getSelectedFiles();
			String formatDesc = fc.getFileFilter().getDescription();
			try{
				format = formatDesc.substring(0, formatDesc.indexOf("(")).trim();
			} catch (Exception e){}
		}
		if (files == null) {
			return;
		}
				
		HashSet<String> s = new HashSet<String>();
		for (File file : files) {
			s.add(file.getAbsolutePath());
		}
		model.addInputFiles(s.toArray(new String[0]));
		model.setInputFormat(format);

		
		
	}
}
