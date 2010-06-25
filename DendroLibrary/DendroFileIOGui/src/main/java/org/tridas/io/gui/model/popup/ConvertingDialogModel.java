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
