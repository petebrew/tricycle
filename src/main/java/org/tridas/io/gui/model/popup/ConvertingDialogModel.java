/**
 * Created at Jun 21, 2010, 3:47:23 AM
 */
package org.tridas.io.gui.model.popup;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author daniel
 *
 */
public class ConvertingDialogModel extends AbstractModel {

	private int convertingPercent = 0;
	private String convertingFilename = null;
	
	
	public void setConvertingPercent(int argConvertingPercent) {
		int old = convertingPercent;
		convertingPercent = argConvertingPercent;
		firePropertyChange("convertingPercent", old, convertingPercent);
	}
	
	public int getConvertingPercent() {
		return convertingPercent;
	}
	
	public void setConvertingFilename(String argConvertingFilename) {
		String old = convertingFilename;
		convertingFilename = argConvertingFilename;
		firePropertyChange("convertingFilename", old, convertingFilename);
	}
	
	public String getConvertingFilename() {
		return convertingFilename;
	}
}
