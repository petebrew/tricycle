/**
 * Created at Jun 16, 2010, 3:57:31 AM
 */
package org.tridas.io.gui.components.editors;

import java.awt.Component;

import javax.swing.JTextField;

import org.tridas.io.defaults.values.DoubleDefaultValue;
import org.tridas.io.defaults.values.IntegerDefaultValue;
import org.tridas.io.gui.components.DefaultFieldEditor;
import org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate;

/**
 * @author daniel
 *
 */
public class DoubleEditor extends AbstractEditorDelegate{

	public JTextField comp = new JTextField();
	public Double orig = null;
	
	/**
	 * @param argEditor
	 */
	public DoubleEditor(DefaultFieldEditor argEditor) {
		super(argEditor);
		comp.addActionListener(this);
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return (comp.getText().equals(""))? null: Double.parseDouble(comp.getText());
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
		DoubleDefaultValue val = (DoubleDefaultValue) argValue;
		orig = val.getValue();
		comp.setText(val.getStringValue());
	}
	
	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#stopCellEditing()
	 */
	@Override
	public boolean stopCellEditing() {
		try{
			Double.parseDouble(comp.getText().trim());
		}catch (Exception e) {
			return false;
		}
		return super.stopCellEditing();
	}

	/**
	 * @see org.tridas.io.gui.components.DefaultFieldEditor.AbstractEditorDelegate#revert()
	 */
	@Override
	public void revert() {
		if(orig == null){
			comp.setText(null);
		}else{
			comp.setText(orig.toString());
		}
	}
}