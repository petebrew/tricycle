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
 * Created on May 26, 2010, 9:27:46 PM
 */
package org.tridas.io.gui.control.fileList;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class BrowseEvent extends MVCEvent {
	
	/**
	 * @param argKey
	 */
	public BrowseEvent() {
		super(FileListController.BROWSE);
	}
}
