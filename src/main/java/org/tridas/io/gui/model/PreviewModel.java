/**
 * Created on Jun 7, 2010, 6:33:44 PM
 */
package org.tridas.io.gui.model;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author Daniel Murphy
 */
public class PreviewModel extends AbstractModel {
	
	private String fileString = null;
	private String filename = null;
	
	public void setFileString(String argFileString) {
		String old = fileString;
		fileString = argFileString;
		firePropertyChange("fileString", old, fileString);
	}
	
	public String getFileString() {
		return fileString;
	}
	
	public void setFilename(String argFilename) {
		String old = filename;
		filename = argFilename;
		firePropertyChange("filename", old, filename);
	}
	
	public String getFilename() {
		return filename;
	}
}
