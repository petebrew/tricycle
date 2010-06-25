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
public class MainWindowModel extends AbstractModel {
	
	private static final MainWindowModel model = new MainWindowModel();
	
	//private static final SimpleLogger log = new SimpleLogger(MainWindowModel.class);
	
	private boolean lock = false;
	
	private MainWindowModel() {}
	
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
	
	public static MainWindowModel getInstance() {
		return model;
	}
}
