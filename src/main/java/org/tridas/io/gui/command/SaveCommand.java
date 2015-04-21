/*******************************************************************************
 * Copyright 2011 Daniel Murphy and Peter Brewer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.tridas.io.gui.command;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.IDendroFile;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.TricycleModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.OverwriteModel;
import org.tridas.io.gui.model.popup.SavingDialogModel;
import org.tridas.io.gui.model.popup.OverwriteModel.Response;
import org.tridas.io.gui.view.popup.OverwritePopup;
import org.tridas.io.gui.view.popup.SavingProgress;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

/**
 * @author Daniel
 *
 */
public class SaveCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(SaveCommand.class);

	@SuppressWarnings("unused")
	public synchronized void execute(MVCEvent argEvent) {
		
		try {
			MVC.splitOff();
		} catch (IllegalThreadException e1) {
			e1.printStackTrace();
		} catch (IncorrectThreadException e1) {
			e1.printStackTrace();
		}
		
		TricycleModelLocator loc = TricycleModelLocator.getInstance();
		
		TricycleModel mwm = loc.getTricycleModel();
		SavingProgress storedSavingProgress = null;
		
		ConvertModel cmodel = loc.getConvertModel();
		// surround it all with try, so no matter what happens we will close the saving
		// dialog and
		// unlock the gui
		try {
			
			File folder = null;
			JFileChooser fd = new JFileChooser();
			fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fd.setMultiSelectionEnabled(false);
			File lastDirectory = TricycleModelLocator.getInstance().getLastDirectoryWrite();
			fd.setCurrentDirectory(lastDirectory);
			
			
			int retValue = fd.showSaveDialog(TricycleModelLocator.getInstance().getMainWindow());
			if (retValue == JFileChooser.APPROVE_OPTION) {
				folder = fd.getSelectedFile();
				TricycleModelLocator.getInstance().setLastDirectoryWrite(folder);
			}
			else {
				return;
			}
			
			SavingDialogModel model = new SavingDialogModel();
			model.setSavingFilename("");
			model.setSavingPercent(0);
			
			int totalFiles = 0;
			for (ConvertModel.ReaderWriterObject p : cmodel.getConvertedList()) {
				if (p.writer == null) {
					continue;
				}
				for (IDendroFile f : p.writer.getFiles()) {
					totalFiles++;
				}
			}
			if (totalFiles == 0) {
				JOptionPane.showMessageDialog(null, I18n.getText("control.convert.noFiles"), "",
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			
			mwm.setLock(true);
			
			int currFile = 0;
			final SavingProgress savingProgress = new SavingProgress(
					TricycleModelLocator.getInstance().getMainWindow(), model);
			storedSavingProgress = savingProgress; // so we can shut it down at the end
			
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					savingProgress.setVisible(true);
				}
			});
			while(!savingProgress.isVisible()){
				Thread.sleep(100);
			}
			
			// waiting for saving dialog window to become visible
			int slept = 0;
			while (!savingProgress.isVisible()) {
				try {
					Thread.sleep(50); // for bug 213
				} catch (InterruptedException e) {}
				slept += 50;
				if (slept >= 5000) {
					log.error("Slept for 5 seconds but saving progress window is still not open. breaking");
					break;
				}
			}
			
			// start the saving loop
			cmodel.setSaveRunning(true);
			Response response = null;
			boolean all = false;
			for (int i = 0; i < cmodel.getConvertedList().size(); i++) {
				if (!cmodel.isSaveRunning()) {
					break;
				}
				ConvertModel.ReaderWriterObject p = cmodel.getConvertedList().get(i);
				if (p.writer != null) {
					
					String outputFolder = folder.getAbsolutePath();
					// custom implementation of saveAllToDisk, as we need to
					// keep track of each dendro file for the progress window
					
					if (!outputFolder.endsWith(File.separator) && !outputFolder.equals("")) {
						outputFolder += File.separator;
					}
					
					IDendroFile[] files = p.writer.getFiles();
					for (int j = 0; j < files.length; j++) {
						IDendroFile dof = files[j];
						if (!cmodel.isSaveRunning()) {
							break;
						}
						currFile++;
						String filename = p.writer.getNamingConvention().getFilename(dof);
						
						model.setSavingFilename(filename + "." + dof.getExtension());
						
						// check to see if it exists:
						File file = new File(outputFolder + File.separator + filename + "." + dof.getExtension());
						if (file.exists()) {
							if (response == null) {
								OverwriteModel om = new OverwriteModel();
								om.setAll(false);
								om.setMessage(I18n.getText("control.convert.overwrite", filename, filename + "(1)"));
								OverwritePopup pop = new OverwritePopup(om, TricycleModelLocator.getInstance().getMainWindow());
								// this should hang until the window is closed
								pop.setVisible(true);
								
								response = om.getResponse();
								all = om.isAll();
								if (response == null) {
									log.error("response is null");
									j--;
									currFile--;
									continue;
								}
							}
							
							switch (response) {
								case IGNORE :
									response = null;
									continue;
								case OVERWRITE :
									p.writer.saveFileToDisk(outputFolder, dof);
									break;
								case RENAME :
									p.writer.getNamingConvention().setFilename(dof, filename + "(1)");
									j--;
									currFile--;
									response = null;
									continue;
							}
							if (!all) {
								response = null;
							}
						}
						p.writer.saveFileToDisk(outputFolder, dof);
						model.setSavingPercent(currFile * 100 / totalFiles);
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception thrown while saving.", e);
			throw new RuntimeException(e);
		} finally {
			if (storedSavingProgress != null) {
				storedSavingProgress.setVisible(false);
			}
			mwm.setLock(false);
		}
	}
}
