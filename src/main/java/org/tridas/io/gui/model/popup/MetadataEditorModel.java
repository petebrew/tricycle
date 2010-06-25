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
 * Created at Jun 14, 2010, 3:12:20 PM
 */
package org.tridas.io.gui.model.popup;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author daniel
 *
 */
public class MetadataEditorModel extends AbstractModel{
	
	private MetadataTableModel tableModel = null;
	
	private String filename = null;
	
	public MetadataEditorModel(String argFilename){
		filename = argFilename;
	}
	
	public void setFilename(String argFilename){
		String old = filename;
		filename = argFilename;
		firePropertyChange("filename", old, filename);
	}
	
	public String getFilename(){
		return filename;
	}
	
	public void setTableModel(MetadataTableModel argModel){
		MetadataTableModel old = tableModel;
		tableModel = argModel;
		firePropertyChange("tableModel", old, tableModel);
	}
	
	public MetadataTableModel getTableModel(){
		return tableModel;
	}
}
