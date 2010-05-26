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
/**
 * Created on May 22, 2010, 3:50:08 AM
 */
package org.tridas.io.gui.mvc.model;

/**
 * For storing arrays of values, look at {@link CloneableArrayList} for easy
 * cloning.
 * 
 * @author Daniel
 */
public abstract class AbstractDirtyableModel extends AbstractModel implements IDirtyable, ICloneable {

	private boolean dirty = false;

	/**
	 * If the model is "dirty", or changed since last save.
	 * 
	 * @see com.dmurph.mvc.model.IDirtyable#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Call this every time a value is set.
	 * 
	 * @see com.dmurph.mvc.model.IDirtyable#updateDirty(boolean)
	 */
	@Override
	public void updateDirty(boolean argIsDirty) {
		dirty = dirty || argIsDirty;
	}

	/**
	 * @see com.dmurph.mvc.model.IDirtyable#setDirty(boolean)
	 */
	@Override
	public boolean setDirty(boolean argDirty) {
		boolean oldDirty = dirty;
		if (dirty == argDirty) {
			return dirty;
		}

		if (!dirty) {
			save();
		}
		dirty = argDirty;
		return oldDirty;
	}

	/**
	 * Reverts to clean state, gets rid of changes.
	 * 
	 * @see com.dmurph.mvc.model.IDirtyable#clean()
	 */
	@Override
	public boolean clean() {
		boolean oldDirty = dirty;
		revert();
		dirty = false;
		return oldDirty;
	}

	/**
	 * Revert the model to the clean values.
	 */
	protected abstract void revert();

	/**
	 * Save the clean values from the working/dirty values
	 */
	protected abstract void save();

	public abstract ICloneable clone();

}
