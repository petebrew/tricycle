/**
 * Created on May 26, 2010, 3:49:32 AM
 */
package org.tridas.io.gui.control.main.fileList;

import java.util.Set;

import org.tridas.io.gui.model.fileList.FileListModel;
import org.tridas.io.gui.mvc.control.FrontController;
import org.tridas.io.gui.mvc.control.MVCEvent;
import org.tridas.io.gui.mvc.control.events.ObjectEvent;

/**
 * @author Daniel
 *
 */
public class FileListController extends FrontController {
	public static final String REMOVE_SELECTED = "FILE_LIST_REMOVE_SELECTED";
	
	public FileListController(){
		registerEventKey(REMOVE_SELECTED, "removeSelected");
	}
	
	public void removeSelected(MVCEvent argEvent){
		ObjectEvent<Set<String>> event = (ObjectEvent<Set<String>>) argEvent;
		FileListModel model = FileListModel.getInstance();
		
		model.removeInputFiles(event.getObject());
	}
}
