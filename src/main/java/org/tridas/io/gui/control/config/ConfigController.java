/**
 * Created on Jun 5, 2010, 10:13:36 PM
 */
package org.tridas.io.gui.control.config;

import org.tridas.io.gui.model.ConfigModel;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;

/**
 * @author Daniel Murphy
 *
 */
public class ConfigController extends FrontController{
	public static final String SET_INPUT_FORMAT = "CONFIG_SET_INPUT_FORMAT";
	public static final String SET_OUTPUT_FORMAT = "CONFIG_SET_OUTPUT_FORMAT";
	public static final String SET_NAMING_CONVENTION = "CONFIG_SET_NAMING_CONVENTION";
	public static final String SET_DETECT_CHARSET = "CONFIG_SET_DETECT_CHARSET";

	private ConfigModel model = ConfigModel.getInstance();
	
	public ConfigController(){
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
	
	public void setInputFormat(MVCEvent argEvent){
		// TODO
	}
	
	public void setOutputFormat(MVCEvent argEvent){
		// TODO
	}
	
	public void setNamingConvention(MVCEvent argEvent){
		// TODO
	}
	
	public void setDetectCharset(MVCEvent argEvent){
		// TODO
	}
}
