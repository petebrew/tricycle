/**
 * Created on May 27, 2010, 2:43:10 AM
 */
package org.tridas.io.gui.control.convert;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class SaveEvent extends MVCEvent {
	
	public SaveEvent() {
		super(ConvertController.SAVE);
	}
}
