/**
 * Created on May 26, 2010, 3:49:32 AM
 */
package org.tridas.io.gui.control.main.fileList;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.tridas.io.gui.model.fileList.FileListModel;
import org.tridas.io.gui.mvc.control.FrontController;
import org.tridas.io.gui.mvc.control.MVCEvent;
import org.tridas.io.gui.mvc.control.events.ObjectEvent;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;

/**
 * @author Daniel
 *
 */
public class FileListController extends FrontController {
	public static final String REMOVE_SELECTED = "FILE_LIST_REMOVE_SELECTED";
	public static final String ADD_FILE = "FILE_LIST_ADD_FILE";
	public static final String BROWSE = "FILE_LIST_BROWSE";
	
	public FileListController(){
		registerEventKey(REMOVE_SELECTED, "removeSelected");
		registerEventKey(ADD_FILE, "addFile");
		registerEventKey(BROWSE, "browse");
	}
	
	public void removeSelected(MVCEvent argEvent){
		RemoveSelectedEvent event = (RemoveSelectedEvent) argEvent;
		FileListModel model = FileListModel.getInstance();
		
		model.removeInputFiles(event.getSelectedSet());
	}
	
	public void addFile(MVCEvent argEvent){
		AddFileEvent event = (AddFileEvent) argEvent;
		FileListModel model = FileListModel.getInstance();
		model.addInputFile(event.getFile());
	}
	
	public void browse(MVCEvent argEvent){
		File[] files = IOUtils.inputFiles(null);
		if(files == null){
			return;
		}
		FileListModel model = FileListModel.getInstance();
		
		if(files.length == 1){
			model.setFileField(files[0].getAbsolutePath());
		}else{
			HashSet<String> s = new HashSet<String>();
			for(File file : files){
				s.add(file.getAbsolutePath());
			}
			model.addInputFiles(s);
		}
	}
}
