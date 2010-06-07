package org.tridas.io.gui.control;

import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.gui.view.popup.AboutWindow;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

public class MainWindowController extends FrontController {
	public static final String STARTUP = "STARTUP_EVENT";
	public static final String QUIT = "MAIN_WINDOW_QUIT";
	public static final String ABOUT = "MAIN_WINDOW_ABOUT";
	
	public MainWindow view = null;
	public AboutWindow about = null;
	
	public MainWindowController() {
		try {
			registerCommand(STARTUP, "startup");
			registerCommand(QUIT, "quit");
			registerCommand(ABOUT, "about");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void startup(MVCEvent argEvent) {
		view = new MainWindow();
		view.setVisible(true);
		view.setDefaultCloseOperation(3);
		about = new AboutWindow();
		about.setVisible(true);
		about.pack();
	}
	
	public void quit(MVCEvent argEvent) {
		System.exit(0);
	}
	
	public void about(MVCEvent argEvent) {
		about.setVisible(true);
		about.toFront();
	}
}
