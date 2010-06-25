/**
 * Copyright 2010 Daniel Murphy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	
	private String outputFormat = "TRiDaS";
	private MVCArrayList<DefaultMutableTreeNode> nodes = new MVCArrayList<DefaultMutableTreeNode>();
	
	private int processed = 0;
	private int failed = 0;
	private int convWithWarnings = 0;
	
	private ConvertModel() {}
	
	@SuppressWarnings("unchecked")
	public void setNodes(List<DefaultMutableTreeNode> argNodes) {
		MVCArrayList<DefaultMutableTreeNode> old = (MVCArrayList<DefaultMutableTreeNode>) nodes.clone();
		nodes.clear();
		nodes.addAll(argNodes);
		firePropertyChange("nodes", old, nodes.clone());
	}
	
	@SuppressWarnings("unchecked")
	public List<DefaultMutableTreeNode> getNodes() {
		return (List<DefaultMutableTreeNode>) nodes.clone();
	}
	
	
	/**
	 * @param outputFormat
	 *            the outputFormat to set
	 */
	public void setOutputFormat(String argOutputFormat) {
		String old = outputFormat;
		outputFormat = argOutputFormat;
		firePropertyChange("outputFormat", old, outputFormat);
	}
	
	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}
	
	public void setProcessed(int argProcessed) {
		int old = processed;
		processed = argProcessed;
		firePropertyChange("processed", old, processed);
	}
	
	public int getProcessed() {
		return processed;
	}
	
	public void setFailed(int argFailed) {
		int old = failed;
		failed = argFailed;
		firePropertyChange("failed", old, failed);
	}
	
	public int getFailed() {
		return failed;
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
