package org.tridas.io.gui.model;

import org.tridas.io.gui.control.main.MainWindowController;
import org.tridas.io.gui.control.main.convert.ConvertController;
import org.tridas.io.gui.control.main.fileList.FileListController;

public class ModelLocator {
	private static final ModelLocator ml = new ModelLocator();

	private MainWindowController mainWindowController = new MainWindowController();
	private FileListController fileListController = new FileListController();
	private ConvertController convertController = new ConvertController();
	
	private ModelLocator() {

	}

	public static ModelLocator getInstance() {
		return ml;
	}
}
