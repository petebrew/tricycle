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
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

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
				//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				
				 for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				
			} catch (Exception e) {
				e.printStackTrace();
				//log.warn(I18n.getText("lookfeel.nimbus"));
				
			}
		}
		else
		{
			// On MacOSX set standard GUI conventions...
			
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TRiCYCLE");
			System.setProperty("com.apple.macos.use-file-dialog-packages", "false"); // for AWT
			System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
			UIManager.put("JFileChooser.packageIsTraversable", "never"); // for swing
			new MacOSMods();
			
		}
		
		// Set tracking key
		AnalyticsConfigData config = new AnalyticsConfigData(TricycleModel.ANALYTICS_CODE);
		JGoogleAnalyticsTracker t = new JGoogleAnalyticsTracker(config, GoogleAnalyticsVersion.V_4_7_2);
		MVC.setTracker(t);
		//MVC.showEventMonitor();
		t.setEnabled(model.getTricycleModel().isTracking());
		MVC.getTracker().trackPageView(getBuildRevision(), "Startup", "tridas.org");
		
		
		// Set Swing widget internationalisation
		internationliseSwingWidgets();

		(new StartupEvent(true)).dispatch();
	}
	
	
	static {
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle("build");
		} catch (MissingResourceException mre) {
			log.error("Could not find build properties file containing build timestamp, version, revision etc.");
			//mre.printStackTrace();
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
		return getBuildInfo("buildNumber");
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
			log.error("Unable to find build info: " + key);
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
	
	/**
	 * Globally internationalise any swing widget text
	 */
	private static void internationliseSwingWidgets()
	{
		UIManager.put("FileChooser.cancelButtonText", I18n.getText("general.cancel"));
		UIManager.put("FileChooser.saveButtonText", I18n.getText("general.save"));
		UIManager.put("FileChooser.openButtonText", I18n.getText("general.open"));
		UIManager.put("FileChooser.lookInLabelText", I18n.getText("view.popup.filechooser.lookIn")+":");
		UIManager.put("FileChooser.fileNameLabelText", I18n.getText("view.popup.filechooser.fileName"));
		UIManager.put("FileChooser.filesOfTypeLabelText", I18n.getText("view.popup.filechooser.fileType"));
	}
}
