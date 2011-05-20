package org.tridas.io.gui;

import org.tridas.io.gui.view.popup.AboutWindow;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

public class MacOSAboutHandler extends Application {

	public MacOSAboutHandler(){
		addApplicationListener(new AboutBoxHandler());
		
	}
	
	class AboutBoxHandler extends ApplicationAdapter{
		public void handleAbout(ApplicationEvent event)
		{
			new AboutWindow(null).setVisible(true);
			
		}
	}
}
