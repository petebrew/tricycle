/**
 * 1:29:25 AM, Mar 25, 2010
 */
package org.tridas.io.gui.mvc.control.events;

import org.tridas.io.gui.mvc.control.MVCEvent;

/**
 * @author daniel
 */
public class ObjectEvent<K> extends MVCEvent {

	private final K object;

	/**
	 * @param argKey
	 */
	public ObjectEvent(String argKey, K argObject) {
		super(argKey);
		object = argObject;
	}

	public K getObject() {
		return object;
	}
}
