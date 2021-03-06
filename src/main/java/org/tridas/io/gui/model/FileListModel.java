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
 * Created on May 25, 2010, 8:18:44 PM
 */
package org.tridas.io.gui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.tridas.io.enums.InputFormat;

import com.dmurph.mvc.model.AbstractModel;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author Daniel
 */
@SuppressWarnings("unchecked")
public class FileListModel extends AbstractModel {
	
	private static final long serialVersionUID = 1L;
	
	//private static final SimpleLogger log = new SimpleLogger(FileListModel.class);
	
	private String inputFormat = InputFormat.getInputFormats()[0];
	private String fileField = null;
	private final MVCArrayList<String> inputFiles = new MVCArrayList<String>();
	private String treatAs = null;
	
	
	public FileListModel() {}
	
	/**
	 * @param inputFormat
	 *            the inputFormat to set
	 */
	public void setInputFormat(String argInputFormat) {
		
		ArrayList<String> possFormats = new ArrayList<String>(Arrays.asList(InputFormat.getInputFormats()));
		
		if(!possFormats.contains(argInputFormat))
		{
			return;
		}
		
		String old = inputFormat;
		inputFormat = argInputFormat;
		firePropertyChange("inputFormat", old, inputFormat);
	}
	
	public void setFilesTreatAs(String arg)
	{
		String old = treatAs;
		treatAs = arg;
		firePropertyChange("treatAs", old, treatAs);
	}
	
	/**
	 * @return the inputFormat
	 */
	public String getInputFormat() {
		return inputFormat;
	}
	
	public String getFilesTreatAs()
	{
		return treatAs;
	}
	
	
	/**
	 * @param fileField
	 *            the fileField to set
	 */
	public void setFileField(String argFileField) {
		String old = fileField;
		fileField = argFileField;
		firePropertyChange("fileField", old, fileField);
	}
	
	/**
	 * @return the fileField
	 */
	public String getFileField() {
		return fileField;
	}
	
	public void addInputFile(String argFile) {
		if (inputFiles.contains(argFile)) {
			return;
		}
		MVCArrayList<File> old = (MVCArrayList<File>) inputFiles.clone();
		inputFiles.add(argFile);
		firePropertyChange("inputFiles", old, inputFiles.clone());
	}
	
	public void addInputFiles(String[] strings) {
		MVCArrayList<File> old = (MVCArrayList<File>) inputFiles.clone();
		HashSet<String> notCommon = new HashSet<String>();
		
		notCommon.addAll(inputFiles);
		
		for(String filename : strings)
		{
			notCommon.add(filename);
		}
		
		inputFiles.clear();
		for(String s : notCommon){
			inputFiles.add(s);
		}
		firePropertyChange("inputFiles", old, inputFiles.clone());
	}
	
	public void removeInputFile(String argFile) {
		MVCArrayList<File> old = (MVCArrayList<File>) inputFiles.clone();
		if (inputFiles.remove(argFile)) {
			firePropertyChange("inputFiles", old, inputFiles.clone());
		}
	}
	
	public void removeInputFiles(Set<String> argFiles) {
		MVCArrayList<File> old = (MVCArrayList<File>) inputFiles.clone();
		if (inputFiles.removeAll(argFiles)) {
			firePropertyChange("inputFiles", old, inputFiles.clone());
		}
	}
	
	public void clearInputFiles() {
		if (inputFiles.size() == 0) {
			return;
		}
		MVCArrayList<File> old = (MVCArrayList<File>) inputFiles.clone();
		inputFiles.clear();
		firePropertyChange("inputFiles", old, inputFiles);
	}
	
	public MVCArrayList<String> getInputFiles() {
		return inputFiles;
	}
}
