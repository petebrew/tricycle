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
	public static final String ADD_FILE = "FILE_LIST_ADD_FILE";
	
	public FileListController(){
		registerEventKey(REMOVE_SELECTED, "removeSelected");
		registerEventKey(ADD_FILE, "addFile");
	}
	
	public void removeSelected(MVCEvent argEvent){
		RemoveSelectedEvent event = (RemoveSelectedEvent) argEvent;
		FileListModel model = FileListModel.getInstance();
		
		model.removeInputFiles(event.getSelectedSet());
	}
	
	public void addFile(MVCEvent argEvent){
		
	}
}
