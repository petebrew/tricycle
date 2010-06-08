/**
 * Created on Jun 5, 2010, 10:13:36 PM
 */
package org.tridas.io.gui.control.config;

import org.tridas.io.TridasIO;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.model.ConfigModel;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.StringEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel Murphy
 */
public class ConfigController extends FrontController {
	public static final String SET_INPUT_FORMAT = "CONFIG_SET_INPUT_FORMAT";
	public static final String SET_OUTPUT_FORMAT = "CONFIG_SET_OUTPUT_FORMAT";
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
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void setInputFormat(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
		model.setInputFormat(event.getValue());
	}
	
	public void setOutputFormat(MVCEvent argEvent) {
		StringEvent event = (StringEvent) argEvent;
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
}
