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
 * Created on May 27, 2010, 2:40:21 AM
 */
package org.tridas.io.gui.control.convert;

import org.tridas.io.defaults.IMetadataFieldSet;
import org.tridas.io.gui.model.ConvertModel.TreatFilesAsOption;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.tracking.ITrackable;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class ConvertEvent extends MVCEvent implements ITrackable{
	
	private final String inputFormat;
	private final String namingConvention;
	private final String outputFormat;
	private final TreatFilesAsOption treatFilesAs;
	private final IMetadataFieldSet readerDefaults;
	private final IMetadataFieldSet writerDefaults;
	
	public ConvertEvent(String argInputFormat, String argOutputFormat, String argNamingConvention, TreatFilesAsOption argTreatFilesAs,
						IMetadataFieldSet argReaderDefaults, IMetadataFieldSet argWriterDefaults) {
		super(ConvertController.CONVERT);
		namingConvention = argNamingConvention;
		outputFormat = argOutputFormat;
		inputFormat = argInputFormat;
		treatFilesAs = argTreatFilesAs;
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
	
	public TreatFilesAsOption getTreatFilesAs()
	{
		return treatFilesAs;
	}

	public IMetadataFieldSet getReaderDefaults() {
		return readerDefaults;
	}

	public IMetadataFieldSet getWriterDefaults() {
		return writerDefaults;
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
	 */
	@Override
	public String getTrackingCategory() {
		return "Tricycle";
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return "Convert";
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
	 */
	@Override
	public String getTrackingLabel() {
		return inputFormat +" > "+outputFormat;
	}

	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
