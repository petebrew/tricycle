/**
 * Created at May 20, 2010, 12:47:10 AM
 */
package org.tridas.io.gui.model;

import java.io.File;

import javax.swing.ComboBoxModel;

import org.grlea.log.SimpleLogger;
import org.tridas.io.gui.mvc.model.AbstractModel;
import org.tridas.io.gui.mvc.model.CloneableArrayList;
import org.tridas.io.gui.mvc.model.ICloneable;

/**
 * @author daniel
 */
public class MainWindowModel extends AbstractModel {

	private static final MainWindowModel model = new MainWindowModel();

	private static final SimpleLogger log = new SimpleLogger(MainWindowModel.class);

	private CloneableArrayList<File> outputFiles = new CloneableArrayList<File>();

	private MainWindowModel() {}

	public static MainWindowModel getInstance() {
		return model;
	}
}
