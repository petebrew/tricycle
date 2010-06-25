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
 * Created at Jun 16, 2010, 2:23:30 AM
 */
package org.tridas.io.gui.components.editors;

import java.awt.Component;

import javax.swing.JTextField;

import org.tridas.io.defaults.values.StringDefaultValue;
import org.tridas.io.gui.components.DefaultFieldEditor;
import org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate;

/**
 * @author daniel
 *
 */
@SuppressWarnings("serial")
public class StringValueEditor extends AbstractEditorDelegate {
	
	public JTextField comp = new JTextField();
	public String orig = null;
	
	/**
	 * @param argEditor
	 */
	public StringValueEditor(DefaultFieldEditor argEditor) {
		super(argEditor);
		comp.addActionListener(this);
	}

	
	/**
	 * @see org.tridas.io.gui.components.editors.ICellFieldEditor#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object argValue) {
		StringDefaultValue val = (StringDefaultValue) argValue;
		orig = val.getValue();
		comp.setText(val.getValue());
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return (comp.getText().equals(""))? null: comp.getText();
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#getComponent()
	 */
	@Override
	public Component getComponent() {
		return comp;
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#revert()
	 */
	@Override
	public void revert() {
		comp.setText(orig);
	}
}
