/**
 * Created on May 27, 2010, 1:54:37 AM
 */
package org.tridas.io.gui.model;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.dmurph.mvc.model.AbstractModel;
import com.dmurph.mvc.util.MVCArrayList;

/**
 * @author Daniel
 */
public class ConvertModel extends AbstractModel {
	
	private static final ConvertModel model = new ConvertModel();
	
	private int savingPercent = 0;
	private String savingFilename = null;
	private MVCArrayList<DefaultMutableTreeNode> nodes = new MVCArrayList<DefaultMutableTreeNode>();
	
	private int processed = 0;
	private int failed = 0;
	private int convWithWarnings = 0;
	
	private ConvertModel() {}
	
	public void setNodes(List<DefaultMutableTreeNode> argNodes) {
		MVCArrayList<DefaultMutableTreeNode> old = (MVCArrayList<DefaultMutableTreeNode>) nodes.clone();
		nodes.clear();
		nodes.addAll(argNodes);
		firePropertyChange("nodes", old, nodes.clone());
	}
	
	public List<DefaultMutableTreeNode> getNodes() {
		return (List<DefaultMutableTreeNode>) nodes.clone();
	}
	
	/**
	 * @param savingPercent
	 *            the savingPercent to set
	 */
	public void setSavingPercent(int argSavingPercent) {
		int old = savingPercent;
		savingPercent = argSavingPercent;
		firePropertyChange("savingPercent", old, savingPercent);
	}
	
	/**
	 * @return the savingPercent
	 */
	public int getSavingPercent() {
		return savingPercent;
	}
	
	/**
	 * @param savingFilename
	 *            the savingFilename to set
	 */
	public void setSavingFilename(String argSavingFilename) {
		String old = savingFilename;
		savingFilename = argSavingFilename;
		firePropertyChange("savingFilename", old, savingFilename);
	}
	
	/**
	 * @return the savingFilename
	 */
	public String getSavingFilename() {
		return savingFilename;
	}
	
	
	public void incrementProcessed(){
		setProcessed(processed +1);
	}

	public void setProcessed(int argProcessed) {
		int old = processed;
		processed = argProcessed;
		firePropertyChange("processed", old, processed);
	}

	public int getProcessed() {
		return processed;
	}

	public void incrementFailed(){
		setFailed(failed +1);
	}
	
	public void setFailed(int argFailed) {
		int old = failed;
		failed = argFailed;
		firePropertyChange("failed", old, failed);
	}

	public int getFailed() {
		return failed;
	}

	public void incrementConvWithWarnings(){
		setConvWithWarnings(convWithWarnings +1);
	}
	
	public void setConvWithWarnings(int argConvWithWarnings) {
		int old = convWithWarnings;
		convWithWarnings = argConvWithWarnings;
		firePropertyChange("convWithWarnings", old, convWithWarnings);
	}

	public int getConvWithWarnings() {
		return convWithWarnings;
	}

	
	public static final ConvertModel getInstance() {
		return model;
	}
}
