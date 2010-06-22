/**
 * Created at Jun 21, 2010, 3:39:37 AM
 */
package org.tridas.io.gui.model.popup;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author daniel
 *
 */
public class SavingDialogModel extends AbstractModel {
	
	private int savingPercent = 0;
	private String savingFilename = null;
	

	public void setSavingPercent(int argSavingPercent) {
		int old = savingPercent;
		savingPercent = argSavingPercent;
		firePropertyChange("savingPercent", old, savingPercent);
	}
	
	public int getSavingPercent() {
		return savingPercent;
	}
	
	public void setSavingFilename(String argSavingFilename) {
		String old = savingFilename;
		savingFilename = argSavingFilename;
		firePropertyChange("savingFilename", old, savingFilename);
	}
	
	public String getSavingFilename() {
		return savingFilename;
	}
}
