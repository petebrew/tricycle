/**
 * Created on Jun 5, 2010, 10:13:36 PM
 */
package org.tridas.io.gui.control.config;

import java.awt.Frame;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import org.tridas.io.AbstractDendroCollectionWriter;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.defaults.AbstractDefaultValue;
import org.tridas.io.gui.I18n;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.model.ConfigModel;
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
		try {
			registerCommand(SET_INPUT_FORMAT, "setInputFormat");
			registerCommand(SET_OUTPUT_FORMAT, "setOutputFormat");
			registerCommand(SET_NAMING_CONVENTION, "setNamingConvention");
			registerCommand(SET_READING_CHARSET, "setReadingCharset");
			registerCommand(SET_WRITING_CHARSET, "setWritingCharset");
			registerCommand(INPUT_DEFAULTS_PRESSED, "displayInputDefaults");
			registerCommand(OUTPUT_DEFAULTS_PRESSED, "displayOutputDefaults");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void setInputFormat(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
		model.setReaderDefaults(null);
		model.setInputFormat(event.getValue());
	}
	
	public void setOutputFormat(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
		model.setWriterDefaults(null);
		model.setOutputFormat(event.getValue());
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
			AbstractDendroFileReader reader = TridasIO.getFileReader(model.getInputFormat());
			if(reader == null){
				Frame parent = ModelLocator.getInstance().getMainWindow();
				JOptionPane.showMessageDialog(parent, "Could not find defaults for input format '"+model.getInputFormat()+"'.",
											  "Error", JOptionPane.ERROR_MESSAGE);
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
			AbstractDendroCollectionWriter writer = TridasIO.getFileWriter(model.getOutputFormat());
			if(writer == null){
				Frame parent = ModelLocator.getInstance().getMainWindow();
				JOptionPane.showMessageDialog(parent, "Could not find defaults for output format '"+model.getOutputFormat()+"'.",
											  "Error", JOptionPane.ERROR_MESSAGE);
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
