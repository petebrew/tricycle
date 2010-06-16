/**
 * Created on May 27, 2010, 2:40:21 AM
 */
package org.tridas.io.gui.control.convert;

import org.tridas.io.defaults.IMetadataFieldSet;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class ConvertEvent extends MVCEvent {
	
	private final String inputFormat;
	private final String namingConvention;
	private final String outputFormat;
	private final IMetadataFieldSet readerDefaults;
	private final IMetadataFieldSet writerDefaults;
	
	public ConvertEvent(String argInputFormat, String argOutputFormat, String argNamingConvention,
						IMetadataFieldSet argReaderDefaults, IMetadataFieldSet argWriterDefaults) {
		super(ConvertController.CONVERT);
		namingConvention = argNamingConvention;
		outputFormat = argOutputFormat;
		inputFormat = argInputFormat;
		readerDefaults = argReaderDefaults;
		writerDefaults = argWriterDefaults;
	}
	
	public String getNamingConvention() {
		return namingConvention;
	}
	
	public String getOutputFormat() {
		return outputFormat;
	}

	public String getInputFormat() {
		return inputFormat;
	}

	public IMetadataFieldSet getReaderDefaults() {
		return readerDefaults;
	}

	public IMetadataFieldSet getWriterDefaults() {
		return writerDefaults;
	}
}
