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
 * Created on Jun 5, 2010, 10:13:36 PM
 */
package org.tridas.io.gui.control.config;

import java.awt.Frame;
import javax.swing.JOptionPane;

import org.apache.commons.lang.WordUtils;
import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.TricycleModelLocator;
import org.tridas.io.gui.model.popup.MetadataEditorModel;
import org.tridas.io.gui.model.popup.MetadataTableModel;
import org.tridas.io.gui.view.popup.MetadataEditor;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel Murphy
 */
public class ConfigController extends FrontController {
	public static final String SET_INPUT_FORMAT = "TRIYCYCLE_CONFIG_SET_INPUT_FORMAT";
	public static final String SET_OUTPUT_FORMAT = "TRIYCYCLE_CONFIG_SET_OUTPUT_FORMAT";
	public static final String INPUT_DEFAULTS_PRESSED = "TRIYCYCLE_CONFIG_INPUT_DEFAULTS_PRESSED";
	public static final String OUTPUT_DEFAULTS_PRESSED = "TRIYCYCLE_CONFIG_OUTPUT_DEFAULTS_PRESSED";
	public static final String SET_NAMING_CONVENTION = "TRIYCYCLE_CONFIG_SET_NAMING_CONVENTION";
	public static final String SET_READING_CHARSET = "TRIYCYCLE_CONFIG_SET_READING_CHARSET";
	public static final String SET_WRITING_CHARSET = "TRIYCYCLE_CONFIG_SET_WRITING_CHARSET";
	
	private final ConfigModel model;
	
	public ConfigController(ConfigModel argModel) {
		model = argModel;
		registerCommand(SET_INPUT_FORMAT, "setInputFormat");
		registerCommand(SET_OUTPUT_FORMAT, "setOutputFormat");
		registerCommand(SET_NAMING_CONVENTION, "setNamingConvention");
		registerCommand(SET_READING_CHARSET, "setReadingCharset");
		registerCommand(SET_WRITING_CHARSET, "setWritingCharset");
		registerCommand(INPUT_DEFAULTS_PRESSED, "displayInputDefaults");
		registerCommand(OUTPUT_DEFAULTS_PRESSED, "displayOutputDefaults");
	}
	
	public void setInputFormat(MVCEvent argEvent) {
		ConfigEvent event = (ConfigEvent) argEvent;
		model.setReaderDefaults(null);
		String xls = org.tridas.io.I18n.getText("excelmatrix.about.shortName");
		String odf = org.tridas.io.I18n.getText("odfmatrix.about.shortName");
		String csv = org.tridas.io.I18n.getText("csv.about.shortName");

		// If this is a matrix format then we may need to warn users that it's
		// not magical
		if((event.getValue().equals(xls) || 
				event.getValue().equals(odf) ||
				event.getValue().equals(csv)) && 
				model.warnedAboutMatrixStyle==false &&
				TricycleModelLocator.getInstance().isWarnAboutMatrixStyle())
		{
			Object[] options = {I18n.getText("view.popup.dontWarnAgain"), "OK"};
			
			int n  = JOptionPane.showOptionDialog(null,
				    WordUtils.wrap(I18n.getText("view.popup.warnAboutMatrixStyle"), 60),
				    I18n.getText("view.popup.spreadsheetFiles"),
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.INFORMATION_MESSAGE,
				    null,
				    options,
				    options[1]);

			if(n==0)
			{
				TricycleModelLocator.getInstance().setWarnAboutMatrixStyle(false);
			}
			
			model.warnedAboutMatrixStyle = true;
			
		}
		
		FileListModel fmodel = TricycleModelLocator.getInstance().getFileListModel();
		fmodel.setInputFormat(event.getValue());
		
	}
	
	public void setOutputFormat(MVCEvent argEvent) {
		ConfigEvent event = (ConfigEvent) argEvent;
		model.setWriterDefaults(null);
		ConvertModel cmodel = TricycleModelLocator.getInstance().getConvertModel();
		cmodel.setOutputFormat(event.getValue());
	}
	
	public void setNamingConvention(MVCEvent argEvent) {
		ConfigEvent event = (ConfigEvent) argEvent;
		model.setNamingConvention(event.getValue());
	}
	
	public void setReadingCharset(MVCEvent argEvent) {
		ConfigEvent event = (ConfigEvent) argEvent;
		model.setReadingCharset(event.getValue());
		
		if (event.getValue().equals(Charsets.AUTO)) {
			TridasIO.setReadingCharset(null);
			TridasIO.setCharsetDetection(true);
			return;
		}
		else {
			TridasIO.setCharsetDetection(false);
		}
		
		TridasIO.setReadingCharset(event.getValue());
	}
	
	public void setWritingCharset(MVCEvent argEvent) {
		ConfigEvent event = (ConfigEvent) argEvent;
		model.setWritingCharset(event.getValue());
		
		TridasIO.setWritingCharset(event.getValue());
	}
	
	public void displayInputDefaults(MVCEvent argEvent){
		
		MetadataTableModel tmodel = new MetadataTableModel();
		
		if(model.getReaderDefaults() == null){
			FileListModel fmodel = TricycleModelLocator.getInstance().getFileListModel();
			AbstractDendroFileReader reader = TridasIO.getFileReader(fmodel.getInputFormat());
			if(reader == null){
				Frame parent = TricycleModelLocator.getInstance().getMainWindow();
				JOptionPane.showMessageDialog(parent, I18n.getText("control.config.inputDefaultsNotFound", fmodel.getInputFormat()),
											  I18n.getText("general.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}else{
				model.setReaderDefaults(reader.constructDefaultMetadata());
			}
		}
		tmodel.setMetadataSet(model.getReaderDefaults());

		MetadataEditorModel model = new MetadataEditorModel(I18n.getText("view.popup.meta.readerDefault"));
		model.setTableModel(tmodel);
		
		MetadataEditor editor = new MetadataEditor(TricycleModelLocator.getInstance().getMainWindow(), model);
		editor.setVisible(true);
	}
	
	public void displayOutputDefaults(MVCEvent argEvent){
		MetadataTableModel tmodel = new MetadataTableModel();
		
		if(model.getWriterDefaults() == null){
			ConvertModel cmodel = TricycleModelLocator.getInstance().getConvertModel();
			AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(cmodel.getOutputFormat());
			if(writer == null){
				Frame parent = TricycleModelLocator.getInstance().getMainWindow();
				JOptionPane.showMessageDialog(parent, I18n.getText("control.config.outputDefaultsNotFound", cmodel.getOutputFormat()),
						  					  I18n.getText("general.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}else{
				model.setWriterDefaults(writer.constructDefaultMetadata());
			}
		}
		tmodel.setMetadataSet(model.getWriterDefaults());

		MetadataEditorModel model = new MetadataEditorModel(I18n.getText("view.popup.meta.writerDefault"));
		model.setTableModel(tmodel);
		
		MetadataEditor editor = new MetadataEditor(TricycleModelLocator.getInstance().getMainWindow(), model);
		editor.setVisible(true);
	}
}
