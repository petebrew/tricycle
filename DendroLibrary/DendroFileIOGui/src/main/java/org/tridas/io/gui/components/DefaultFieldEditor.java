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

package org.tridas.io.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.tridas.io.defaults.values.BooleanDefaultValue;
import org.tridas.io.defaults.values.DoubleDefaultValue;
import org.tridas.io.defaults.values.IntegerDefaultValue;
import org.tridas.io.defaults.values.StringDefaultValue;
import org.tridas.io.gui.components.editors.BooleanEditor;
import org.tridas.io.gui.components.editors.DoubleEditor;
import org.tridas.io.gui.components.editors.IntegerEditor;
import org.tridas.io.gui.components.editors.StringValueEditor;
import org.tridas.io.gui.components.editors.UneditableEditor;

/**
 * @author daniel
 */
@SuppressWarnings("serial")
public class DefaultFieldEditor extends AbstractCellEditor implements TableCellEditor {
	
	public AbstractEditorDelegate delegate = null;

	//
	// Override the implementations of the superclass, forwarding all methods
	// from the CellEditor interface to our delegate.
	//
	
	/**
	 * Forwards the message from the <code>CellEditor</code> to
	 * the <code>delegate</code>.
	 * 
	 * @see AbstractEditorDelegate#getCellEditorValue
	 */
	public Object getCellEditorValue() {
		return delegate.getCellEditorValue();
	}
	
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			return ((MouseEvent) anEvent).getClickCount() >= 2;
		}
		return true;
	}
	
	/**
	 * Forwards the message from the <code>CellEditor</code> to
	 * the <code>delegate</code>.
	 * 
	 * @see AbstractEditorDelegate#shouldSelectCell(EventObject)
	 */
	public boolean shouldSelectCell(EventObject anEvent) {
		return delegate.shouldSelectCell(anEvent);
	}
	
	/**
	 * Forwards the message from the <code>CellEditor</code> to
	 * the <code>delegate</code>.
	 * 
	 * @see AbstractEditorDelegate#stopCellEditing
	 */
	public boolean stopCellEditing() {
		return delegate.stopCellEditing();
	}
	
	/**
	 * Forwards the message from the <code>CellEditor</code> to
	 * the <code>delegate</code>.
	 * 
	 * @see AbstractEditorDelegate#cancelCellEditing
	 */
	public void cancelCellEditing() {
		delegate.cancelCellEditing();
	}
	
	/**
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
	 *      java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable argTable, Object argValue, boolean argIsSelected, int argRow,
			int argColumn) {
		if (argValue == null) {
			delegate = new UneditableEditor(this);
		}
		else {
			if (argValue instanceof StringDefaultValue) {
				delegate = new StringValueEditor(this);
			}
			else if (argValue instanceof IntegerDefaultValue) {
				delegate = new IntegerEditor(this);
			}else if(argValue instanceof DoubleDefaultValue){
				delegate = new DoubleEditor(this);
			}else if(argValue instanceof BooleanDefaultValue){
				delegate = new BooleanEditor(this);
			}
			else {
				delegate = new UneditableEditor(this);
			}
		}
		delegate.setValue(argValue);
		return delegate.getComponent();
	}
	
	/**
	 * The protected <code>EditorDelegate</code> class.
	 */
	public static abstract class AbstractEditorDelegate implements ActionListener, ItemListener, Serializable {
		
		public DefaultFieldEditor editor;
		
		public AbstractEditorDelegate(DefaultFieldEditor argEditor){
			editor = argEditor;
		}
		
		public abstract Component getComponent();
				
		/**
		 * Returns the value of this cell.
		 * 
		 * @return the value of this cell
		 */
		public abstract Object getCellEditorValue();
		
		/**
		 * Sets the value of this cell.
		 * 
		 * @param value
		 *            the new value of this cell
		 */
		public abstract void setValue(Object value);
		
		
		public abstract void revert();
		
		/**
		 * Returns true to indicate that the editing cell may
		 * be selected.
		 * 
		 * @param anEvent
		 *            the event
		 * @return true
		 * @see #isCellEditable
		 */
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}
		
		/**
		 * Returns true to indicate that editing has begun.
		 * 
		 * @param anEvent
		 *            the event
		 */
		public boolean startCellEditing(EventObject anEvent) {
			return true;
		}
		
		/**
		 * Stops editing and
		 * returns true to indicate that editing has stopped.
		 * This method calls <code>fireEditingStopped</code>.
		 * 
		 * @return true
		 */
		public boolean stopCellEditing() {
			editor.fireEditingStopped();
			return true;
		}
		
		/**
		 * Cancels editing. This method calls <code>fireEditingCanceled</code>.
		 */
		public void cancelCellEditing() {
			editor.fireEditingCanceled();
		}
		
		/**
		 * When an action is performed, editing is ended.
		 * 
		 * @param e
		 *            the action event
		 * @see #stopCellEditing
		 */
		public void actionPerformed(ActionEvent e) {
			editor.stopCellEditing();
		}
		
		/**
		 * When an item's state changes, editing is ended.
		 * 
		 * @param e
		 *            the action event
		 * @see #stopCellEditing
		 */
		public void itemStateChanged(ItemEvent e) {
			editor.stopCellEditing();
		}
	}
}
