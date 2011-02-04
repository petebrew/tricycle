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

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import org.apache.commons.lang.StringUtils;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.PreviewModel;
import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.gui.view.popup.AboutWindow;
import org.tridas.io.gui.view.popup.OptionsWindow;
import org.tridas.io.gui.view.popup.PreviewWindow;
import org.tridas.io.util.FileHelper;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

public class TricycleController extends FrontController {
	public static final String STARTUP = "TRIYCYCLE_STARTUP_EVENT";
	public static final String QUIT = "TRIYCYCLE_QUIT";
	public static final String ABOUT = "TRIYCYCLE_ABOUT";
	public static final String HELPVIEWER = "TRIYCYCLE_HELP";

	public static final String OPTIONS = "TRIYCYCLE_OPTIONS";
	public static final String VIEW_LOG = "TRIYCYCLE_VIEW_LOG";
	
	private MainWindow view = null;
	private AboutWindow about = null;
	private OptionsWindow options = null;
	
	public TricycleController() {
		registerCommand(STARTUP, "startup");
		registerCommand(QUIT, "quit");
		registerCommand(ABOUT, "about");
		registerCommand(OPTIONS, "options");
		registerCommand(VIEW_LOG, "log");
		registerCommand(HELPVIEWER, "helpViewer");
	}
	
	public void startup(MVCEvent argEvent) {
		if(view == null){
			view = new MainWindow(TricycleModelLocator.getInstance().getTricycleModel());
			TricycleModelLocator.getInstance().setMainWindow(view);
			if(argEvent instanceof StartupEvent){
				if(((StartupEvent) argEvent).exitOnClose){
					view.setDefaultCloseOperation(3);
				}else{
					view.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				}
			}
			view.setVisible(true);
			ToolTipManager.sharedInstance().setDismissDelay(10000);
		}else{
			view.setVisible(true);
		}
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
		if(about == null){
			about = new AboutWindow(TricycleModelLocator.getInstance().getMainWindow());
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
		if(options == null){
			options = new OptionsWindow(TricycleModelLocator.getInstance().getMainWindow(), TricycleModelLocator.getInstance().getConfigModel());
		}
		options.setVisible(true);
	}
	
	public void log(MVCEvent argEvent){
		PreviewModel pmodel = new PreviewModel();
		pmodel.setFilename("TRiCYCLE.log");
		
		FileHelper fh = new FileHelper();
		pmodel.setFileString(StringUtils.join(fh.loadStrings("~/.TRiCYCLE.log"), "\n"));
		
		MainWindow window = TricycleModelLocator.getInstance().getMainWindow();
		Dimension size = window.getSize();
		
		PreviewWindow preview = new PreviewWindow(TricycleModelLocator.getInstance().getMainWindow(), size.width - 40, size.height - 40, pmodel);
		preview.setVisible(true);
	}
	
	public void helpViewer(MVCEvent argEvent){
		

		
	}
}
