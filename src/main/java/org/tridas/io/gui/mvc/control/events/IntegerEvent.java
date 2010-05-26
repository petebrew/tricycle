/**
 * 1:28:12 AM, Mar 25, 2010
 */
package org.tridas.io.gui.mvc.control.events;

import org.tridas.io.gui.mvc.control.CEvent;

/**
 * @author daniel
 */
public class IntegerEvent extends CEvent {

	private final int integer;

	public IntegerEvent(String argKey, int argInt) {
		super(argKey);
		integer = argInt;
	}

	public int getInteger() {
		return integer;
	}
}
