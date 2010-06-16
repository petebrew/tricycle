/**
 * Created on Jun 5, 2010, 4:15:27 PM
 */
package org.tridas.io.gui.model;

import org.tridas.io.TridasIO;
import org.tridas.io.defaults.IMetadataFieldSet;
import org.tridas.io.gui.enums.Charsets;
import org.tridas.io.gui.enums.InputFormat;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author Daniel Murphy
 */
public class ConfigModel extends AbstractModel {
	
	private static final ConfigModel model = new ConfigModel();
	
	private String inputFormat = InputFormat.AUTO;
	private String outputFormat = "TRiDaS";
	private String namingConvention = "Numerical";
	private String writingCharset = TridasIO.getWritingCharset();
	private String readingCharset = TridasIO.isCharsetDetection() ? Charsets.AUTO : TridasIO.getReadingCharset();;
	
	private IMetadataFieldSet readerDefaults = null;
	private IMetadataFieldSet writerDefaults = null;
	
	private ConfigModel() {}
	
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
	
	public void setNamingConvention(String argNamingConvention) {
		String old = namingConvention;
		namingConvention = argNamingConvention;
		firePropertyChange("namingConvention", old, namingConvention);
	}
	
	public String getNamingConvention() {
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
	 * public void setHideWarnings(boolean argHideWarnings) {
	 * boolean old = hideWarnings;
	 * hideWarnings = argHideWarnings;
	 * firePropertyChange("hideWarnings", old, hideWarnings);
	 * }
	 * /**
	 * @return the hideWarnings
	 * public boolean isHideWarnings() {
	 * return hideWarnings;
	 * }
	 */

	public void setWritingCharset(String argCharset) {
		String old = writingCharset;
		writingCharset = argCharset;
		firePropertyChange("writingCharset", old, writingCharset);
	}
	
	public String getWritingCharset() {
		return writingCharset;
	}
	
	/**
	 * @param argReadingCharset
	 *            the readingCharset to set
	 */
	public void setReadingCharset(String argReadingCharset) {
		String old = readingCharset;
		readingCharset = argReadingCharset;
		firePropertyChange("readingCharset", old, readingCharset);
	}
	
	/**
	 * @return the readingCharset
	 */
	public String getReadingCharset() {
		return readingCharset;
	}
	
	public void setReaderDefaults(IMetadataFieldSet argReaderDefaults) {
		IMetadataFieldSet old = readerDefaults;
		readerDefaults = argReaderDefaults;
		firePropertyChange("readerDefaults", old, readerDefaults);
	}
	
	public IMetadataFieldSet getReaderDefaults() {
		return readerDefaults;
	}

	public void setWriterDefaults(IMetadataFieldSet argWriterDefaults) {
		IMetadataFieldSet old = writerDefaults;
		writerDefaults = argWriterDefaults;
		firePropertyChange("writerDefaults", old, writerDefaults);
	}

	public IMetadataFieldSet getWriterDefaults() {
		return writerDefaults;
	}
	
	public static final ConfigModel getInstance() {
		return model;
	}
}
