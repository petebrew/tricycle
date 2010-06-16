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
