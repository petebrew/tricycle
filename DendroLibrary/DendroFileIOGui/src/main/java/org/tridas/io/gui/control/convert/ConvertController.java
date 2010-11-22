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
/**
 * Created on May 27, 2010, 2:37:40 AM
 */
package org.tridas.io.gui.control.convert;

import java.awt.Dimension;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;
import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;
import org.tridas.io.formats.tridas.TridasFile;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.command.ConvertCommand;
import org.tridas.io.gui.command.ConvertCommand.DendroWrapper;
import org.tridas.io.gui.command.SaveCommand;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.PreviewModel;
import org.tridas.io.gui.util.XMLFormatter;
import org.tridas.io.gui.view.MainWindow;
import org.tridas.io.gui.view.popup.PreviewWindow;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel Murphy
 */
public class ConvertController extends FrontController {
	private static final SimpleLogger log = new SimpleLogger(ConvertController.class);
	
	public static final String SAVE = "TRIYCYCLE_CONVERT_SAVE";
	public static final String CONVERT = "TRIYCYCLE_CONVERT_CONVERT";
	public static final String PREVIEW = "TRIYCYCLE_CONVERT_PREVIEW";
	public static final String CANCEL_CONVERT = "TRIYCYCLE_CONVERT_CANCEL_CONVERT";
	public static final String CANCEL_SAVE = "TRIYCYCLE_CONVERT_CANCEL_SAVE";
	
	
	public ConvertController() {
		registerCommand(PREVIEW, "preview");
		registerCommand(CONVERT, ConvertCommand.class);
		registerCommand(SAVE, SaveCommand.class);
		registerCommand(CANCEL_CONVERT, "cancelConvert");
		registerCommand(CANCEL_SAVE, "cancelSave");
	}
	
	public void cancelConvert(MVCEvent argEvent) {
		ConvertModel.getInstance().setConvertRunning(false);
	}
	
	public void cancelSave(MVCEvent argEvent) {
		ConvertModel.getInstance().setSaveRunning(false);
	}
	
	@SuppressWarnings("unchecked")
	public void preview(MVCEvent argEvent) {
		ObjectEvent<DefaultMutableTreeNode> event = (ObjectEvent<DefaultMutableTreeNode>) argEvent;
		
		DefaultMutableTreeNode node = event.getValue();
		
		if (node.getUserObject() == null) {
			return;
		}
		if (node.getUserObject() instanceof DendroWrapper) {
			DendroWrapper file = (DendroWrapper) node.getUserObject();
			
			PreviewModel pmodel = new PreviewModel();
			pmodel.setFilename(file.toString());
			
			boolean worked = false;
			String[] strings = null;
			try {
				strings = file.file.saveToString();
				worked = strings != null;
			} catch (Exception e) {
				log.error("Could not convert file '"+file.toString()+"' to strings");
				log.dbe(DebugLevel.L2_ERROR, e);
				worked = false;
			}
			
			if (worked) {
				String all = StringUtils.join(strings, "\n");
				if(file.file instanceof TridasFile){
					try{
						all = XMLFormatter.format(all);
					}catch(Exception e){
						log.error("Error formatting xml text: "+all);
					}
				}
				pmodel.setFileString(all);
			}
			else {
				pmodel.setFileString(I18n.getText("view.popup.preview.error"));
			}
			
			MainWindow window = TricycleModelLocator.getInstance().getMainWindow();
			Dimension size = window.getSize();
			
			PreviewWindow preview = new PreviewWindow(window, size.width - 40, size.height - 40, pmodel);
			preview.setVisible(true);
			preview.toFront();
		}
	}
}
