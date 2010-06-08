package org.tridas.io.gui;

import javax.swing.UIManager;

import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.control.MainWindowController;
import org.tridas.io.gui.model.ModelLocator;

import com.dmurph.mvc.MVCEvent;

public class App {
	
	private static final SimpleLogger log = new SimpleLogger(App.class);
	
	// here to instantiate controllers
	protected static final ModelLocator model = ModelLocator.getInstance();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(!System.getProperty("os.name").startsWith("Mac")){
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				log.warn("Could not use nimbus look and feel");
			}
		}
		
		(new MVCEvent(MainWindowController.STARTUP)).dispatch();
	}
}
