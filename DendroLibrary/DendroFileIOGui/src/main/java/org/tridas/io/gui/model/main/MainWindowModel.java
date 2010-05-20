/**
 * Created at May 20, 2010, 12:47:10 AM
 */
package org.tridas.io.gui.model.main;

import java.io.File;

import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.mvc.model.AbstractModel;
import org.tridas.io.gui.mvc.model.CloneableArrayList;

/**
 * @author daniel
 *
 */
public class MainWindowModel extends AbstractModel {
	
	private static final SimpleLogger log = new SimpleLogger(MainWindowModel.class);
	
	private String outputFormat = null;	
	private String inputFormat = null;
	
	private String namingConvention = null;
	
	private CloneableArrayList<File> inputFiles = new CloneableArrayList<File>();
	private CloneableArrayList<File> outputFiles = new CloneableArrayList<File>();
	
	

	/**
	 * @param outputFormat the outputFormat to set
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
	
	
	/**
	 * @param inputFormat the inputFormat to set
	 */
	public void setInputFormat(String argInputFormat) {
		String old = inputFormat;
		inputFormat = argInputFormat;
		firePropertyChange("inputFormat", old, argInputFormat);
	}

	/**
	 * @return the inputFormat
	 */
	public String getInputFormat() {
		return inputFormat;
	}
	
	/**
	 * @param namingConvention the namingConvention to set
	 */
	public void setNamingConvention(String argNamingConvention) {
		String old = namingConvention;
		namingConvention = argNamingConvention;
		firePropertyChange("namingConvention", old, namingConvention);
	}

	/**
	 * @return the namingConvention
	 */
	public String getNamingConvention() {
		return namingConvention;
	}

	/**
	 * @see org.tridas.io.gui.mvc.model.AbstractModel#revert()
	 */
	@Override
	protected void revert() {
		log.info("revert does nothing, this isn't a saving model");
	}

	/**
	 * @see org.tridas.io.gui.mvc.model.AbstractModel#save()
	 */
	@Override
	protected void save() {
		log.info("save does nothing, this isn't a saving model");
	}
}
