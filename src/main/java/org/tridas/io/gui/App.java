package org.tridas.io.gui;

import javax.swing.UIManager;

import org.grlea.log.SimpleLogger;
import org.tridas.io.control.main.MainWindowController;
import org.tridas.io.gui.model.ModelLocator;
import org.tridas.io.gui.mvc.control.CEvent;
import org.tridas.io.gui.mvc.control.MVC;

public class App {
	
	private static final SimpleLogger log = new SimpleLogger(App.class);
	
	// here to instantiate controllers
	protected static final ModelLocator model = ModelLocator.getInstance();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			log.warn("Could not use nimbus look and feel, probably on a Mac.");
		}
		MVC.dispatchEvent(new CEvent(MainWindowController.STARTUP_EVENT));
	}
}
