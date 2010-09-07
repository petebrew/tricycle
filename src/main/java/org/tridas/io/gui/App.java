/**
 * Copyright 2010 Daniel Murphy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tridas.io.gui;

import javax.swing.UIManager;

import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.control.MainWindowController;
import org.tridas.io.gui.model.TricycleModelLocator;

import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.monitor.EventMonitor;

public class App {
	
	private static final SimpleLogger log = new SimpleLogger(App.class);
	
	// here to instantiate controllers
	protected static final TricycleModelLocator model = TricycleModelLocator.getInstance();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(!System.getProperty("os.name").startsWith("Mac")){
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				log.warn(I18n.getText("lookfeel.nimbus"));
			}
		}
		EventMonitor monitor = new EventMonitor( null, 400);
		MVC.setGlobalEventMonitor(monitor);
		monitor.setVisible(false);
		
		(new MVCEvent(MainWindowController.STARTUP)).dispatch();
	}
}
