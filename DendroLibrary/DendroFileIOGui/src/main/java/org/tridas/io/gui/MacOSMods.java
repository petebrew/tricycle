package org.tridas.io.gui;

import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.view.popup.AboutWindow;
import org.tridas.io.gui.view.popup.OptionsWindow;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

/**
 * Modifications required for Mac look and feel
 * 
 * @author pwb48
 *
 */
public class MacOSMods extends Application {

	@SuppressWarnings("deprecation")
	public MacOSMods(){
		addApplicationListener(new AboutBoxHandler());
		
	}
	
	class AboutBoxHandler extends ApplicationAdapter{
		public void handleAbout(ApplicationEvent event)
		{
			new AboutWindow(null).setVisible(true);
			
		}
	}
	
	class PreferencesHandler extends ApplicationAdapter{
		public void handlePreferences(ApplicationEvent event)
		{
			new OptionsWindow(TricycleModelLocator.getInstance().getMainWindow(), TricycleModelLocator.getInstance().getConfigModel()).setVisible(true);
			
		}
	}
}
