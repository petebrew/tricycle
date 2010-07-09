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

import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.model.ConfigModel;
import org.tridas.io.gui.model.ConvertModel;
import org.tridas.io.gui.model.FileListModel;
import org.tridas.io.gui.model.ModelLocator;
import org.tridas.io.gui.model.popup.MetadataEditorModel;
import org.tridas.io.gui.model.popup.MetadataTableModel;
import org.tridas.io.gui.view.popup.MetadataEditor;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.StringEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel Murphy
 */
public class ConfigController extends FrontController {
	public static final String SET_INPUT_FORMAT = "CONFIG_SET_INPUT_FORMAT";
	public static final String SET_OUTPUT_FORMAT = "CONFIG_SET_OUTPUT_FORMAT";
	public static final String INPUT_DEFAULTS_PRESSED = "CONFIG_INPUT_DEFAULTS_PRESSED";
	public static final String OUTPUT_DEFAULTS_PRESSED = "CONFIG_OUTPUT_DEFAULTS_PRESSED";
	public static final String SET_NAMING_CONVENTION = "CONFIG_SET_NAMING_CONVENTION";
	public static final String SET_READING_CHARSET = "CONFIG_SET_READING_CHARSET";
	public static final String SET_WRITING_CHARSET = "CONFIG_SET_WRITING_CHARSET";
	
	private ConfigModel model = ConfigModel.getInstance();
	
	public ConfigController() {
		registerCommand(SET_INPUT_FORMAT, "setInputFormat");
		registerCommand(SET_OUTPUT_FORMAT, "setOutputFormat");
		registerCommand(SET_NAMING_CONVENTION, "setNamingConvention");
		registerCommand(SET_READING_CHARSET, "setReadingCharset");
		registerCommand(SET_WRITING_CHARSET, "setWritingCharset");
		registerCommand(INPUT_DEFAULTS_PRESSED, "displayInputDefaults");
		registerCommand(OUTPUT_DEFAULTS_PRESSED, "displayOutputDefaults");
	}
	
	public void setInputFormat(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
		model.setReaderDefaults(null);
		FileListModel fmodel = FileListModel.getInstance();
		fmodel.setInputFormat(event.getValue());
	}
	
	public void setOutputFormat(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
		model.setWriterDefaults(null);
		ConvertModel cmodel = ConvertModel.getInstance();
		cmodel.setOutputFormat(event.getValue());
	}
	
	public void setNamingConvention(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
		model.setNamingConvention(event.getValue());
	}
	
	public void setReadingCharset(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
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
		StringEvent event = (StringEvent) argEvent;
		model.setWritingCharset(event.getValue());
		
		TridasIO.setWritingCharset(event.getValue());
	}
	
	public void displayInputDefaults(MVCEvent argEvent){
		
		MetadataTableModel tmodel = new MetadataTableModel();
		
		if(model.getReaderDefaults() == null){
			FileListModel fmodel = FileListModel.getInstance();
			AbstractDendroFileReader reader = TridasIO.getFileReader(fmodel.getInputFormat());
			if(reader == null){
				Frame parent = ModelLocator.getInstance().getMainWindow();
				JOptionPane.showMessageDialog(parent, I18n.getText("control.config.inputDefaultsNotFound", fmodel.getInputFormat()),
											  I18n.getText("control.config.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}else{
				model.setReaderDefaults(reader.constructDefaultMetadata());
			}
		}
		tmodel.setMetadataSet(model.getReaderDefaults());

		MetadataEditorModel model = new MetadataEditorModel(I18n.getText("view.popup.meta.readerDefault"));
		model.setTableModel(tmodel);
		
		MetadataEditor editor = new MetadataEditor(ModelLocator.getInstance().getMainWindow(), model);
		editor.setVisible(true);
	}
	
	public void displayOutputDefaults(MVCEvent argEvent){
		MetadataTableModel tmodel = new MetadataTableModel();
		
		if(model.getWriterDefaults() == null){
			ConvertModel cmodel = ConvertModel.getInstance();
			AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(cmodel.getOutputFormat());
			if(writer == null){
				Frame parent = ModelLocator.getInstance().getMainWindow();
				JOptionPane.showMessageDialog(parent, I18n.getText("control.config.outputDefaultsNotFound", cmodel.getOutputFormat()),
						  					  I18n.getText("control.config.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}else{
				model.setWriterDefaults(writer.constructDefaultMetadata());
			}
		}
		tmodel.setMetadataSet(model.getWriterDefaults());

		MetadataEditorModel model = new MetadataEditorModel(I18n.getText("view.popup.meta.writerDefault"));
		model.setTableModel(tmodel);
		
		MetadataEditor editor = new MetadataEditor(ModelLocator.getInstance().getMainWindow(), model);
		editor.setVisible(true);
	}
}
