/**
 * Created on Jun 5, 2010, 4:15:27 PM
 */
package org.tridas.io.gui.model;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author Daniel Murphy
 *
 */
public class ConfigModel extends AbstractModel {

	private static final ConfigModel model = new ConfigModel();
	
	private String inputFormat = "automatic";
	private String outputFormat = "tridas";
	private String namingConvention = "UUID";
	private boolean detectCharset = false;
	
	private ConfigModel(){}
	
	/**
	 * @param inputFormat
	 *            the inputFormat to set
	 */
	public void setInputFormat(String argInputFormat) {
		String old = inputFormat;
		inputFormat = argInputFormat;
		firePropertyChange("inputFormat", old, inputFormat);
	}

	/**
	 * @return the inputFormat
	 */
	public String getInputFormat() {
		return inputFormat;
	}
	
	public void setNamingConvention(String argNamingConvention){
		String old = namingConvention;
		namingConvention = argNamingConvention;
		firePropertyChange("namingConvention", old, namingConvention);
	}
	
	public String getNamingConvention(){
		return namingConvention;
	}
	
	/**
	 * @param outputFormat
	 *            the outputFormat to set
	 */
	public void setOutputFormat(String argOutputFormat) {
		String old = outputFormat;
		outputFormat = argOutputFormat;
		firePropertyChange("outputFormat", old, outputFormat);
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}
	
	/*
	 * @param argHideWarnings the hideWarnings to set
	 *
	public void setHideWarnings(boolean argHideWarnings) {
		boolean old = hideWarnings;
		hideWarnings = argHideWarnings;
		firePropertyChange("hideWarnings", old, hideWarnings);
	}

	/**
	 * @return the hideWarnings
	 *
	public boolean isHideWarnings() {
		return hideWarnings;
	}*/

	/**
	 * @param detectCharset the detectCharset to set
	 */
	public void setDetectCharset(boolean argDetectCharset) {
		boolean old = detectCharset;
		detectCharset = argDetectCharset;
		firePropertyChange("detectCharset", old, detectCharset);
	}

	/**
	 * @return the detectCharset
	 */
	public boolean isDetectCharset() {
		return detectCharset;
	}

	public static final ConfigModel getInstance(){
		return model;
	}
}
