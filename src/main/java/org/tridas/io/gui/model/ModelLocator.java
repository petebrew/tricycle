package org.tridas.io.gui.model;

import org.tridas.io.gui.control.MainWindowController;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.control.fileList.FileListController;

public class ModelLocator {
	private static final ModelLocator ml = new ModelLocator();
	
	private MainWindowController mainWindowController = new MainWindowController();
	private FileListController fileListController = new FileListController();
	private ConvertController convertController = new ConvertController();
	private ConfigController configController = new ConfigController();
	
	private ModelLocator() {

	}
	
	public static ModelLocator getInstance() {
		return ml;
	}
}
