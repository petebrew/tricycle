/**
 * Copyright (c) 2010 Daniel Murphy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tridas.io.gui.mvc.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * <p>
 * Abstract model class, used for storing data and throwing {@link PropertyChangeEvent}s when values
 * are changed. Extending classes should call {@link #firePropertyChange(String, Object, Object)}
 * for this to work. For storing arrays of values, look at {@link CloneableArrayList} for easy
 * cloning.
 * </p>
 * <p>
 * For models that need to keep track of original values versus new values (like a dirtyable model
 * that revert to original values, used for knowing if something needs saving, etc), then look at
 * {@link AbstractDirtyableModel}.
 * </p>
 * 
 * @see PropertyChangeEvent
 * @see PropertyChangeListener
 * @see AbstractDirtyableModel
 * @author daniel
 */
public abstract class AbstractModel implements ICloneable {
	protected final PropertyChangeSupport propertyChangeSupport;

	public AbstractModel() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	/**
	 * Adds a property change listener to this model
	 * 
	 * @param argListener
	 */
	public void addPropertyChangeListener(PropertyChangeListener argListener) {
		propertyChangeSupport.addPropertyChangeListener(argListener);
	}

	/**
	 * Removes a property change listener to this model
	 * 
	 * @param argListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener argListener) {
		propertyChangeSupport.removePropertyChangeListener(argListener);
	}

	/**
	 * Fires a property change event. If the argOldValue == argNewValue
	 * or argOldValue.equals( argNewValue) then no event is thrown.
	 * 
	 * @param argPropertyName
	 *            property name, should match the get and set methods for property name
	 * @param argOldValue
	 * @param argNewValue
	 */
	protected void firePropertyChange(String argPropertyName, Object argOldValue, Object argNewValue) {
		if (argOldValue.equals(argNewValue)) {
			return;
		}
		// this handles the rest internally
		propertyChangeSupport.firePropertyChange(argPropertyName, argOldValue, argNewValue);
	}

	/**
	 * @see ICloneable#clone()
	 */
	@Override
	public abstract ICloneable clone();
}
