/**
 * Created at Jun 16, 2010, 2:26:29 AM
 */
package org.tridas.io.gui.components.editors;

import java.awt.Component;

import javax.swing.JLabel;

import org.tridas.io.defaults.AbstractDefaultValue;
import org.tridas.io.gui.components.DefaultFieldEditor;
import org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate;

/**
 * @author daniel
 *
 */
public class UneditableEditor extends AbstractEditorDelegate {
	
	private AbstractDefaultValue val;
	private JLabel comp = new JLabel();
	
	/**
	 * @param argEditor
	 */
	public UneditableEditor(DefaultFieldEditor argEditor) {
		super(argEditor);
	}

	@Override
	public void setValue(Object argValue) {
		val = (AbstractDefaultValue) argValue;
		comp.setText(val.getStringValue());
	}


	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return val.getValue();
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
	public void revert() {}
}
