/**
 * Created on May 26, 2010, 9:27:46 PM
 */
package org.tridas.io.gui.control.main.fileList;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 *
 */
public class BrowseEvent extends MVCEvent {

	/**
	 * @param argKey
	 */
	public BrowseEvent() {
		 super(FileListController.BROWSE);
	}
}
