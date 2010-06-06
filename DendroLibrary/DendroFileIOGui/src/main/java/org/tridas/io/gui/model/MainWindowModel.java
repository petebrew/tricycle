/**
 * Created at May 20, 2010, 12:47:10 AM
 */
package org.tridas.io.gui.model;

import java.io.File;

import javax.swing.ComboBoxModel;

import org.grlea.log.SimpleLogger;

import com.dmurph.mvc.model.AbstractModel;
/**
 * @author daniel
 */
public class MainWindowModel extends AbstractModel {

	private static final MainWindowModel model = new MainWindowModel();

	private static final SimpleLogger log = new SimpleLogger(MainWindowModel.class);
	
	private boolean lock = false;
	
	private MainWindowModel() {}

	/**
	 * @param lock the lock to set
	 */
	public void setLock(boolean argLock) {
		boolean old = lock;
		lock = argLock;
		firePropertyChange("lock", old, lock);
	}

	/**
	 * @return the lock
	 */
	public boolean isLock() {
		return lock;
	}

	public static MainWindowModel getInstance() {
		return model;
	}
}
