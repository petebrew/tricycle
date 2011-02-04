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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.gui.control.StartupEvent;
import org.tridas.io.gui.model.TricycleModelLocator;

import com.dmurph.mvc.MVC;
import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

public class App {
	
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
	// here to instantiate controllers
	protected static final TricycleModelLocator model = TricycleModelLocator.getInstance();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(!System.getProperty("os.name").startsWith("Mac")){
			// For non-MacOSX systems set Nimbus as LnF
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				log.warn(I18n.getText("lookfeel.nimbus"));
			}
		}
		else
		{
			// On MacOSX set standard GUI conventions...
			
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Corina");
			System.setProperty("com.apple.macos.use-file-dialog-packages", "false"); // for AWT
			UIManager.put("JFileChooser.packageIsTraversable", "never"); // for swing

			
		}
//		EventMonitor monitor = new EventMonitor( null, 400);
//		MVC.setGlobalEventMonitor(monitor);
//		monitor.setVisible(true);
		
		// set tracking key
		AnalyticsConfigData config = new AnalyticsConfigData("UA-17109202-7");
		JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(config, GoogleAnalyticsVersion.V_4_7_2);
		//MVC.setTracker(tracker);
		
		(new StartupEvent(true)).dispatch();
	}
}
