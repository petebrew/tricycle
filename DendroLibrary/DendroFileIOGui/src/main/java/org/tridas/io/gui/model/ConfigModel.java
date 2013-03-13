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
 * Created on Jun 5, 2010, 4:15:27 PM
 */
package org.tridas.io.gui.model;

import org.tridas.io.TridasIO;
import org.tridas.io.defaults.IMetadataFieldSet;
import org.tridas.io.enums.Charsets;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.model.AbstractRevertibleModel;

/**
 * @author Daniel Murphy
 */
public class ConfigModel extends AbstractRevertibleModel {
	private static final long serialVersionUID = 1L;
	
	private String namingConvention = "Numerical";
	private String writingCharset = TridasIO.getWritingCharset();
	private String readingCharset = TridasIO.isCharsetDetection() ? Charsets.AUTO : TridasIO.getReadingCharset();;
	private String locale;
	public Boolean warnedAboutMatrixStyle = false;
	
	private IMetadataFieldSet readerDefaults = null;
	private IMetadataFieldSet writerDefaults = null;
	
	public ConfigModel() {}
	
	public void setNamingConvention(String argNamingConvention) {
		String old = namingConvention;
		namingConvention = argNamingConvention;
		firePropertyChange("namingConvention", old, namingConvention);
	}
	
	public String getNamingConvention() {
		return namingConvention;
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
	
	public void setLocale(String argLocale){
		String old = locale;
		locale = argLocale;
		firePropertyChange("locale", old, locale);
	}
	
	public String getLocale()
	{
		return locale;
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

	/**
	 * @see com.dmurph.mvc.model.AbstractRevertableModel#clone()
	 */
	@Override
	public ICloneable clone() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.dmurph.mvc.model.AbstractRevertableModel#setProperty(java.lang.String, java.lang.Object)
	 */
	protected Object setProperty(String argPropertyName, Object argValue) {
		String prop = argPropertyName;
		
		if(prop.equals("writerDefaults")){
			setWriterDefaults((IMetadataFieldSet) argValue);
		}else if(prop.equals("readerDefaults")){
			setReaderDefaults((IMetadataFieldSet) argValue);
		}else if(prop.equals("writingCharset")){
			setWritingCharset((String) argValue);
		}else if(prop.equals("readingCharset")){
			setReadingCharset((String) argValue);
		}else if(prop.equals("namingConvention")){
			setNamingConvention((String) argValue);
		}
		return null;
	}

	/**
	 * @see com.dmurph.mvc.ICloneable#cloneFrom(com.dmurph.mvc.ICloneable)
	 */
	public void cloneFrom(ICloneable argOther) {
		throw new UnsupportedOperationException();
	}
}
