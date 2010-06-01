/**
 * Created on May 27, 2010, 2:40:21 AM
 */
package org.tridas.io.gui.control.main.convert;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 *
 */
public class ConvertEvent extends MVCEvent {

	private final String namingConvention;
	private final String outputFormat;
	
	public ConvertEvent(String argOutputFormat, String argNamingConvention){
		super(ConvertController.CONVERT);
		namingConvention = argNamingConvention;
		outputFormat = argOutputFormat;
	}

	/**
	 * @return the namingConvention
	 */
	public String getNamingConvention() {
		return namingConvention;
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}
	
	
}
