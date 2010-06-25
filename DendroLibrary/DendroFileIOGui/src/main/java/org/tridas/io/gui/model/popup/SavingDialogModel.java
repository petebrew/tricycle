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
