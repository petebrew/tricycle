package org.tridas.io.gui.control.main;

import org.tridas.io.gui.mvc.control.MVCEvent;
import org.tridas.io.gui.mvc.control.FrontController;
import org.tridas.io.gui.view.MainView;

public class MainWindowController extends FrontController {
	public static final String STARTUP_EVENT = "STARTUP_EVENT";

	public MainView view = null;

	public MainWindowController() {
		registerEventKey(STARTUP_EVENT, "startup");
	}

	public void startup(MVCEvent argEvent) {
		view = new MainView();
		view.setVisible(true);
		view.setDefaultCloseOperation(3);
	}
}
