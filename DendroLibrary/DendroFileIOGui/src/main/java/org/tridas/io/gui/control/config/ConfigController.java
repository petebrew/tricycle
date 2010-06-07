/**
 * Created on Jun 5, 2010, 10:13:36 PM
 */
package org.tridas.io.gui.control.config;

import org.tridas.io.TridasIO;
import org.tridas.io.gui.model.ConfigModel;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.StringEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel Murphy
 */
public class ConfigController extends FrontController {
	public static final String SET_INPUT_FORMAT = "CONFIG_SET_INPUT_FORMAT";
	public static final String SET_OUTPUT_FORMAT = "CONFIG_SET_OUTPUT_FORMAT";
	public static final String SET_NAMING_CONVENTION = "CONFIG_SET_NAMING_CONVENTION";
	public static final String SET_DETECT_CHARSET = "CONFIG_SET_DETECT_CHARSET";
	
	private ConfigModel model = ConfigModel.getInstance();
	
	public ConfigController() {
		try {
			registerCommand(SET_INPUT_FORMAT, "setInputFormat");
			registerCommand(SET_OUTPUT_FORMAT, "setOutputFormat");
			registerCommand(SET_NAMING_CONVENTION, "setNamingConvention");
			registerCommand(SET_DETECT_CHARSET, "setDetectCharset");
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
	
	@SuppressWarnings("unchecked")
	public void setDetectCharset(MVCEvent argEvent) {
		ObjectEvent<Boolean> event = (ObjectEvent<Boolean>) argEvent;
		model.setDetectCharset(event.getValue());
		
		TridasIO.setCharsetDetection(event.getValue());
	}
}
