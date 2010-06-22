package org.tridas.io.gui.model;

import java.net.URL;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.tridas.io.gui.control.MainWindowController;
import org.tridas.io.gui.control.config.ConfigController;
import org.tridas.io.gui.control.convert.ConvertController;
import org.tridas.io.gui.control.fileList.FileListController;
import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.util.IOUtils;

@SuppressWarnings("unused")
public class ModelLocator {
	private static final ModelLocator ml = new ModelLocator();
	
	private MainWindowController mainWindowController = new MainWindowController();
	private FileListController fileListController = new FileListController();
	private ConvertController convertController = new ConvertController();
	private ConfigController configController = new ConfigController();
	
	private ImageIcon windowIcon;
	
	private MainWindow view = null;
	
	private LinkedList<JFrame> dependantPopups = new LinkedList<JFrame>();
	
	private ModelLocator() {
		URL windowIconURL = IOUtils.getFileInJarURL("icons/16x16/application.png");
		if(windowIconURL != null){
			windowIcon = new ImageIcon( windowIconURL);
		}else{
			windowIcon = new ImageIcon();
		}
	}
	
	public ImageIcon getWindowIcon(){
		return windowIcon;
	}
	
	public void setMainWindow(MainWindow argWindow) {
		view = argWindow;
	}
	
	public MainWindow getMainWindow() {
		return view;
	}
	
	public static ModelLocator getInstance() {
		return ml;
	}
}
