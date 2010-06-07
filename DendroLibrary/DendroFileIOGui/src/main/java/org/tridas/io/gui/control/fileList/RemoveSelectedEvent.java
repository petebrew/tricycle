/**
 * Created on May 26, 2010, 3:50:21 AM
 */
package org.tridas.io.gui.control.fileList;

import java.util.Set;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
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
