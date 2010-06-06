package org.tridas.io.gui.control;

import org.tridas.io.gui.view.MainView;
import org.tridas.io.gui.view.popup.SavingProgress;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

public class MainWindowController extends FrontController {
	public static final String STARTUP_EVENT = "STARTUP_EVENT";

	public MainView view = null;

	public MainWindowController() {
		try {
			registerCommand(STARTUP_EVENT, "startup");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public void startup(MVCEvent argEvent) {
		view = new MainView();
		view.setVisible(true);
		view.setDefaultCloseOperation(3);
	}
}
