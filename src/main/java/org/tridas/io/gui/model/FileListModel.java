/**
 * Created on May 25, 2010, 8:18:44 PM
 */
package org.tridas.io.gui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.grlea.log.SimpleLogger;

import com.dmurph.mvc.model.AbstractModel;
import com.dmurph.mvc.util.MVCArrayList;
/**
 * @author Daniel
 */
public class FileListModel extends AbstractModel {
	private static final FileListModel model = new FileListModel();

	private static final SimpleLogger log = new SimpleLogger(FileListModel.class);

	private String inputFormat = "automatic";
	private String fileField = null;
	private ArrayList<String> inputFiles = new MVCArrayList<String>();

	private FileListModel() {}

	/**
	 * @param inputFormat
	 *            the inputFormat to set
	 */
	public void setInputFormat(String argInputFormat) {
		String old = inputFormat;
		inputFormat = argInputFormat;
		firePropertyChange("inputFormat", old, inputFormat);
	}

	/**
	 * @return the inputFormat
	 */
	public String getInputFormat() {
		return inputFormat;
	}

	/**
	 * @param fileField the fileField to set
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
		if(inputFiles.contains(argFile)){
			return;
		}
		MVCArrayList<File> old = (MVCArrayList<File>) inputFiles.clone();
		inputFiles.add(argFile);
		firePropertyChange("inputFiles", old, inputFiles.clone());
	}

	public void addInputFiles(Set<String> argFiles) {
		MVCArrayList<File> old = (MVCArrayList<File>) inputFiles.clone();
		HashSet<String> notCommon = new HashSet<String>();
		
		notCommon.addAll(inputFiles);
		notCommon.addAll(argFiles);
		
		inputFiles.clear();
		inputFiles.addAll(notCommon);
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

	public ArrayList<String> getInputFiles() {
		return (ArrayList<String>) inputFiles.clone();
	}

	public static FileListModel getInstance() {
		return model;
	}
}
