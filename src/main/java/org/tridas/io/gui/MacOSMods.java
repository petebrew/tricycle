package org.tridas.io.gui;

import java.awt.Desktop;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;

import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.view.popup.AboutWindow;
import org.tridas.io.gui.view.popup.OptionsWindow;

/**
 * Modifications required for Mac look and feel.
 */
public class MacOSMods {

	public MacOSMods() {
		if (!Desktop.isDesktopSupported()) {
			return;
		}

		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
			desktop.setAboutHandler(new TricycleAboutHandler());
		}
		if (desktop.isSupported(Desktop.Action.APP_PREFERENCES)) {
			desktop.setPreferencesHandler(new TricyclePreferencesHandler());
		}
	}

	private static final class TricycleAboutHandler implements AboutHandler {
		@Override
		public void handleAbout(AboutEvent event) {
			new AboutWindow(null).setVisible(true);
		}
	}

	private static final class TricyclePreferencesHandler implements PreferencesHandler {
		@Override
		public void handlePreferences(PreferencesEvent event) {
			new OptionsWindow(
				TricycleModelLocator.getInstance().getMainWindow(),
				TricycleModelLocator.getInstance().getConfigModel()
			).setVisible(true);
		}
	}
}
