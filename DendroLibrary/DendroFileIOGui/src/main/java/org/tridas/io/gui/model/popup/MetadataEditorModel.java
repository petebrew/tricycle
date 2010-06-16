/**
 * Created at Jun 14, 2010, 3:12:20 PM
 */
package org.tridas.io.gui.model.popup;

import javax.swing.table.TableModel;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.model.AbstractDirtyableModel;

/**
 * @author daniel
 *
 */
public class MetadataEditorModel extends AbstractDirtyableModel{
	
	private MetadataTableModel cleanTableModel = null;
	private MetadataTableModel tableModel = null;
	
	private String cleanFilename = null;
	private String filename = null;
	
	public MetadataEditorModel(String argFilename){
		cleanFilename = argFilename;
		filename = argFilename;
	}
	
	public void setFilename(String argFilename){
		updateDirty(!cleanFilename.equals(argFilename));
		String old = filename;
		filename = argFilename;
		firePropertyChange("filename", old, filename);
	}
	
	public String getFilename(){
		return filename;
	}
	
	public void setTableModel(MetadataTableModel argModel){
		updateDirty(cleanTableModel.equals(argModel));
		MetadataTableModel old = tableModel;
		tableModel = argModel;
		firePropertyChange("tableModel", old, tableModel);
	}

	/**
	 * @see com.dmurph.mvc.model.AbstractDirtyableModel#cleanModel()
	 */
	@Override
	protected void cleanModel() {
		cleanFilename = filename;
		if(tableModel != null){
			cleanTableModel = (MetadataTableModel) tableModel.clone();			
		}else{
			cleanTableModel = null;
		}
	}

	/**
	 * @see com.dmurph.mvc.model.AbstractDirtyableModel#clone()
	 */
	@Override
	public ICloneable clone() {
		MetadataEditorModel model = new MetadataEditorModel(filename);
		model.cloneFrom(this);
		return model;
	}

	/**
	 * @see com.dmurph.mvc.model.AbstractDirtyableModel#revertModel()
	 */
	@Override
	protected void revertModel() {
		if(cleanTableModel != null){
			setTableModel((MetadataTableModel) cleanTableModel.clone());
		}else{
			setTableModel(null);
		}
		
		setFilename(cleanFilename);
	}

	/**
	 * @see com.dmurph.mvc.ICloneable#cloneFrom(com.dmurph.mvc.ICloneable)
	 */
	@Override
	public void cloneFrom(ICloneable argOther) {
		MetadataEditorModel other = (MetadataEditorModel) argOther;
		if(other.cleanTableModel != null){
			cleanTableModel = (MetadataTableModel) other.cleanTableModel.clone();			
		}else{
			cleanTableModel = null;
		}
		
		if(other.tableModel != null){
			setTableModel((MetadataTableModel) other.tableModel.clone());
		}else{
			setTableModel(null);
		}
		
		cleanFilename = other.cleanFilename;
		setFilename(other.filename);
	}
}
