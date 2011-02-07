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

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.gui.control.StartupEvent;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;

import com.dmurph.mvc.MVC;
import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.JGoogleAnalyticsTracker.GoogleAnalyticsVersion;

public class App {
	
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
	// here to instantiate controllers
	protected static final TricycleModelLocator model = TricycleModelLocator.getInstance();
	
	// resource bundle containing build details
	private final static ResourceBundle build;
	
	
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
		AnalyticsConfigData config = new AnalyticsConfigData(TricycleModel.ANALYTICS_CODE);
		JGoogleAnalyticsTracker t = new JGoogleAnalyticsTracker(config, GoogleAnalyticsVersion.V_4_7_2);
		MVC.setTracker(t);
		
		t.setEnabled(model.getTricycleModel().isTracking());
		
		(new StartupEvent(true)).dispatch();
	}
	

	
	static {
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle("build");
		} catch (MissingResourceException mre) {
			System.out.println("Could not find build properties file.");
			mre.printStackTrace();
			bundle = new ResourceBundle() {
				
				@Override
				protected Object handleGetObject(String key) {
					return key;
				}
				
				@Override
				public Enumeration<String> getKeys() {
					return EMPTY_ENUMERATION;
				}
				
				private final Enumeration<String> EMPTY_ENUMERATION = new Enumeration<String>() {
					public boolean hasMoreElements() {
						return false;
					}
					
					public String nextElement() {
						throw new NoSuchElementException();
					}
				};
			};
		}
		build = bundle;
	}
	
	public static String getBuildTimestamp()
	{
		return getBuildInfo("buildtimestamp");
	}
	
	public static String getBuildRevision()
	{
		return getBuildInfo("buildnumber");
	}
	
	public static String getBuildVersion()
	{
		return getBuildInfo("version");
	}
	
	/**
	 * Get the text for this key. The text has no special control characters in
	 * it, and can be presented to the user.
	 * 
	 * @param key
	 *            the key to look up in the build properties file
	 * @return the text
	 */
	private static String getBuildInfo(String key) {
		String value = null;
		
		try {
			value = build.getString(key);
		} catch (MissingResourceException e) {
			System.err.println("Unable to find the entry for the key: " + key);
			return I18n.getText("???");
		};
		
		StringBuffer buf = new StringBuffer();
		
		int n = value.length();
		boolean ignore = false;
		for (int i = 0; i < n; i++) {
			char c = value.charAt(i);
			switch (c) {
				case '&' :
					continue;
				case '[' :
					ignore = true;
					break;
				case ']' :
					ignore = false;
					break;
				default :
					if (!ignore) {
						buf.append(c);
					}
			}
		}
		
		return buf.toString().trim();
	}
}
