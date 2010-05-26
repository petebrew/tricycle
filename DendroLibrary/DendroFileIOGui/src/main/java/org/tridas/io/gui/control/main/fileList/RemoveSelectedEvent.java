/**
 * Created on May 26, 2010, 3:50:21 AM
 */
package org.tridas.io.gui.control.main.fileList;

import java.util.Set;

import org.tridas.io.gui.mvc.control.MVCEvent;

/**
 * @author Daniel
 *
 */
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
