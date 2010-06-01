/**
 * Created on May 27, 2010, 2:43:10 AM
 */
package org.tridas.io.gui.control.main.convert;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 *
 */
public class SaveEvent extends MVCEvent {
	
	public SaveEvent() {
		super(ConvertController.SAVE);
	}
}
