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
 * @author sbr00pwb
 *
 */
public class GuessFormatDialogModel extends AbstractModel {
	private static final long serialVersionUID = 1L;
	
	private int progressPercent = 0;
	private boolean cancelled = false;
	
	public void setProgressPercent(int argProgressPercent) {
		int old = progressPercent;
		progressPercent = argProgressPercent;
		firePropertyChange("progressPercent", old, progressPercent);
	}
	
	public int getProgressPercent() {
		return progressPercent;
	}
	
	public void setCancelled(boolean argCancelled) {
		boolean old = cancelled;
		this.cancelled = argCancelled;
		firePropertyChange("cancelled", old, cancelled);
	}

	public boolean isCancelled() {
		return cancelled;
	}
}
