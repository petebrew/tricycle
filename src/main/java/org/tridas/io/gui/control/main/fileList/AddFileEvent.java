/**
 * Created on May 26, 2010, 12:17:37 PM
 */
package org.tridas.io.gui.control.main.fileList;

import org.tridas.io.gui.mvc.control.MVCEvent;

/**
 * @author Daniel
 *
 */
public class AddFileEvent extends MVCEvent {

	/**
	 * @param argKey
	 */
	public AddFileEvent() {
		super(FileListController.ADD_FILE);
	}
}
