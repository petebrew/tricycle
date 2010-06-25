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
 * Created at Jun 16, 2010, 4:24:24 PM
 */
package org.tridas.io.gui.components.editors;

import java.awt.Component;

import javax.swing.JComboBox;
import org.tridas.io.defaults.values.BooleanDefaultValue;
import org.tridas.io.gui.components.DefaultFieldEditor;
import org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate;

/**
 * @author daniel
 *
 */
@SuppressWarnings("serial")
public class BooleanEditor extends AbstractEditorDelegate{

	public JComboBox comp = new JComboBox(new String[]{"true","false"});
	public Boolean orig = null;
	
	/**
	 * @param argEditor
	 */
	public BooleanEditor(DefaultFieldEditor argEditor) {
		super(argEditor);
		comp.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		comp.addItemListener(this);
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return Boolean.parseBoolean(comp.getSelectedItem().toString());
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#getComponent()
	 */
	@Override
	public Component getComponent() {
		return comp;
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object argValue) {
		BooleanDefaultValue val = (BooleanDefaultValue) argValue;
		orig = val.getValue();
		comp.setSelectedItem(orig.toString());
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#revert()
	 */
	@Override
	public void revert() {
		comp.setSelectedItem(orig.toString());
	}
}