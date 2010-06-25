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
 * Created at Jun 14, 2010, 5:09:18 PM
 */
package org.tridas.io.gui.model.popup;

import java.util.HashMap;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;

import org.tridas.io.defaults.AbstractDefaultValue;
import org.tridas.io.defaults.IMetadataFieldSet;

import com.dmurph.mvc.ICloneable;

/**
 * @author daniel
 */
@SuppressWarnings("serial")
public class MetadataTableModel extends AbstractTableModel implements ICloneable {
	
	public static final String[] columns = {"Property", "Value", "Overriding"};
	private final HashMap<Enum, AbstractDefaultValue> map = new HashMap<Enum, AbstractDefaultValue>();
		
	public void setMetadataSet(IMetadataFieldSet argSet){
		map.clear();
		for(Enum e : argSet.keySet()){
			map.put(e, argSet.getDefaultValue(e));
		}
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 3;
	}
	
	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return map.size();
	}
	
	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Enum<?> e = getKeyAt(rowIndex);
		if (columnIndex == 0) {
			return e;
		}
		else if (columnIndex == 1) {
			return map.get(e);
		}
		else if (columnIndex == 2) {
			return map.get(e).isOverriding();
		}
		return null;
	}
	
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col < 1) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void setValueAt(Object value, int row, int col) {
		Enum<?> e = getKeyAt(row);
		if (col == 0) {
			return;
		}
		AbstractDefaultValue<Object> val = map.get(e);
		if (col == 1) {
			if (val.setValue(value)) {
				
				fireTableCellUpdated(row, col);
			}
		}
		else {
			val.setOverriding((Boolean) value);
			fireTableCellUpdated(row, col);
		}
	}
	
	private Enum getKeyAt(int argRow) {
		Iterator<Enum> it = map.keySet().iterator();
		for (int i = 0; i < argRow; i++) {
			it.next();
		}
		
		return it.next();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MetadataTableModel){
			return map.equals( ((MetadataTableModel) obj).map);			
		}
		return false;
	}
	
	/**
	 * @see com.dmurph.mvc.ICloneable#cloneFrom(com.dmurph.mvc.ICloneable)
	 */
	@SuppressWarnings("unchecked")
	public void cloneFrom(ICloneable argOther) {
		MetadataTableModel other = (MetadataTableModel) argOther;
		map.clear();
		for(Enum<?> key: other.map.keySet()){
			map.put(key, (AbstractDefaultValue<Object>) other.map.get(key).clone());
		}
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public ICloneable clone(){
		MetadataTableModel model = new MetadataTableModel();
		model.cloneFrom(this);
		return model;
	}
}
