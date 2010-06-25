/**
 * Created on May 26, 2010, 3:49:32 AM
 */
package org.tridas.io.gui.control.fileList;

import java.io.File;
import java.util.HashSet;

import javax.swing.JFileChooser;

import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.ModelLocator;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel
 */
public class FileListController extends FrontController {
	public static final String REMOVE_SELECTED = "FILE_LIST_REMOVE_SELECTED";
	public static final String REMOVE_ALL = "FILE_LIST_REMOVE_ALL";
	public static final String ADD_FILE = "FILE_LIST_ADD_FILE";
	public static final String BROWSE = "FILE_LIST_BROWSE";
	
	public FileListController() {
		try {
			registerCommand(REMOVE_SELECTED, "removeSelected");
			registerCommand(REMOVE_ALL, "removeAll");
			registerCommand(ADD_FILE, "addFile");
			registerCommand(BROWSE, "browse");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void removeAll(MVCEvent argEvent){
		FileListModel model = FileListModel.getInstance();
		model.clearInputFiles();
	}
	
	public void removeSelected(MVCEvent argEvent) {
		RemoveSelectedEvent event = (RemoveSelectedEvent) argEvent;
		FileListModel model = FileListModel.getInstance();
		
		model.removeInputFiles(event.getSelectedSet());
	}
	
	public void addFile(MVCEvent argEvent) {
		AddFileEvent event = (AddFileEvent) argEvent;
		if(event.getFile() == null || event.getFile().equals("")){
			return;
		}
		FileListModel model = FileListModel.getInstance();
		model.addInputFile(event.getFile());
	}
	
	public void browse(MVCEvent argEvent) {
		
		// custom jfilechooser
		File[] files = null;
		JFileChooser fd = new JFileChooser();
		fd.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fd.setMultiSelectionEnabled(true);
		File lastDirectory = ModelLocator.getInstance().getLastDirectory();
		if(lastDirectory != null){
			fd.setCurrentDirectory(lastDirectory);
		}
		
		int retValue = fd.showOpenDialog(ModelLocator.getInstance().getMainWindow());
		if (retValue == JFileChooser.APPROVE_OPTION) {
			files = fd.getSelectedFiles();
			ModelLocator.getInstance().setLastDirectory(fd.getCurrentDirectory());
		}
		if (files == null) {
			return;
		}
		
		FileListModel model = FileListModel.getInstance();
		
		if (files.length == 1) {
			model.setFileField(files[0].getAbsolutePath());
		}
		else {
			HashSet<String> s = new HashSet<String>();
			for (File file : files) {
				s.add(file.getAbsolutePath());
			}
			model.addInputFiles(s);
		}
	}
}
