/**
 * Created at Jun 16, 2010, 2:22:00 AM
 */
package org.tridas.io.gui.components.editors;

/**
 * @author daniel
 *
 */
public interface ICellFieldEditor {
	
	public void setValue(Object argValue);
	
	public Object getValue();
	
	public boolean canStart(int argClicks);
}
