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
