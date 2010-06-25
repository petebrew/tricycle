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
 * Created on May 26, 2010, 3:50:21 AM
 */
package org.tridas.io.gui.control.fileList;

import java.util.Set;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class RemoveSelectedEvent extends MVCEvent {
	
	private final Set<String> selectedSet;
	
	/**
	 * @param argKey
	 */
	public RemoveSelectedEvent(Set<String> argSelectedSet) {
		super(FileListController.REMOVE_SELECTED);
		selectedSet = argSelectedSet;
	}
	
	/**
	 * @return the selectedSet
	 */
	public Set<String> getSelectedSet() {
		return selectedSet;
	}
	
}
