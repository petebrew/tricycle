/**
 * Created at May 20, 2010, 12:47:10 AM
 */
package org.tridas.io.gui.model;

import java.io.File;

import javax.swing.ComboBoxModel;

import org.grlea.log.SimpleLogger;

import com.dmurph.mvc.model.AbstractModel;
import com.dmurph.mvc.util.MVCArrayList;

/**
 * @author daniel
 */
public class MainWindowModel extends AbstractModel {

	private static final MainWindowModel model = new MainWindowModel();

	private static final SimpleLogger log = new SimpleLogger(MainWindowModel.class);

	private MVCArrayList<File> outputFiles = new MVCArrayList<File>();

	private MainWindowModel() {}

	public static MainWindowModel getInstance() {
		return model;
	}
}
