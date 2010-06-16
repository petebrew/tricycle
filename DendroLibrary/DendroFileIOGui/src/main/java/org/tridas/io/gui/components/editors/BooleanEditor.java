/**
 * Created at Jun 16, 2010, 4:24:24 PM
 */
package org.tridas.io.gui.components.editors;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.tridas.io.defaults.values.BooleanDefaultValue;
import org.tridas.io.defaults.values.IntegerDefaultValue;
import org.tridas.io.gui.components.DefaultFieldEditor;
import org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate;

/**
 * @author daniel
 *
 */
public class BooleanEditor extends AbstractEditorDelegate{

	public JComboBox comp = new JComboBox(new String[]{"true","false"});
	public Boolean orig = null;
	
	/**
	 * @param argEditor
	 */
	public BooleanEditor(DefaultFieldEditor argEditor) {
		super(argEditor);
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