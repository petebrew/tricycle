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
package org.tridas.io.gui.control;

import org.tridas.io.gui.model.ModelLocator;
import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.gui.view.popup.AboutWindow;
import org.tridas.io.gui.view.popup.OptionsWindow;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

public class MainWindowController extends FrontController {
	public static final String STARTUP = "STARTUP_EVENT";
	public static final String QUIT = "MAIN_WINDOW_QUIT";
	public static final String ABOUT = "MAIN_WINDOW_ABOUT";
	public static final String OPTIONS = "MAIN_WINDOW_OPTIONS";
	
	private MainWindow view = null;
	private AboutWindow about = null;
	private OptionsWindow options = null;
	
	public MainWindowController() {
		try {
			registerCommand(STARTUP, "startup");
			registerCommand(QUIT, "quit");
			registerCommand(ABOUT, "about");
			registerCommand(OPTIONS, "options");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void startup(MVCEvent argEvent) {
		view = new MainWindow();
		ModelLocator.getInstance().setMainWindow(view);
		view.setDefaultCloseOperation(3);
		view.setVisible(true);
		about = new AboutWindow(view);
		options = new OptionsWindow(view);
	}
	
	public void quit(MVCEvent argEvent) {
		System.exit(0);
	}
	
	public void about(MVCEvent argEvent) {
		try {
			MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
			e.printStackTrace();
		} catch (IncorrectThreadException e) {
			e.printStackTrace();
		}
		about.setVisible(true);
	}
	
	public void options(MVCEvent argEvent){
		try {
			MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
			e.printStackTrace();
		} catch (IncorrectThreadException e) {
			e.printStackTrace();
		}
		options.setVisible(true);
	}
}
