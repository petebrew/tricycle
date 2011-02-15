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
 * Created at May 20, 2010, 12:47:10 AM
 */
package org.tridas.io.gui.model;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author daniel
 */
public class TricycleModel extends AbstractModel {
	private static final long serialVersionUID = 1L;
	
	public static final String ANALYTICS_CODE = "UA-17109202-7";
		
	private boolean lock = false;
	private boolean tracking = false;
	private boolean autoUpdate = false;
	
	public TricycleModel() {}
	
	/**
	 * @return the tracking
	 */
	public boolean isTracking() {
		return tracking;
	}

	/**
	 * @param argTracking the tracking to set
	 */
	public void setTracking(boolean argTracking) {
		boolean old = tracking;
		tracking = argTracking;
		firePropertyChange("tracking", old, tracking);
	}
	
	/**
	 * @return the tracking
	 */
	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	/**
	 * @param argTracking the tracking to set
	 */
	public void setAutoUpdate(boolean arg) {
		boolean old = autoUpdate;
		autoUpdate = arg;
		firePropertyChange("autoupdate", old, autoUpdate);
	}
	
	/**
	 * @param lock
	 *            the lock to set
	 */
	public void setLock(boolean argLock) {
		boolean old = lock;
		lock = argLock;
		firePropertyChange("lock", old, lock);
	}
	
	/**
	 * @return the lock
	 */
	public boolean isLock() {
		return lock;
	}
}
