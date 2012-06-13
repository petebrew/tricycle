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
 * Created on May 26, 2010, 12:17:37 PM
 */
package org.tridas.io.gui.control.fileList;

import java.io.File;
import java.util.ArrayList;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class AddMultipleFilesEvent extends MVCEvent {
	
	private final File[] files;
	
	/**
	 * @param argKey
	 */
	public AddMultipleFilesEvent(File[] files2) {
		super(FileListController.ADD_MULTIPLE_FILES);
		files = files2;
			
	}
	
	public AddMultipleFilesEvent(String[] files2)
	{
		super(FileListController.ADD_MULTIPLE_FILES);
		
		ArrayList<File> fileList = new ArrayList<File>();
		for(String f : files2)
		{
			fileList.add(new File(f.trim()));
		}
		
		files = fileList.toArray(new File[0]);
	}
	
	/**
	 * @return the file
	 */
	public String[] getFiles() {
		ArrayList<String> fileList = new ArrayList<String>();
		for(File file : files)
		{
			fileList.add(file.getAbsolutePath());
		}
		
		return fileList.toArray(new String[0]);
		
	}
}
