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

	private final String file;
	
	/**
	 * @param argKey
	 */
	public AddFileEvent(String argFile) {
		super(FileListController.ADD_FILE);
		file = argFile;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
}
